package org.example.musk.functions.message.service;

import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.functions.message.model.vo.MessageDetailVO;
import org.example.musk.functions.message.model.vo.UserMessageVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.annotation.Resource;

/**
 * 消息查询服务测试类
 *
 * @author musk-functions-message
 */
@SpringBootTest(
    classes = org.example.musk.functions.message.TestApplication.class,
    properties = {
        "spring.profiles.active=test"
    }
)
public class MessageQueryServiceTest {

    @Resource
    private MessageQueryService messageQueryService;

    /**
     * 测试获取用户未读消息数量
     */
    @Test
    public void testGetUnreadMessageCount() {
        // 获取用户未读消息数量
        int count = messageQueryService.getUnreadMessageCount(1001, 1, 1);
        Assertions.assertTrue(count >= 0);
    }

    /**
     * 测试获取用户消息列表
     */
    @Test
    public void testGetUserMessages() {
        // 获取用户所有消息
        PageResult<UserMessageVO> allMessages = messageQueryService.getUserMessages(1001, 1, 1, 1, 10, null);
        Assertions.assertNotNull(allMessages);
        Assertions.assertFalse(allMessages.getList().isEmpty());

        // 获取用户已读消息
        PageResult<UserMessageVO> readMessages = messageQueryService.getUserMessages(1001, 1, 1, 1, 10, true);
        Assertions.assertNotNull(readMessages);

        // 获取用户未读消息
        PageResult<UserMessageVO> unreadMessages = messageQueryService.getUserMessages(1001, 1, 1, 1, 10, false);
        Assertions.assertNotNull(unreadMessages);
    }

    /**
     * 测试获取消息详情
     */
    @Test
    public void testGetMessageDetail() {
        // 获取消息详情
        MessageDetailVO detail = messageQueryService.getMessageDetail(1, 1001);
        Assertions.assertNotNull(detail);
        Assertions.assertEquals(1, detail.getMessageId());
        Assertions.assertTrue(detail.getIsRead());
    }
}
