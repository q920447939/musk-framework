package org.example.musk.auth.web.vo.req;

import lombok.Data;

@Data
public class MemberSaveReqVO {

    /**
     * 注册渠道
     *
     * 枚举 {@link TODO member_register_channel 对应的类}
     */
    private Integer registerChannel;

    /**
     * 会员账号
     */
    private String memberCode;

    /**
//     * 会员昵称
//     */
    private String memberNickName;
    /**
     * 密码
     */
    private String password;

    /**
     * 会员识别id
     */
    private Integer memberSimpleId;

    private int inviteMemberSimpleId;



    /**
     * 头像地址
     */
    private String avatar;


    /**
     * 状态枚举
     */
    private Integer status;
}
