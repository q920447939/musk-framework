package org.example.musk.functions.member.level.event.listener;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.dynamic.datasource.tx.DsPropagation;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.constant.db.DBConstant;
import org.example.musk.functions.member.level.dao.MemberGrowthValueRecordMapper;
import org.example.musk.functions.member.level.event.MemberGrowthValueChangeEvent;
import org.example.musk.functions.member.level.model.entity.MemberGrowthValueRecordDO;
import org.example.musk.functions.member.level.service.MemberLevelService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 成长值变更事件监听器
 *
 * @author musk-functions-member-level
 */
@Component
@Slf4j
@DS(DBConstant.MEMBER_LEVEL)
public class MemberGrowthValueChangeEventListener {

    @Resource
    private MemberGrowthValueRecordMapper memberGrowthValueRecordMapper;

    @Resource
    private MemberLevelService memberLevelService;

    /**
     * 处理成长值变更事件
     * 1. 记录成长值变更历史
     * 2. 计算会员等级
     *
     * @param event 事件
     */
    @EventListener
    @Async
    @DSTransactional(propagation = DsPropagation.REQUIRED, rollbackFor = Exception.class)
    public void handleMemberGrowthValueChangeEvent(MemberGrowthValueChangeEvent event) {
        try {
            log.info("处理成长值变更事件，memberId={}, changeType={}, changeValue={}",
                    event.getMemberId(), event.getChangeType(), event.getChangeValue());

            // 1. 记录成长值变更历史
            recordGrowthValueChange(event);

            // 2. 计算会员等级
            calculateMemberLevel(event);

            log.info("成长值变更事件处理完成，memberId={}", event.getMemberId());
        } catch (Exception e) {
            log.error("处理成长值变更事件异常", e);
        }
    }

    /**
     * 记录成长值变更历史
     *
     * @param event 事件
     */
    private void recordGrowthValueChange(MemberGrowthValueChangeEvent event) {
        MemberGrowthValueRecordDO record = new MemberGrowthValueRecordDO();
        record.setTenantId(event.getTenantId());
        record.setDomainId(event.getDomainId());
        record.setMemberId(event.getMemberId());
        record.setChangeType(event.getChangeType());
        record.setChangeValue(event.getChangeValue());
        record.setBeforeValue(event.getBeforeValue());
        record.setAfterValue(event.getAfterValue());
        record.setSourceType(event.getSourceType());
        record.setSourceId(event.getSourceId());
        record.setDescription(event.getDescription());
        record.setOperator(event.getOperator());
        memberGrowthValueRecordMapper.insert(record);

        log.info("成长值变更历史记录成功，memberId={}, changeType={}, changeValue={}",
                event.getMemberId(), event.getChangeType(), event.getChangeValue());
    }

    /**
     * 计算会员等级
     *
     * @param event 事件
     */
    private void calculateMemberLevel(MemberGrowthValueChangeEvent event) {
        Integer newLevelId = memberLevelService.calculateMemberLevel(event.getMemberId());
        log.info("会员等级计算完成，memberId={}, newLevelId={}", event.getMemberId(), newLevelId);
    }
}
