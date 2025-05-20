package org.example.musk.functions.message;

import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.functions.message.enums.MessagePlatformTypeEnum;
import org.example.musk.functions.message.enums.MessageTargetTypeEnum;
import org.example.musk.functions.message.enums.MessageTypeEnum;
import org.example.musk.functions.message.model.vo.MessageCreateReqVO;
import org.example.musk.functions.message.model.vo.MessageDetailVO;
import org.example.musk.functions.message.model.vo.MessageSendReqVO;
import org.example.musk.functions.message.model.vo.MessageTemplateSendReqVO;
import org.example.musk.functions.message.model.vo.UserMessageVO;
import org.example.musk.functions.message.service.MessageManageService;
import org.example.musk.functions.message.service.MessageQueryService;
import org.example.musk.functions.message.service.MessageReadService;
import org.example.musk.functions.message.service.MessageSendService;
import org.example.musk.functions.message.service.MessageTemplateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息模块集成测试类
 *
 * @author musk-functions-message
 */
@SpringBootTest(
    classes = TestApplication.class,
    properties = {
        "spring.profiles.active=test"
    }
)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MessageModuleIntegrationTest extends BaseTest{

    @Resource
    private MessageManageService messageManageService;

    @Resource
    private MessageTemplateService messageTemplateService;

    @Resource
    private MessageSendService messageSendService;

    @Resource
    private MessageQueryService messageQueryService;

    @Resource
    private MessageReadService messageReadService;

    private static Integer testMessageId;
    private static Integer testTemplateMessageId;
    private static final Integer TEST_USER_ID = 1001;
    private static final Integer TEST_TENANT_ID = 1;
    private static final Integer TEST_DOMAIN_ID = 1;



    /**
     * 测试创建和发布消息
     */
    @Test
    @Order(1)
    public void testCreateAndPublishMessage() {
        // 创建消息
        MessageCreateReqVO createReqVO = new MessageCreateReqVO();
        createReqVO.setTenantId(TEST_TENANT_ID);
        createReqVO.setDomainId(TEST_DOMAIN_ID);
        createReqVO.setMessageType(MessageTypeEnum.TEXT.getType());
        createReqVO.setTitle("集成测试消息");
        createReqVO.setContent("这是一条集成测试消息内容");
        createReqVO.setPlatformType(MessagePlatformTypeEnum.ALL.getType());

        testMessageId = messageManageService.createMessage(createReqVO);
        Assertions.assertNotNull(testMessageId);

        // 发布消息
        boolean success = messageManageService.publishMessage(testMessageId);
        Assertions.assertTrue(success);
    }

    /**
     * 测试发送消息给用户
     */
    @Test
    @Order(2)
    public void testSendMessageToUser() {
        // 准备发送请求
        MessageSendReqVO sendReqVO = new MessageSendReqVO();
        sendReqVO.setMessageId(testMessageId);
        sendReqVO.setTargetType(MessageTargetTypeEnum.USER.getType());

        List<Integer> targetIds = new ArrayList<>();
        targetIds.add(TEST_USER_ID);
        sendReqVO.setTargetIds(targetIds);

        // 发送消息
        boolean success = messageSendService.sendMessage(sendReqVO);
        Assertions.assertTrue(success);
    }

    /**
     * 测试使用模板发送消息
     */
    @Test
    @Order(3)
    public void testSendTemplateMessage() {
        // 准备模板发送请求
        MessageTemplateSendReqVO templateSendReqVO = new MessageTemplateSendReqVO();
        templateSendReqVO.setTemplateCode("system_welcome");

        Map<String, Object> params = new HashMap<>();
        params.put("userName", "集成测试用户");
        templateSendReqVO.setTemplateParams(params);

        templateSendReqVO.setTargetType(MessageTargetTypeEnum.USER.getType());

        List<Integer> targetIds = new ArrayList<>();
        targetIds.add(TEST_USER_ID);
        templateSendReqVO.setTargetIds(targetIds);

        // 使用模板发送消息
        testTemplateMessageId = messageSendService.sendTemplateMessage(templateSendReqVO);
        Assertions.assertNotNull(testTemplateMessageId);
    }

    /**
     * 测试查询用户消息
     */
    @Test
    @Order(4)
    public void testQueryUserMessages() {
        // 获取用户未读消息数量
        int unreadCount = messageQueryService.getUnreadMessageCount(TEST_USER_ID, TEST_TENANT_ID, TEST_DOMAIN_ID);
        Assertions.assertTrue(unreadCount > 0);

        // 获取用户消息列表
        PageResult<UserMessageVO> messages = messageQueryService.getUserMessages(
                TEST_USER_ID, TEST_TENANT_ID, TEST_DOMAIN_ID, 1, 10, null);
        Assertions.assertNotNull(messages);
        Assertions.assertFalse(messages.getList().isEmpty());

        // 获取消息详情
        MessageDetailVO detail = messageQueryService.getMessageDetail(testMessageId, TEST_USER_ID);
        Assertions.assertNotNull(detail);
        Assertions.assertEquals(testMessageId, detail.getMessageId());
        Assertions.assertFalse(detail.getIsRead());
    }

    /**
     * 测试标记消息为已读
     */
    @Test
    @Order(5)
    public void testMarkMessageAsRead() {
        // 标记消息为已读
        boolean success = messageReadService.markAsRead(testMessageId, TEST_USER_ID);
        Assertions.assertTrue(success);

        // 获取消息详情，验证已读状态
        MessageDetailVO detail = messageQueryService.getMessageDetail(testMessageId, TEST_USER_ID);
        Assertions.assertNotNull(detail);
        Assertions.assertTrue(detail.getIsRead());
    }

    /**
     * 测试标记所有消息为已读
     */
    @Test
    @Order(6)
    public void testMarkAllMessagesAsRead() {
        // 标记所有消息为已读
        int count = messageReadService.markAllAsRead(TEST_USER_ID, TEST_TENANT_ID, TEST_DOMAIN_ID);
        Assertions.assertTrue(count >= 0);

        // 获取未读消息数量，验证是否为0
        int unreadCount = messageQueryService.getUnreadMessageCount(TEST_USER_ID, TEST_TENANT_ID, TEST_DOMAIN_ID);
        Assertions.assertEquals(0, unreadCount);
    }

    /**
     * 测试用户删除消息
     */
    @Test
    @Order(7)
    public void testDeleteUserMessage() {
        // 用户删除消息
        boolean success = messageReadService.deleteUserMessage(testMessageId, TEST_USER_ID);
        Assertions.assertTrue(success);
    }

    /**
     * 测试完整流程
     */
    @Test
    @Order(8)
    public void testFullProcess() {
        // 1. 创建消息
        MessageCreateReqVO createReqVO = new MessageCreateReqVO();
        createReqVO.setTenantId(TEST_TENANT_ID);
        createReqVO.setDomainId(TEST_DOMAIN_ID);
        createReqVO.setMessageType(MessageTypeEnum.TEXT.getType());
        createReqVO.setTitle("完整流程测试消息");
        createReqVO.setContent("这是一条完整流程测试消息内容");
        createReqVO.setPlatformType(MessagePlatformTypeEnum.ALL.getType());

        Integer messageId = messageManageService.createMessage(createReqVO);
        Assertions.assertNotNull(messageId);

        // 2. 发布消息
        boolean publishSuccess = messageManageService.publishMessage(messageId);
        Assertions.assertTrue(publishSuccess);

        // 3. 发送消息给用户
        boolean sendSuccess = messageSendService.sendToUser(messageId, TEST_USER_ID);
        Assertions.assertTrue(sendSuccess);

        // 4. 查询用户消息
        MessageDetailVO detail = messageQueryService.getMessageDetail(messageId, TEST_USER_ID);
        Assertions.assertNotNull(detail);
        Assertions.assertEquals(messageId, detail.getMessageId());
        Assertions.assertFalse(detail.getIsRead());

        // 5. 标记消息为已读
        boolean markSuccess = messageReadService.markAsRead(messageId, TEST_USER_ID);
        Assertions.assertTrue(markSuccess);

        // 6. 验证已读状态
        detail = messageQueryService.getMessageDetail(messageId, TEST_USER_ID);
        Assertions.assertNotNull(detail);
        Assertions.assertTrue(detail.getIsRead());

        // 7. 用户删除消息
        boolean deleteSuccess = messageReadService.deleteUserMessage(messageId, TEST_USER_ID);
        Assertions.assertTrue(deleteSuccess);
    }
}
