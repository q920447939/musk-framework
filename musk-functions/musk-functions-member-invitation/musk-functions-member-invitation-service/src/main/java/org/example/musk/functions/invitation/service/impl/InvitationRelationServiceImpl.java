package org.example.musk.functions.invitation.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.exception.BusinessException;
import org.example.musk.constant.db.DBConstant;
import org.example.musk.functions.cache.annotation.CacheEvict;
import org.example.musk.functions.cache.annotation.Cacheable;
import org.example.musk.functions.invitation.dao.entity.MemberInvitationRelationDO;
import org.example.musk.functions.invitation.dao.mapper.MemberInvitationRelationMapper;
import org.example.musk.functions.invitation.service.InvitationRelationService;
import org.example.musk.plugin.lock.config.anno.PluginLockSafeExec;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 邀请关系服务实现类
 *
 * @author musk-functions-member-invitation
 */
@Service
@Slf4j
@DS(DBConstant.MEMBER)
public class InvitationRelationServiceImpl implements InvitationRelationService {

    @Resource
    private MemberInvitationRelationMapper invitationRelationMapper;

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    @CacheEvict(namespace = "INVITATION_RELATION", pattern = "*")
    @PluginLockSafeExec(lockKey = "invitation:relation:#{#inviteeMemberId}", ttl = 30)
    public MemberInvitationRelationDO createInvitationRelation(String invitationCode, Integer inviterMemberId,
                                                               Integer inviteeMemberId, String registerIp,
                                                               String registerChannel) {
        // 参数验证
        if (StrUtil.isBlank(invitationCode)) {
            throw new BusinessException("邀请码不能为空");
        }
        if (inviterMemberId == null) {
            throw new BusinessException("邀请人会员ID不能为空");
        }
        if (inviteeMemberId == null) {
            throw new BusinessException("被邀请人会员ID不能为空");
        }

        // 验证邀请关系的合法性
        if (!validateInvitationRelation(inviterMemberId, inviteeMemberId)) {
            throw new BusinessException("邀请关系不合法");
        }

        // 检查是否已存在邀请关系
        MemberInvitationRelationDO existingRelation = invitationRelationMapper.selectByInviteeMemberId(inviteeMemberId);
        if (existingRelation != null) {
            log.warn("被邀请人已存在邀请关系，inviteeMemberId={}, existingInviterMemberId={}",
                    inviteeMemberId, existingRelation.getInviterMemberId());
            throw new BusinessException("该用户已被邀请");
        }

        try {
            // 创建邀请关系
            MemberInvitationRelationDO relationEntity = MemberInvitationRelationDO.builder()
                    .invitationCode(invitationCode)
                    .inviterMemberId(inviterMemberId)
                    .inviteeMemberId(inviteeMemberId)
                    .registerTime(LocalDateTime.now())
                    .registerIp(registerIp)
                    .registerChannel(registerChannel)
                    .status(1) // 有效状态
                    .build();

            int result = invitationRelationMapper.insert(relationEntity);
            if (result <= 0) {
                throw new BusinessException("创建邀请关系失败");
            }

            log.info("创建邀请关系成功，invitationCode={}, inviterMemberId={}, inviteeMemberId={}",
                    invitationCode, inviterMemberId, inviteeMemberId);

            return relationEntity;

        } catch (Exception e) {
            log.error("创建邀请关系异常，invitationCode={}, inviterMemberId={}, inviteeMemberId={}",
                    invitationCode, inviterMemberId, inviteeMemberId, e);
            throw new BusinessException("创建邀请关系失败：" + e.getMessage());
        }
    }

    @Override
    @Cacheable(namespace = "INVITATION_RELATION", key = "'invitee:' + #inviteeMemberId")
    public MemberInvitationRelationDO getInvitationRelationByInvitee(Integer inviteeMemberId) {
        if (inviteeMemberId == null) {
            return null;
        }
        return invitationRelationMapper.selectByInviteeMemberId(inviteeMemberId);
    }

    @Override
    @Cacheable(namespace = "INVITATION_RELATION", key = "'inviter:' + #inviteeMemberId")
    public List<MemberInvitationRelationDO> getInvitationRelationsByInviter(Integer inviterMemberId) {
        if (inviterMemberId == null) {
            throw new BusinessException("邀请人会员ID不能为空");
        }
        return invitationRelationMapper.selectByInviterMemberId(inviterMemberId);
    }

