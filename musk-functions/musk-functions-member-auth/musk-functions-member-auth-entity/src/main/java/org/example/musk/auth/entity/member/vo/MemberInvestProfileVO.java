package org.example.musk.auth.entity.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 会员 DO
 *
 * @author musk
 */
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberInvestProfileVO {


    /**
     * 会员账号
     */
    private String memberCode;

    /**
     * 会员昵称
     */
    private String memberNickName;

    /**
     * 会员识别id
     */
    private Integer memberSimpleId;


    /**
     * 头像地址
     */
    private String avatar;



}
