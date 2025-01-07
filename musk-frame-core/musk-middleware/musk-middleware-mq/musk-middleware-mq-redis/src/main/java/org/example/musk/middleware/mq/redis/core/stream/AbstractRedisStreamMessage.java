package org.example.musk.middleware.mq.redis.core.stream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.example.musk.middleware.mq.redis.core.message.AbstractRedisMessage;

/**
 * Redis Stream Message 抽象类
 *
 * @author musk
 */
public abstract class AbstractRedisStreamMessage extends AbstractRedisMessage {

    /**
     * 获得 Redis Stream Key，默认使用类名
     *
     * @return Channel
     */
    @JsonIgnore // 避免序列化
    public String getStreamKey() {
        return getClass().getSimpleName();
    }

}
