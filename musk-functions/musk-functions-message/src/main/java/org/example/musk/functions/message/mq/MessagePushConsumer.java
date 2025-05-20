package org.example.musk.functions.message.mq;

import lombok.extern.slf4j.Slf4j;
import org.example.musk.functions.message.constant.MessageConstant;
import org.example.musk.functions.message.service.MessagePushService;
import org.example.musk.middleware.mq.redis.core.stream.AbstractRedisStreamMessageListener;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * 消息推送消费者
 *
 * @author musk-functions-message
 */
@Component
@Slf4j
public class MessagePushConsumer extends AbstractRedisStreamMessageListener<MessagePushMessage> {

    @Resource
    private MessagePushService messagePushService;

    /**
     * 构造函数
     */
    public MessagePushConsumer() {
        super(MessageConstant.REDIS_CONSUMER_GROUP);
    }

    @Override
    public void onMessage(MessagePushMessage message) {
        log.info("[onMessage][收到消息推送消息：{}]", message);
        try {
            // 处理消息推送
            messagePushService.processMessagePush(
                    message.getMessageId(), message.getUserId(), message.getSendRecordId());
        } catch (Exception e) {
            log.error("[onMessage][处理消息推送异常，消息：{}]", message, e);
        }
    }
}
