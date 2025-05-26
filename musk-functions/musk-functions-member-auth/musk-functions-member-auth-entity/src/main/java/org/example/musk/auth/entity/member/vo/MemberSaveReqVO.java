package org.example.musk.auth.entity.member.vo;

import lombok.Data;

@Data
public class MemberSaveReqVO {


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
