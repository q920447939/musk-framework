package org.example.musk.tests.service;

import jakarta.annotation.Resource;
import org.example.musk.functions.message.enums.MessagePlatformTypeEnum;
import org.example.musk.functions.message.enums.MessageTargetTypeEnum;
import org.example.musk.functions.message.enums.MessageTypeEnum;
import org.example.musk.functions.message.model.vo.MessageCreateReqVO;
import org.example.musk.functions.message.model.vo.MessageSendReqVO;
import org.example.musk.functions.message.model.vo.MessageTemplateSendReqVO;
import org.example.musk.tests.BaseTest;
import org.example.musk.tests.TestApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息发送服务测试类
 *
 * @author musk-functions-message
 */
@SpringBootTest(
    classes = TestApplication.class,
    properties = {
        "spring.profiles.active=test",
            "log4j-env=dev"

    }
)
public class MessageSendServiceTest extends BaseTest {

    @Resource
    private MessageSendService messageSendService;

    @Resource
    private MessageManageService messageManageService;

    /**
     * 测试发送消息给单个用户
     */
    @Test
    public void testSendToUser() {
        // 先创建并发布一条消息
        MessageCreateReqVO createReqVO = new MessageCreateReqVO();
        createReqVO.setTenantId(1);
        createReqVO.setDomainId(1);
        createReqVO.setMessageType(MessageTypeEnum.TEXT.getType());
        createReqVO.setTitle("发送给单个用户的消息");
        createReqVO.setContent("这是发送给单个用户的消息内容");
        createReqVO.setPlatformType(MessagePlatformTypeEnum.ALL.getType());

        Integer messageId = messageManageService.createMessage(createReqVO);
        messageManageService.publishMessage(messageId);

        // 发送消息给单个用户
        boolean success = messageSendService.sendToUser(messageId, 1001);
        Assertions.assertTrue(success);
    }

    /**
     * 测试发送消息给多个用户
     */
    @Test
    public void testSendToUsers() {
        // 先创建并发布一条消息
        MessageCreateReqVO createReqVO = new MessageCreateReqVO();
        createReqVO.setTenantId(1);
        createReqVO.setDomainId(1);
        createReqVO.setMessageType(MessageTypeEnum.TEXT.getType());
        createReqVO.setTitle("发送给多个用户的消息");
        createReqVO.setContent("这是发送给多个用户的消息内容");
        createReqVO.setPlatformType(MessagePlatformTypeEnum.ALL.getType());

        Integer messageId = messageManageService.createMessage(createReqVO);
        messageManageService.publishMessage(messageId);

        // 准备用户ID列表
        List<Integer> userIds = new ArrayList<>();
        userIds.add(1001);
        userIds.add(1002);

        // 发送消息给多个用户
        int successCount = messageSendService.sendToUsers(messageId, userIds);
        Assertions.assertTrue(successCount > 0);
    }

    /**
     * 测试发送消息给所有用户
     */
    @Test
    public void testSendToAllUsers() {
        // 先创建并发布一条消息
        MessageCreateReqVO createReqVO = new MessageCreateReqVO();
        createReqVO.setTenantId(1);
        createReqVO.setDomainId(1);
        createReqVO.setMessageType(MessageTypeEnum.TEXT.getType());
        createReqVO.setTitle("发送给所有用户的消息");
        createReqVO.setContent("这是发送给所有用户的消息内容");
        createReqVO.setPlatformType(MessagePlatformTypeEnum.ALL.getType());

        Integer messageId = messageManageService.createMessage(createReqVO);
        messageManageService.publishMessage(messageId);

        // 发送消息给所有用户
        boolean success = messageSendService.sendToAllUsers(messageId, 1, 1);
        Assertions.assertTrue(success);
    }

    /**
     * 测试使用消息发送请求发送消息
     */
    @Test
    public void testSendMessage() {
        // 先创建并发布一条消息
        MessageCreateReqVO createReqVO = new MessageCreateReqVO();
        createReqVO.setTenantId(1);
        createReqVO.setDomainId(1);
        createReqVO.setMessageType(MessageTypeEnum.TEXT.getType());
        createReqVO.setTitle("通过请求发送的消息");
        createReqVO.setContent("这是通过请求发送的消息内容");
        createReqVO.setPlatformType(MessagePlatformTypeEnum.ALL.getType());

        Integer messageId = messageManageService.createMessage(createReqVO);
        messageManageService.publishMessage(messageId);

        // 准备发送请求
        MessageSendReqVO sendReqVO = new MessageSendReqVO();
        sendReqVO.setMessageId(messageId);
        sendReqVO.setTargetType(MessageTargetTypeEnum.USER.getType());

        List<Integer> targetIds = new ArrayList<>();
        targetIds.add(1001);
        sendReqVO.setTargetIds(targetIds);

        // 发送消息
        boolean success = messageSendService.sendMessage(sendReqVO);
        Assertions.assertTrue(success);
    }

    /**
     * 测试使用模板发送消息
     */
    @Test
    public void testSendTemplateMessage() {
        // 准备模板发送请求
        MessageTemplateSendReqVO templateSendReqVO = new MessageTemplateSendReqVO();
        templateSendReqVO.setTemplateCode("system_welcome");

        Map<String, Object> params = new HashMap<>();
        params.put("userName", "测试用户");
        templateSendReqVO.setTemplateParams(params);

        templateSendReqVO.setTargetType(MessageTargetTypeEnum.USER.getType());

        List<Integer> targetIds = new ArrayList<>();
        targetIds.add(1001);
        templateSendReqVO.setTargetIds(targetIds);

        // 使用模板发送消息
        Integer messageId = messageSendService.sendTemplateMessage(templateSendReqVO);
        Assertions.assertNotNull(messageId);
    }

    /**
     * 测试使用模板发送消息给单个用户
     */
    @Test
    public void testSendTemplateMessageToUser() {
        // 准备模板参数
        Map<String, Object> params = new HashMap<>();
        params.put("userName", "测试用户");

        // 使用模板发送消息给单个用户
        Integer messageId = messageSendService.sendTemplateMessageToUser("system_welcome", params, 1001);
        Assertions.assertNotNull(messageId);
    }

    /**
     * 测试处理待发送的消息
     */
    @Test
    public void testProcessPendingMessages() {
        // 处理待发送的消息
        messageSendService.processPendingMessages();
    }
}
