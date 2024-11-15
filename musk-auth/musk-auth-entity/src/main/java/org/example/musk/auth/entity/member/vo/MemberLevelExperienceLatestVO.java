package org.example.musk.auth.entity.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 用户等级经验值 DO
 *
 * @author 马斯克源码
 */
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLevelExperienceLatestVO {

    /**
     * 会员当前等级
     */
    private Integer memberCurLevel;
    /**
     * 会员当前等级的经验值
     */
    private Integer memberCurExperience;

    /**
     * 升级需要的经验值
     */
    private Integer maxExperience;
    private String levelDescript;

}
