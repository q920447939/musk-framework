package org.example.musk.functions.message.service;

import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.functions.message.BaseTest;
import org.example.musk.functions.message.enums.MessagePlatformTypeEnum;
import org.example.musk.functions.message.enums.MessageStatusEnum;
import org.example.musk.functions.message.enums.MessageTypeEnum;
import org.example.musk.functions.message.model.entity.SystemMessageDO;
import org.example.musk.functions.message.model.vo.MessageCreateReqVO;
import org.example.musk.functions.message.model.vo.MessageRespVO;
import org.example.musk.functions.message.model.vo.MessageUpdateReqVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.annotation.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息管理服务测试类
 *
 * @author musk-functions-message
 */
@SpringBootTest(
    classes = org.example.musk.functions.message.TestApplication.class,
    properties = {
        "spring.profiles.active=test",
            "log4j-env=dev"
    }
)
@Slf4j
public class MessageManageServiceTest extends BaseTest {

    @Resource
    private MessageManageService messageManageService;

    /**
     * 测试创建消息
     */
    @Test
    public void testCreateMessage() {
        // 创建消息请求
        MessageCreateReqVO createReqVO = new MessageCreateReqVO();
        createReqVO.setTenantId(1);
        createReqVO.setDomainId(1);
        createReqVO.setMessageType(MessageTypeEnum.TEXT.getType());
        createReqVO.setTitle("测试消息");
        createReqVO.setContent("这是一条测试消息内容");
        createReqVO.setPriority(0);
        createReqVO.setIsForced(false);
        createReqVO.setActionType(0);
        createReqVO.setPlatformType(MessagePlatformTypeEnum.ALL.getType());

        // 创建消息
        Integer messageId = messageManageService.createMessage(createReqVO);
        Assertions.assertNotNull(messageId);

        // 获取消息
        SystemMessageDO message = messageManageService.getMessage(messageId);
        Assertions.assertNotNull(message);
        Assertions.assertEquals(createReqVO.getTitle(), message.getTitle());
        Assertions.assertEquals(createReqVO.getContent(), message.getContent());
        Assertions.assertEquals(MessageStatusEnum.DRAFT.getStatus(), message.getStatus());
    }

    /**
     * 测试更新消息
     */
    @Test
    public void testUpdateMessage() {
        // 先创建一条消息
        MessageCreateReqVO createReqVO = new MessageCreateReqVO();
        createReqVO.setTenantId(1);
        createReqVO.setDomainId(1);
        createReqVO.setMessageType(MessageTypeEnum.TEXT.getType());
        createReqVO.setTitle("原始消息");
        createReqVO.setContent("这是原始消息内容");
        createReqVO.setPlatformType(MessagePlatformTypeEnum.ALL.getType());

        Integer messageId = messageManageService.createMessage(createReqVO);

        // 更新消息
        MessageUpdateReqVO updateReqVO = new MessageUpdateReqVO();
        updateReqVO.setId(messageId);
        updateReqVO.setTitle("更新后的消息");
        updateReqVO.setContent("这是更新后的消息内容");

        boolean success = messageManageService.updateMessage(updateReqVO);
        Assertions.assertTrue(success);

        // 获取更新后的消息
        SystemMessageDO message = messageManageService.getMessage(messageId);
        Assertions.assertNotNull(message);
        Assertions.assertEquals(updateReqVO.getTitle(), message.getTitle());
        Assertions.assertEquals(updateReqVO.getContent(), message.getContent());
    }

    /**
     * 测试发布消息
     */
    @Test
    public void testPublishMessage() {
        // 先创建一条消息
        MessageCreateReqVO createReqVO = new MessageCreateReqVO();
        createReqVO.setTenantId(1);
        createReqVO.setDomainId(1);
        createReqVO.setMessageType(MessageTypeEnum.TEXT.getType());
        createReqVO.setTitle("待发布消息");
        createReqVO.setContent("这是待发布消息内容");
        createReqVO.setPlatformType(MessagePlatformTypeEnum.ALL.getType());

        Integer messageId = messageManageService.createMessage(createReqVO);

        // 发布消息
        boolean success = messageManageService.publishMessage(messageId);
        Assertions.assertTrue(success);

        // 获取发布后的消息
        SystemMessageDO message = messageManageService.getMessage(messageId);
        Assertions.assertNotNull(message);
        Assertions.assertEquals(MessageStatusEnum.PUBLISHED.getStatus(), message.getStatus());
        Assertions.assertNotNull(message.getStartTime());
    }