    @Override
    @Cacheable(namespace = "INVITATION_RELATION", key = "'code:' + #invitationCode")
    public List<MemberInvitationRelationDO> getInvitationRelationsByCode(String invitationCode) {
        if (StrUtil.isBlank(invitationCode)) {
            throw new BusinessException("邀请码不能为空");
        }
        return invitationRelationMapper.selectByInvitationCode(invitationCode);
    }

    @Override
    @Cacheable(namespace = "INVITATION_RELATION", key = "'count:' + #inviterMemberId")
    public Long countInvitationsByInviter(Integer inviterMemberId) {
        if (inviterMemberId == null) {
            return 0L;
        }
        return invitationRelationMapper.countByInviterMemberId(inviterMemberId);
    }

    @Override
    public boolean existsInvitationRelation(Integer inviterMemberId, Integer inviteeMemberId) {
        if (inviterMemberId == null || inviteeMemberId == null) {
            return false;
        }
        return invitationRelationMapper.existsRelation(inviterMemberId, inviteeMemberId);
    }

    @Override
    public boolean validateInvitationRelation(Integer inviterMemberId, Integer inviteeMemberId) {
        // 不能邀请自己
        if (inviterMemberId.equals(inviteeMemberId)) {
            log.warn("不能邀请自己，memberId={}", inviterMemberId);
            return false;
        }

        // 检查是否存在循环邀请
        if (checkCircularInvitation(inviterMemberId, inviteeMemberId)) {
            log.warn("存在循环邀请，inviterMemberId={}, inviteeMemberId={}", inviterMemberId, inviteeMemberId);
            return false;
        }

        return true;
    }

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    @CacheEvict(namespace = "INVITATION_RELATION",pattern = "*")
    public boolean invalidateInvitationRelation(Long id) {
        if (id == null) {
            throw new BusinessException("邀请关系ID不能为空");
        }

        MemberInvitationRelationDO updateEntity = new MemberInvitationRelationDO();
        updateEntity.setId(id);
        updateEntity.setStatus(2); // 失效状态

        int result = invitationRelationMapper.updateById(updateEntity);
        if (result > 0) {
            log.info("失效邀请关系成功，id={}", id);
            return true;
        }

        return false;
    }

    @Override
    public MemberInvitationRelationDO getInvitationRelationById(Long id) {
        if (id == null) {
            return null;
        }
        return invitationRelationMapper.selectById(id);
    }

    /**
     * 检查循环邀请
     *
     * 检查逻辑：如果A邀请B，那么B不能邀请A（直接循环）
     * 也不能邀请A的邀请人（间接循环）
     *
     * @param inviterMemberId 邀请人会员ID
     * @param inviteeMemberId 被邀请人会员ID
     * @return 是否存在循环邀请
     */
    private boolean checkCircularInvitation(Integer inviterMemberId, Integer inviteeMemberId) {
        try {
            // 检查被邀请人是否曾经邀请过邀请人
            if (invitationRelationMapper.existsRelation(inviteeMemberId, inviterMemberId)) {
                return true;
            }

            // 检查被邀请人的邀请人链条中是否包含当前邀请人
            MemberInvitationRelationDO inviteeRelation = invitationRelationMapper.selectByInviteeMemberId(inviteeMemberId);
            if (inviteeRelation != null) {
                // 递归检查邀请链条（最多检查5层，防止无限递归）
                return checkInvitationChain(inviteeRelation.getInviterMemberId(), inviterMemberId, 5);
            }

            return false;

        } catch (Exception e) {
            log.error("检查循环邀请异常，inviterMemberId={}, inviteeMemberId={}", inviterMemberId, inviteeMemberId, e);
            // 异常情况下，为了安全起见，认为存在循环邀请
            return true;
        }
    }

    /**
     * 递归检查邀请链条
     *
     * @param currentMemberId 当前会员ID
     * @param targetMemberId 目标会员ID
     * @param maxDepth 最大深度
     * @return 是否在链条中找到目标会员
     */
    private boolean checkInvitationChain(Integer currentMemberId, Integer targetMemberId, int maxDepth) {
        if (maxDepth <= 0 || currentMemberId == null) {
            return false;
        }

        if (currentMemberId.equals(targetMemberId)) {
            return true;
        }

        MemberInvitationRelationDO relation = invitationRelationMapper.selectByInviteeMemberId(currentMemberId);
        if (relation != null) {
            return checkInvitationChain(relation.getInviterMemberId(), targetMemberId, maxDepth - 1);
        }

        return false;
    }

}
