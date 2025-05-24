package org.example.musk.functions.resource.processor.impl;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.functions.resource.config.ResourceProperties;
import org.example.musk.functions.resource.constant.ResourceConstant;
import org.example.musk.functions.resource.processor.ResourceProcessor;
import org.example.musk.functions.resource.util.ResourceTypeUtils;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * 图片缩略图生成处理器
 *
 * @author musk-functions-resource
 */
@Component
@Slf4j
public class ImageThumbnailProcessor implements ResourceProcessor {

    @Resource
    private ResourceProperties resourceProperties;

    @Override
    public InputStream process(InputStream inputStream, String fileType) {
        // 检查是否启用缩略图生成
        if (!resourceProperties.getProcess().getImage().getThumbnailEnabled()) {
            return inputStream;
        }

        try {
            // 读取图片
            byte[] imageBytes = inputStream.readAllBytes();
            BufferedImage image = ImgUtil.read(new ByteArrayInputStream(imageBytes));
            if (image == null) {
                log.warn("读取图片失败，跳过缩略图生成");
                return new ByteArrayInputStream(imageBytes);
            }

            // 获取缩略图尺寸
            String thumbnailSize = resourceProperties.getProcess().getImage().getThumbnailSize();
            if (StrUtil.isBlank(thumbnailSize)) {
                thumbnailSize = ResourceConstant.DEFAULT_THUMBNAIL_SIZE;
            }

            // 解析缩略图尺寸
            String[] sizeArray = thumbnailSize.split("x");
            if (sizeArray.length != 2) {
                log.warn("缩略图尺寸格式错误: {}, 使用默认尺寸", thumbnailSize);
                sizeArray = ResourceConstant.DEFAULT_THUMBNAIL_SIZE.split("x");
            }

            int width = Integer.parseInt(sizeArray[0]);
            int height = Integer.parseInt(sizeArray[1]);

            // 生成缩略图
            Image scale = ImgUtil.scale(image, width, height);

            // 保存缩略图
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImgUtil.write(scale, fileType, outputStream);

            // TODO: 这里可以将缩略图保存到存储中，并关联到原始资源

            // 返回原始图片输入流
            return new ByteArrayInputStream(imageBytes);
        } catch (Exception e) {
            log.error("生成缩略图失败", e);
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
        return "图片缩略图生成处理器";
    }

    @Override
    public int getOrder() {
        return 200;
    }
}