    /**
     * 测试取消发布消息
     */
    @Test
    public void testUnpublishMessage() {
        // 先创建并发布一条消息
        MessageCreateReqVO createReqVO = new MessageCreateReqVO();
        createReqVO.setTenantId(1);
        createReqVO.setDomainId(1);
        createReqVO.setMessageType(MessageTypeEnum.TEXT.getType());
        createReqVO.setTitle("已发布消息");
        createReqVO.setContent("这是已发布消息内容");
        createReqVO.setPlatformType(MessagePlatformTypeEnum.ALL.getType());

        Integer messageId = messageManageService.createMessage(createReqVO);
        messageManageService.publishMessage(messageId);

        // 取消发布消息
        boolean success = messageManageService.unpublishMessage(messageId);
        Assertions.assertTrue(success);

        // 获取取消发布后的消息
        SystemMessageDO message = messageManageService.getMessage(messageId);
        Assertions.assertNotNull(message);
        Assertions.assertEquals(MessageStatusEnum.DRAFT.getStatus(), message.getStatus());
    }

    /**
     * 测试删除消息
     */
    @Test
    public void testDeleteMessage() {
        // 先创建一条消息
        MessageCreateReqVO createReqVO = new MessageCreateReqVO();
        createReqVO.setTenantId(1);
        createReqVO.setDomainId(1);
        createReqVO.setMessageType(MessageTypeEnum.TEXT.getType());
        createReqVO.setTitle("待删除消息");
        createReqVO.setContent("这是待删除消息内容");
        createReqVO.setPlatformType(MessagePlatformTypeEnum.ALL.getType());

        Integer messageId = messageManageService.createMessage(createReqVO);

        // 删除消息
        boolean success = messageManageService.deleteMessage(messageId);
        Assertions.assertTrue(success);

        // 获取删除后的消息
        SystemMessageDO message = messageManageService.getMessage(messageId);
        Assertions.assertNull(message);
    }

    /**
     * 测试获取消息列表
     */
    @Test
    public void testGetMessageList() {
        // 获取消息列表
        List<SystemMessageDO> messages = messageManageService.getMessageList(1, 1, null, null);
        Assertions.assertNotNull(messages);
        Assertions.assertFalse(messages.isEmpty());
    }

    /**
     * 测试分页获取消息列表
     */
    @Test
    public void testGetMessagePage() {
        // 分页获取消息列表
        PageResult<MessageRespVO> pageResult = messageManageService.getMessagePage(1, 1, null, null, 1, 10);
        Assertions.assertNotNull(pageResult);
        Assertions.assertFalse(pageResult.getList().isEmpty());
    }

    /**
     * 测试检查消息状态
     */
    @Test
    public void testCheckMessageStatus() {
        // 先创建并发布一条带结束时间的消息
        MessageCreateReqVO createReqVO = new MessageCreateReqVO();
        createReqVO.setTenantId(1);
        createReqVO.setDomainId(1);
        createReqVO.setMessageType(MessageTypeEnum.TEXT.getType());
        createReqVO.setTitle("带结束时间的消息");
        createReqVO.setContent("这是带结束时间的消息内容");
        createReqVO.setPlatformType(MessagePlatformTypeEnum.ALL.getType());
        createReqVO.setStartTime(LocalDateTime.now().minusDays(2));
        createReqVO.setEndTime(LocalDateTime.now().minusDays(1));

        Integer messageId = messageManageService.createMessage(createReqVO);
        messageManageService.publishMessage(messageId);

        // 检查消息状态
        messageManageService.checkMessageStatus();

        // 获取消息
        SystemMessageDO message = messageManageService.getMessage(messageId);
        Assertions.assertNotNull(message);
        Assertions.assertEquals(MessageStatusEnum.EXPIRED.getStatus(), message.getStatus());
    }
}
