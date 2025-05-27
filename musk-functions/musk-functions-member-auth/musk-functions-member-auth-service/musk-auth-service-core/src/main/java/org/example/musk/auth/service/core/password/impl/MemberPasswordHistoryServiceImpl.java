package org.example.musk.auth.service.core.password.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.dao.password.MemberPasswordHistoryMapper;
import org.example.musk.auth.entity.password.MemberPasswordHistoryDO;
import org.example.musk.auth.service.core.password.MemberPasswordHistoryService;
import org.example.musk.plugin.service.dynamic.source.anno.PluginDynamicSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 会员密码历史记录服务实现
 *
 * @author musk
 */
@Service
@Slf4j
@PluginDynamicSource(group = "auth", ds = "member")
public class MemberPasswordHistoryServiceImpl extends ServiceImpl<MemberPasswordHistoryMapper, MemberPasswordHistoryDO>
        implements MemberPasswordHistoryService {

    @Resource
    private MemberPasswordHistoryMapper passwordHistoryMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long savePasswordHistory(Integer memberId, String passwordHash, String remark) {
        log.info("[密码历史] 保存密码历史记录，memberId={}", memberId);

        try {
            MemberPasswordHistoryDO historyDO = MemberPasswordHistoryDO.builder()
                    .memberId(memberId)
                    .passwordHash(passwordHash)
                    .remark(remark)
                    .build();

            int result = passwordHistoryMapper.insert(historyDO);
            if (result > 0) {
                log.info("[密码历史] 保存成功，memberId={}, historyId={}", memberId, historyDO.getId());
                return historyDO.getId();
            } else {
                log.warn("[密码历史] 保存失败，memberId={}", memberId);
                return null;
            }
        } catch (Exception e) {
            log.error("[密码历史] 保存异常，memberId={}", memberId, e);
            throw e;
        }
    }

    @Override
    public List<MemberPasswordHistoryDO> getPasswordHistory(Integer memberId, Integer limit) {
        log.debug("[密码历史] 获取密码历史记录，memberId={}, limit={}", memberId, limit);

        LambdaQueryWrapper<MemberPasswordHistoryDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MemberPasswordHistoryDO::getMemberId, memberId)
                .orderByDesc(MemberPasswordHistoryDO::getCreateTime)
                .last("LIMIT " + limit);

        return passwordHistoryMapper.selectList(queryWrapper);
    }

    @Override
    public boolean isPasswordInHistory(Integer memberId, String passwordHash, Integer checkCount) {
        log.debug("[密码历史] 检查密码是否在历史记录中，memberId={}, checkCount={}", memberId, checkCount);

        LambdaQueryWrapper<MemberPasswordHistoryDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MemberPasswordHistoryDO::getMemberId, memberId)
                .eq(MemberPasswordHistoryDO::getPasswordHash, passwordHash)
                .orderByDesc(MemberPasswordHistoryDO::getCreateTime)
                .last("LIMIT " + checkCount);

        List<MemberPasswordHistoryDO> historyList = passwordHistoryMapper.selectList(queryWrapper);
        boolean exists = !historyList.isEmpty();

        log.debug("[密码历史] 密码历史检查结果，memberId={}, exists={}", memberId, exists);
        return exists;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cleanExpiredHistory(Integer memberId, Integer keepCount) {
        log.info("[密码历史] 清理过期密码历史记录，memberId={}, keepCount={}", memberId, keepCount);

        try {
            // 查询需要保留的记录ID
            LambdaQueryWrapper<MemberPasswordHistoryDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(MemberPasswordHistoryDO::getMemberId, memberId)
                    .orderByDesc(MemberPasswordHistoryDO::getCreateTime)
                    .last("LIMIT " + keepCount);

            List<MemberPasswordHistoryDO> keepList = passwordHistoryMapper.selectList(queryWrapper);

            if (keepList.isEmpty()) {
                log.debug("[密码历史] 没有需要保留的记录，memberId={}", memberId);
                return 0;
            }

            // 获取最早保留记录的时间
            LocalDateTime earliestKeepTime = keepList.getLast().getCreateTime();

            // 删除早于保留时间的记录
            LambdaUpdateWrapper<MemberPasswordHistoryDO> deleteWrapper = new LambdaUpdateWrapper<>();
            deleteWrapper.eq(MemberPasswordHistoryDO::getMemberId, memberId)
                    .lt(MemberPasswordHistoryDO::getCreateTime, earliestKeepTime);

            int deletedCount = passwordHistoryMapper.delete(deleteWrapper);
            log.info("[密码历史] 清理完成，memberId={}, deletedCount={}", memberId, deletedCount);

            return deletedCount;
        } catch (Exception e) {
            log.error("[密码历史] 清理异常，memberId={}", memberId, e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAllHistory(Integer memberId) {
        log.info("[密码历史] 删除所有密码历史记录，memberId={}", memberId);

        try {
            LambdaUpdateWrapper<MemberPasswordHistoryDO> deleteWrapper = new LambdaUpdateWrapper<>();
            deleteWrapper.eq(MemberPasswordHistoryDO::getMemberId, memberId);

            int deletedCount = passwordHistoryMapper.delete(deleteWrapper);
            log.info("[密码历史] 删除完成，memberId={}, deletedCount={}", memberId, deletedCount);

            return deletedCount;
        } catch (Exception e) {
            log.error("[密码历史] 删除异常，memberId={}", memberId, e);
            throw e;
        }
    }
}
