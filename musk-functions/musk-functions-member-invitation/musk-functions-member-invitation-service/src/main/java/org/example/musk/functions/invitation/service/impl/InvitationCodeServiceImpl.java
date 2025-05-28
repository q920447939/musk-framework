package org.example.musk.functions.invitation.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.exception.BusinessException;
import org.example.musk.constant.db.DBConstant;
import org.example.musk.functions.cache.annotation.CacheEvict;
import org.example.musk.functions.cache.annotation.Cacheable;
import org.example.musk.functions.invitation.dao.entity.MemberInvitationCodeDO;
import org.example.musk.functions.invitation.dao.enums.InvitationCodeStatusEnum;
import org.example.musk.functions.invitation.dao.enums.InvitationCodeTypeEnum;
import org.example.musk.functions.invitation.dao.mapper.MemberInvitationCodeMapper;
import org.example.musk.functions.invitation.service.InvitationCodeService;
import org.example.musk.functions.invitation.service.generator.InvitationCodeGenerator;
import org.example.musk.plugin.lock.config.anno.PluginLockSafeExec;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 邀请码服务实现类
 *
 * @author musk-functions-member-invitation
 */
@Service
@Slf4j
@DS(DBConstant.MEMBER)
public class InvitationCodeServiceImpl implements InvitationCodeService {

    @Resource
    private MemberInvitationCodeMapper invitationCodeMapper;

    @Resource
    private InvitationCodeGenerator invitationCodeGenerator;

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    @PluginLockSafeExec(lockKey = "invitation:generate:#{#memberId}", ttl = 30)
    public MemberInvitationCodeDO generateInvitationCode(Integer memberId, InvitationCodeTypeEnum codeType,
                                                         Integer maxUseCount, Integer expireHours) {
        if (memberId == null) {
            throw new BusinessException("会员ID不能为空");
        }
        if (codeType == null) {
            throw new BusinessException("邀请码类型不能为空");
        }

        try {
            // 生成邀请码
            String invitationCode = generateUniqueCode(memberId, codeType);

            // 计算过期时间
            LocalDateTime expireTime = null;
            if (expireHours != null && expireHours > 0) {
                expireTime = LocalDateTime.now().plusHours(expireHours);
            }

            // 创建邀请码实体
            MemberInvitationCodeDO codeEntity = MemberInvitationCodeDO.builder()
                    .invitationCode(invitationCode)
                    .inviterMemberId(memberId)
                    .codeType(codeType.getCode())
                    .status(InvitationCodeStatusEnum.VALID.getCode())
                    .maxUseCount(maxUseCount)
                    .usedCount(0)
                    .expireTime(expireTime)
                    .build();

            // 保存到数据库
            int result = invitationCodeMapper.insert(codeEntity);
            if (result <= 0) {
                throw new BusinessException("生成邀请码失败");
            }

            log.info("生成邀请码成功，memberId={}, codeType={}, invitationCode={}",
                    memberId, codeType.getDescription(), invitationCode);

            return codeEntity;

        } catch (Exception e) {
            log.error("生成邀请码异常，memberId={}, codeType={}", memberId, codeType, e);
            throw new BusinessException("生成邀请码失败：" + e.getMessage());
        }
    }

