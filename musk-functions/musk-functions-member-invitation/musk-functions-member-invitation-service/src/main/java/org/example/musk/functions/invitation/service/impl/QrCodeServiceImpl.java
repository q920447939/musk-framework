package org.example.musk.functions.invitation.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.exception.BusinessException;
import org.example.musk.constant.db.DBConstant;
import org.example.musk.functions.invitation.service.QrCodeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * 二维码服务实现类
 *
 * @author musk-functions-member-invitation
 */
@Service
@Slf4j
@DS(DBConstant.MEMBER)
public class QrCodeServiceImpl implements QrCodeService {

    @Value("${musk.invitation.qrcode.storage-path:/tmp/qrcode}")
    private String qrCodeStoragePath;

    @Value("${musk.invitation.qrcode.base-url:http://localhost:8080/qrcode}")
    private String qrCodeBaseUrl;

    @Value("${musk.invitation.qrcode.default-size:300}")
    private Integer defaultSize;

    private static final String IMAGE_FORMAT = "PNG";

    @Override
    public String generateQrCode(String content) {
        return generateCustomQrCode(content, null, defaultSize);
    }

    @Override
    public String generateCustomQrCode(String content, String logoUrl, Integer size) {
        if (StrUtil.isBlank(content)) {
            throw new BusinessException("二维码内容不能为空");
        }
        if (size == null || size <= 0) {
            size = defaultSize;
        }

        try {
            // 生成二维码
            BufferedImage qrImage = generateQrCodeImage(content, size);

            // 如果有Logo，添加Logo
            if (StrUtil.isNotBlank(logoUrl)) {
                qrImage = addLogoToQrCode(qrImage, logoUrl);
            }

            // 保存图片并返回URL
            String fileName = IdUtil.simpleUUID() + "." + IMAGE_FORMAT.toLowerCase();
            String filePath = saveQrCodeImage(qrImage, fileName);

            String qrCodeUrl = qrCodeBaseUrl + "/" + fileName;
            log.info("生成二维码成功，content={}, size={}, url={}", content, size, qrCodeUrl);

            return qrCodeUrl;

        } catch (Exception e) {
            log.error("生成二维码失败，content={}, size={}", content, size, e);
            throw new BusinessException("生成二维码失败：" + e.getMessage());
        }
    }

    /**
     * 生成二维码图片
     *
     * @param content 内容
     * @param size 尺寸
     * @return 二维码图片
     */
    private BufferedImage generateQrCodeImage(String content, int size) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);

        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, size, size, hints);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    /**
     * 为二维码添加Logo
     *
     * @param qrImage 二维码图片
     * @param logoUrl Logo URL
     * @return 带Logo的二维码图片
     */
    private BufferedImage addLogoToQrCode(BufferedImage qrImage, String logoUrl) {
        try {
            // 这里简化处理，实际项目中可以从URL下载Logo图片
            // BufferedImage logoImage = downloadImageFromUrl(logoUrl);

            int qrWidth = qrImage.getWidth();
            int qrHeight = qrImage.getHeight();

            // Logo尺寸为二维码的1/5
            int logoSize = qrWidth / 5;

            Graphics2D g2d = qrImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 在中心位置绘制白色背景
            int logoX = (qrWidth - logoSize) / 2;
            int logoY = (qrHeight - logoSize) / 2;

            g2d.setColor(Color.WHITE);
            g2d.fillRoundRect(logoX - 5, logoY - 5, logoSize + 10, logoSize + 10, 10, 10);

            // 这里可以绘制实际的Logo图片
            // g2d.drawImage(logoImage, logoX, logoY, logoSize, logoSize, null);

            g2d.dispose();

            return qrImage;

        } catch (Exception e) {
            log.warn("添加Logo失败，使用原始二维码，logoUrl={}", logoUrl, e);
            return qrImage;
        }
    }

    /**
     * 保存二维码图片
     *
     * @param qrImage 二维码图片
     * @param fileName 文件名
     * @return 文件路径
     */
    private String saveQrCodeImage(BufferedImage qrImage, String fileName) throws IOException {
        // 确保存储目录存在
        Path storagePath = Paths.get(qrCodeStoragePath);
        if (!Files.exists(storagePath)) {
            Files.createDirectories(storagePath);
        }

        // 保存图片
        Path filePath = storagePath.resolve(fileName);
        ImageIO.write(qrImage, IMAGE_FORMAT, filePath.toFile());

        return filePath.toString();
    }

}
