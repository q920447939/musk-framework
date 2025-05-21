package org.example.musk.tests.message.service;

import jakarta.annotation.Resource;
import org.example.musk.functions.message.model.vo.MessageDetailVO;
import org.example.musk.tests.TestApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 消息读取服务测试类
 *
 * @author musk-functions-message
 */
@SpringBootTest(
    classes = TestApplication.class,
    properties = {
        "spring.profiles.active=test"
    }
)
public class MessageReadServiceTest {

    @Resource
    private MessageReadService messageReadService;

    @Resource
    private MessageQueryService messageQueryService;

    /**
     * 测试标记消息为已读
     */
    @Test
    public void testMarkAsRead() {
        // 获取一条未读消息
        MessageDetailVO unreadMessage = messageQueryService.getMessageDetail(2, 1001);
        Assertions.assertNotNull(unreadMessage);

        if (!unreadMessage.getIsRead()) {
            // 标记为已读
            boolean success = messageReadService.markAsRead(unreadMessage.getMessageId(), 1001);
            Assertions.assertTrue(success);

            // 再次获取消息，验证已读状态
            MessageDetailVO readMessage = messageQueryService.getMessageDetail(unreadMessage.getMessageId(), 1001);
            Assertions.assertNotNull(readMessage);
            Assertions.assertTrue(readMessage.getIsRead());
        }
    }

    /**
     * 测试标记所有消息为已读
     */
    @Test
    public void testMarkAllAsRead() {
        // 标记所有消息为已读
        int count = messageReadService.markAllAsRead(1001, 1, 1);
        Assertions.assertTrue(count >= 0);

        // 获取未读消息数量，验证是否为0
        int unreadCount = messageQueryService.getUnreadMessageCount(1001, 1, 1);
        Assertions.assertEquals(0, unreadCount);
    }

    /**
     * 测试用户删除消息
     */
    @Test
    public void testDeleteUserMessage() {
        // 获取一条消息
        MessageDetailVO message = messageQueryService.getMessageDetail(1, 1001);
        Assertions.assertNotNull(message);

        // 用户删除消息
        boolean success = messageReadService.deleteUserMessage(message.getMessageId(), 1001);
        Assertions.assertTrue(success);
    }
}
