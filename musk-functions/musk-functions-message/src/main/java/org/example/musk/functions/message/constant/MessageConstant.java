package org.example.musk.functions.message.constant;

/**
 * 消息模块常量
 *
 * @author musk-functions-message
 */
public interface MessageConstant {

    /**
     * 数据库名称
     */
    String DB_NAME = "musk_message";

    /**
     * 消息缓存过期时间（秒）
     */
    int MESSAGE_CACHE_EXPIRE_SECONDS = 3600;

    /**
     * 消息模板缓存过期时间（秒）
     */
    int MESSAGE_TEMPLATE_CACHE_EXPIRE_SECONDS = 7200;

    /**
     * 消息统计缓存过期时间（秒）
     */
    int MESSAGE_STATS_CACHE_EXPIRE_SECONDS = 300;

    /**
     * 消息状态：草稿
     */
    int STATUS_DRAFT = 0;

    /**
     * 消息状态：已发布
     */
    int STATUS_PUBLISHED = 1;

    /**
     * 消息状态：已过期
     */
    int STATUS_EXPIRED = 2;

    /**
     * 消息发送状态：待发送
     */
    int SEND_STATUS_PENDING = 0;

    /**
     * 消息发送状态：发送中
     */
    int SEND_STATUS_SENDING = 1;

    /**
     * 消息发送状态：发送成功
     */
    int SEND_STATUS_SUCCESS = 2;

    /**
     * 消息发送状态：发送失败
     */
    int SEND_STATUS_FAILED = 3;

    /**
     * 消息目标类型：单个用户
     */
    int TARGET_TYPE_USER = 1;

    /**
     * 消息目标类型：用户组
     */
    int TARGET_TYPE_USER_GROUP = 2;

    /**
     * 消息目标类型：全部用户
     */
    int TARGET_TYPE_ALL_USERS = 3;

    /**
     * 消息操作类型：无操作
     */
    int ACTION_TYPE_NONE = 0;

    /**
     * 消息操作类型：跳转链接
     */
    int ACTION_TYPE_LINK = 1;

    /**
     * 消息操作类型：打开应用
     */
    int ACTION_TYPE_APP = 2;

    /**
     * 消息操作类型：下载更新
     */
    int ACTION_TYPE_DOWNLOAD = 3;

    /**
     * Redis消息队列Key前缀
     */
    String REDIS_STREAM_KEY_PREFIX = "musk:message:stream:";

    /**
     * Redis消息队列消费者组名称
     */
    String REDIS_CONSUMER_GROUP = "message-consumer-group";
}
