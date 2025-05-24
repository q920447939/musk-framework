package org.example.musk.functions.resource.processor.impl;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.functions.resource.config.ResourceProperties;
import org.example.musk.functions.resource.processor.ResourceProcessor;
import org.example.musk.functions.resource.util.ResourceTypeUtils;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

/**
 * 图片压缩处理器
 *
 * @author musk-functions-resource
 */
@Component
@Slf4j
public class ImageCompressProcessor implements ResourceProcessor {

    @Resource
    private ResourceProperties resourceProperties;

    @Override
    public InputStream process(InputStream inputStream, String fileType) {
        // 检查是否启用图片压缩
        if (!resourceProperties.getProcess().getImage().getCompressEnabled()) {
            return inputStream;
        }

        try {
            // 读取图片
            BufferedImage image = ImgUtil.read(inputStream);
            if (image == null) {
                log.warn("读取图片失败，跳过压缩处理");
                return inputStream;
            }

            // 获取压缩质量
            float quality = resourceProperties.getProcess().getImage().getCompressQuality() / 100.0f;

            // 压缩图片
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
            ImgUtil.write(image, fileType, imageOutputStream, quality);
            imageOutputStream.close();

            // 返回压缩后的图片输入流
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (Exception e) {
            log.error("压缩图片失败", e);
            // 发生异常时返回原始输入流
            return inputStream;
        }
    }

    @Override
    public boolean supports(String fileType) {
        return ResourceTypeUtils.isImageType(fileType);
    }

    @Override
    public String getName() {
        return "图片压缩处理器";
    }

    @Override
    public int getOrder() {
        return 100;
    }
}