    @Override
    @Cacheable(namespace = "INVITATION", key = "'code:' + #invitationCode", expireSeconds = 300)
    public MemberInvitationCodeDO validateInvitationCode(String invitationCode) {
        if (StrUtil.isBlank(invitationCode)) {
            return null;
        }

        // 格式验证
        if (!invitationCodeGenerator.validateCodeFormat(invitationCode)) {
            log.warn("邀请码格式无效，invitationCode={}", invitationCode);
            return null;
        }

        // 查询有效的邀请码
        MemberInvitationCodeDO codeEntity = invitationCodeMapper.selectValidCode(invitationCode);
        if (codeEntity == null) {
            log.warn("邀请码不存在或已失效，invitationCode={}", invitationCode);
            return null;
        }

        // 检查使用次数限制
        if (codeEntity.getMaxUseCount() != null &&
            codeEntity.getUsedCount() >= codeEntity.getMaxUseCount()) {
            log.warn("邀请码使用次数已达上限，invitationCode={}, usedCount={}, maxUseCount={}",
                    invitationCode, codeEntity.getUsedCount(), codeEntity.getMaxUseCount());
            return null;
        }

        return codeEntity;
    }

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    @PluginLockSafeExec(lockKey = "invitation:use:#{#invitationCode}", ttl = 30)
    public boolean useInvitationCode(String invitationCode) {
        // 验证邀请码
        MemberInvitationCodeDO codeEntity = validateInvitationCode(invitationCode);
        if (codeEntity == null) {
            return false;
        }

        try {
            // 增加使用次数
            MemberInvitationCodeDO updateEntity = new MemberInvitationCodeDO();
            updateEntity.setId(codeEntity.getId());
            updateEntity.setUsedCount(codeEntity.getUsedCount() + 1);

            // 如果达到最大使用次数，设置为失效
            if (codeEntity.getMaxUseCount() != null &&
                updateEntity.getUsedCount() >= codeEntity.getMaxUseCount()) {
                updateEntity.setStatus(InvitationCodeStatusEnum.INVALID.getCode());
            }

            int result = invitationCodeMapper.updateById(updateEntity);
            if (result > 0) {
                log.info("使用邀请码成功，invitationCode={}, usedCount={}",
                        invitationCode, updateEntity.getUsedCount());
                return true;
            }

            return false;

        } catch (Exception e) {
            log.error("使用邀请码异常，invitationCode={}", invitationCode, e);
            return false;
        }
    }

    @Override
    @Cacheable(namespace = "INVITATION", key = "'member:' + #memberId")
    public List<MemberInvitationCodeDO> getMemberInvitationCodes(Integer memberId) {
        if (memberId == null) {
            throw new BusinessException("会员ID不能为空");
        }
        return invitationCodeMapper.selectByInviterMemberId(memberId);
    }

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    @CacheEvict(namespace = "INVITATION",pattern = "*")
    public boolean disableInvitationCode(Long id) {
        return updateInvitationCodeStatus(id, InvitationCodeStatusEnum.DISABLED);
    }

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    @CacheEvict(namespace = "INVITATION",pattern = "*")
    public boolean enableInvitationCode(Long id) {
        return updateInvitationCodeStatus(id, InvitationCodeStatusEnum.VALID);
    }

    @Override
    public MemberInvitationCodeDO getInvitationCodeById(Long id) {
        if (id == null) {
            return null;
        }
        return invitationCodeMapper.selectById(id);
    }

    @Override
    @Cacheable(namespace = "INVITATION", key = "'member:' + #invitationCode", expireSeconds = 300)
    public MemberInvitationCodeDO getInvitationCodeByCode(String invitationCode) {
        if (StrUtil.isBlank(invitationCode)) {
            return null;
        }
        return invitationCodeMapper.selectByInvitationCode(invitationCode);
    }

    /**
     * 生成唯一邀请码
     *
     * @param memberId 会员ID
     * @param codeType 邀请码类型
     * @return 邀请码
     */
    private String generateUniqueCode(Integer memberId, InvitationCodeTypeEnum codeType) {
        int maxRetries = 10;
        for (int i = 0; i < maxRetries; i++) {
            String code = invitationCodeGenerator.generateCode(memberId, codeType);

            // 检查是否已存在
            MemberInvitationCodeDO existingCode = invitationCodeMapper.selectByInvitationCode(code);
            if (existingCode == null) {
                return code;
            }

            log.warn("生成的邀请码已存在，重新生成，attempt={}, code={}", i + 1, code);
        }

        throw new BusinessException("生成唯一邀请码失败，请稍后重试");
    }

    /**
     * 更新邀请码状态
     *
     * @param id 邀请码ID
     * @param status 状态
     * @return 是否更新成功
     */
    private boolean updateInvitationCodeStatus(Long id, InvitationCodeStatusEnum status) {
        if (id == null) {
            throw new BusinessException("邀请码ID不能为空");
        }

        MemberInvitationCodeDO updateEntity = new MemberInvitationCodeDO();
        updateEntity.setId(id);
        updateEntity.setStatus(status.getCode());

        int result = invitationCodeMapper.updateById(updateEntity);
        if (result > 0) {
            log.info("更新邀请码状态成功，id={}, status={}", id, status.getDescription());
            return true;
        }

        return false;
    }

}
