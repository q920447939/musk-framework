package org.example.musk.functions.member.level.service;

import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.functions.member.level.model.entity.MemberLevelDefinitionDO;
import org.example.musk.functions.member.level.model.vo.MemberLevelChangeRecordVO;
import org.example.musk.functions.member.level.model.vo.MemberLevelDefinitionCreateReqVO;
import org.example.musk.functions.member.level.model.vo.MemberLevelDefinitionUpdateReqVO;
import org.example.musk.functions.member.level.model.vo.MemberLevelInfoVO;
import org.example.musk.functions.member.level.model.vo.MemberLevelProgressVO;

import java.util.List;

/**
 * 会员等级服务接口
 *
 * @author musk-functions-member-level
 */
public interface MemberLevelService {

    /**
     * 创建会员等级定义
     *
     * @param createReqVO 创建请求
     * @return 等级ID
     */
    Integer createLevelDefinition(Integer tenantId,Integer domainId,MemberLevelDefinitionCreateReqVO createReqVO);

    /**
     * 更新会员等级定义
     *
     * @param updateReqVO 更新请求
     */
    void updateLevelDefinition(Integer tenantId,Integer domainId,MemberLevelDefinitionUpdateReqVO updateReqVO);

    /**
     * 删除会员等级定义
     *
     * @param id 等级ID
     */
    void deleteLevelDefinition(Integer tenantId,Integer domainId,Integer id);

    /**
     * 获取会员等级定义
     *
     * @param id 等级ID
     * @return 等级定义
     */
    MemberLevelDefinitionDO getLevelDefinition(Integer id);

    /**
     * 获取会员等级定义列表
     *
     * @param tenantId 租户ID
     * @param domainId 域ID
     * @return 等级定义列表
     */
    List<MemberLevelDefinitionDO> getLevelDefinitionList(Integer tenantId, Integer domainId);

    /**
     * 获取会员当前等级
     *
     * @param memberId 会员ID
     * @return 会员等级信息
     */
    MemberLevelInfoVO getMemberCurrentLevel(Integer domainId,Integer memberId);

    /**
     * 手动设置会员等级
     *
     * @param memberId 会员ID
     * @param levelId 等级ID
     * @param reason 原因
     * @param operator 操作人
     */
    void setMemberLevel(Integer tenantId, Integer domainId,Integer memberId, Integer levelId, String reason, String operator);

    /**
     * 计算会员等级
     * 根据会员成长值计算应该处于的等级
     *
     * @param memberId 会员ID
     * @return 计算后的等级ID
     */
    Integer calculateMemberLevel(Integer domainId,Integer memberId);

    /**
     * 获取会员等级变更历史
     *
     * @param memberId 会员ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 变更历史分页结果
     */
    PageResult<MemberLevelChangeRecordVO> getMemberLevelChangeHistory(Integer memberId, Integer pageNum, Integer pageSize);

    /**
     * 计算会员升级进度
     *
     * @param memberId 会员ID
     * @return 升级进度信息
     */
    MemberLevelProgressVO calculateMemberLevelProgress(Integer domainId,Integer memberId);
}
