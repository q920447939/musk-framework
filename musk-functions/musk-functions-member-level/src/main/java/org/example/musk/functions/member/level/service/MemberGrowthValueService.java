package org.example.musk.functions.member.level.service;

import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.functions.member.level.enums.GrowthValueSourceTypeEnum;
import org.example.musk.functions.member.level.model.vo.MemberGrowthValueRecordVO;
import org.example.musk.functions.member.level.model.vo.MemberGrowthValueVO;

/**
 * 会员成长值服务接口
 *
 * @author musk-functions-member-level
 */
public interface MemberGrowthValueService {

    /**
     * 增加会员成长值
     *
     * @param memberId 会员ID
     * @param growthValue 成长值
     * @param sourceType 来源类型
     * @param sourceId 来源ID
     * @param description 描述
     * @param operator 操作人
     * @return 操作后的成长值
     */
    Integer addGrowthValue(Integer memberId, Integer growthValue, GrowthValueSourceTypeEnum sourceType, String sourceId, String description, String operator);

    /**
     * 减少会员成长值
     *
     * @param memberId 会员ID
     * @param growthValue 成长值
     * @param sourceType 来源类型
     * @param sourceId 来源ID
     * @param description 描述
     * @param operator 操作人
     * @return 操作后的成长值
     */
    Integer deductGrowthValue(Integer memberId, Integer growthValue, GrowthValueSourceTypeEnum sourceType, String sourceId, String description, String operator);

    /**
     * 获取会员成长值信息
     *
     * @param memberId 会员ID
     * @return 成长值信息
     */
    MemberGrowthValueVO getMemberGrowthValue(Integer memberId);

    /**
     * 获取会员成长值变更历史
     *
     * @param memberId 会员ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 变更历史分页结果
     */
    PageResult<MemberGrowthValueRecordVO> getMemberGrowthValueHistory(Integer memberId, Integer pageNum, Integer pageSize);
}
