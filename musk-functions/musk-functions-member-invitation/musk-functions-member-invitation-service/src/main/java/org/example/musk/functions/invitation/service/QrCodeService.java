package org.example.musk.functions.invitation.service;

/**
 * 二维码服务接口
 *
 * @author musk-functions-member-invitation
 */
public interface QrCodeService {

    /**
     * 生成二维码
     *
     * @param content 二维码内容
     * @return 二维码图片URL
     */
    String generateQrCode(String content);

    /**
     * 生成自定义样式二维码
     *
     * @param content 二维码内容
     * @param logoUrl Logo图片URL（可选）
     * @param size 二维码尺寸
     * @return 二维码图片URL
     */
    String generateCustomQrCode(String content, String logoUrl, Integer size);

}
