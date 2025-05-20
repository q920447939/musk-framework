package org.example.musk.functions.message.mq;

import lombok.Data;
import lombok.experimental.Accessors;
import org.example.musk.functions.message.constant.MessageConstant;
import org.example.musk.middleware.mq.redis.core.stream.AbstractRedisStreamMessage;

/**
 * 消息推送消息
 *
 * @author musk-functions-message
 */
@Data
@Accessors(chain = true)
public class MessagePushMessage extends AbstractRedisStreamMessage {

    /**
     * 消息ID
     */
    private Integer messageId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 发送记录ID
     */
    private Integer sendRecordId;

    @Override
    public String getStreamKey() {
        return MessageConstant.REDIS_STREAM_KEY_PREFIX + "push";
    }
}
