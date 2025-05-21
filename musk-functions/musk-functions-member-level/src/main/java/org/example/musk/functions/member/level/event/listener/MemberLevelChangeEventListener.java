package org.example.musk.functions.member.level.event.listener;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.functions.member.level.dao.MemberLevelDefinitionMapper;
import org.example.musk.functions.member.level.enums.MemberLevelChangeTypeEnum;
import org.example.musk.functions.member.level.event.MemberLevelChangeEvent;
import org.example.musk.functions.member.level.model.entity.MemberLevelDefinitionDO;
import org.example.musk.functions.message.enums.MessageActionTypeEnum;
import org.example.musk.functions.message.enums.MessagePriorityEnum;
import org.example.musk.functions.message.enums.MessageTargetTypeEnum;
import org.example.musk.functions.message.enums.MessageTypeEnum;
import org.example.musk.functions.message.enums.MessagePlatformTypeEnum;
import org.example.musk.functions.message.model.dto.SystemMessageCreateReqDTO;
import org.example.musk.tests.service.SystemMessageService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员等级变更事件监听器
 *
 * @author musk-functions-member-level
 */
@Component
@Slf4j
public class MemberLevelChangeEventListener {

    @Resource
    private SystemMessageService systemMessageService;

    @Resource
    private MemberLevelDefinitionMapper memberLevelDefinitionMapper;

    /**
     * 处理会员等级变更事件
     *
     * @param event 事件
     */
    @EventListener
    public void handleMemberLevelChangeEvent(MemberLevelChangeEvent event) {
        try {
            // 获取新等级信息
            MemberLevelDefinitionDO newLevel = memberLevelDefinitionMapper.selectById(event.getNewLevelId());
            if (newLevel == null) {
                log.error("会员等级不存在，levelId={}", event.getNewLevelId());
                return;
            }

            // 获取旧等级信息
            MemberLevelDefinitionDO oldLevel = null;
            if (event.getOldLevelId() != null) {
                oldLevel = memberLevelDefinitionMapper.selectById(event.getOldLevelId());
            }

            // 构建消息内容
            String title;
            String content;

            if (event.getChangeType().equals(MemberLevelChangeTypeEnum.UPGRADE.getValue())) {
                // 升级消息
                title = "恭喜您升级为" + newLevel.getLevelName();
                content = "尊敬的会员，恭喜您成功升级为" + newLevel.getLevelName() + "！\n\n"
                        + "您将享受以下专属权益：\n"
                        + "1. 专属标识\n"
                        + "2. 购物折扣\n"
                        + "3. 生日礼遇\n"
                        + "...\n\n"
                        + "点击查看详细权益说明。";
            } else if (event.getChangeType().equals(MemberLevelChangeTypeEnum.DOWNGRADE.getValue())) {
                // 降级消息
                title = "您的会员等级已变更为" + newLevel.getLevelName();
                content = "尊敬的会员，您的会员等级已变更为" + newLevel.getLevelName() + "。\n\n"
                        + "继续增加成长值可提升会员等级，获得更多权益。\n\n"
                        + "点击查看详细权益说明。";
            } else {
                // 其他变更消息
                title = "您的会员等级已更新为" + newLevel.getLevelName();
                content = "尊敬的会员，您的会员等级已更新为" + newLevel.getLevelName() + "。\n\n"
                        + "点击查看详细权益说明。";
            }

            // 发送消息
            SystemMessageCreateReqDTO messageDTO = new SystemMessageCreateReqDTO();
            messageDTO.setTenantId(event.getTenantId());
            messageDTO.setDomainId(event.getDomainId());
            messageDTO.setMessageType(MessageTypeEnum.SYSTEM_NOTICE.getType());
            messageDTO.setTitle(title);
            messageDTO.setContent(content);
            messageDTO.setPriority(MessagePriorityEnum.IMPORTANT.getPriority());
            messageDTO.setActionType(MessageActionTypeEnum.LINK.getType());
            messageDTO.setActionUrl("/member/level/detail");
            messageDTO.setPlatformType(MessagePlatformTypeEnum.ALL.getType());

            // 设置接收者为当前会员
            List<Integer> targetUserIds = new ArrayList<>();
            targetUserIds.add(event.getMemberId());

            systemMessageService.createAndSendMessage(messageDTO, MessageTargetTypeEnum.USER.getType(), targetUserIds);

            log.info("会员等级变更消息发送成功，memberId={}, oldLevelId={}, newLevelId={}",
                    event.getMemberId(), event.getOldLevelId(), event.getNewLevelId());
        } catch (Exception e) {
            log.error("处理会员等级变更事件异常", e);
        }
    }
}
