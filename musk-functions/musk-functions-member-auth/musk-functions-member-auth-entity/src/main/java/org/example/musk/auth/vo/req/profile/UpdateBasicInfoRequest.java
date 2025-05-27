package org.example.musk.auth.vo.req.profile;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 修改基础信息请求
 *
 * @author musk
 */
@Data
public class UpdateBasicInfoRequest {

    /**
     * 昵称
     */
    @Size(max = 30, message = "昵称长度不能超过30个字符")
    private String nickname;

    /**
     * 头像URL
     */
    @Size(max = 500, message = "头像URL长度不能超过500个字符")
    private String avatar;

    /**
     * 个性签名
     */
    @Size(max = 200, message = "个性签名长度不能超过200个字符")
    private String signature;

    /**
     * 性别（0-未知，1-男，2-女）
     */
    private Integer gender;

    /**
     * 生日（格式：yyyy-MM-dd）
     */
    private String birthday;

    /**
     * 所在地区
     */
    @Size(max = 100, message = "所在地区长度不能超过100个字符")
    private String location;
}
