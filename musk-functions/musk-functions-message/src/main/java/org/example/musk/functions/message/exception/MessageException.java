package org.example.musk.functions.message.exception;

import org.example.musk.common.exception.BusinessException;

/**
 * 消息模块异常
 *
 * @author musk-functions-message
 */
public class MessageException extends BusinessException {

    /**
     * 消息不存在
     */
    public static final String MESSAGE_NOT_EXISTS = "message.not.exists";

    /**
     * 消息模板不存在
     */
    public static final String MESSAGE_TEMPLATE_NOT_EXISTS = "message.template.not.exists";

    /**
     * 消息模板编码已存在
     */
    public static final String MESSAGE_TEMPLATE_CODE_EXISTS = "message.template.code.exists";

    /**
     * 消息状态不允许操作
     */
    public static final String MESSAGE_STATUS_NOT_ALLOWED = "message.status.not.allowed";

    /**
     * 消息发送失败
     */
    public static final String MESSAGE_SEND_FAILED = "message.send.failed";

    /**
     * 消息推送失败
     */
    public static final String MESSAGE_PUSH_FAILED = "message.push.failed";

    /**
     * 消息模板渲染失败
     */
    public static final String MESSAGE_TEMPLATE_RENDER_FAILED = "message.template.render.failed";

    /**
     * 构造函数
     *
     * @param code 错误码
     */
    public MessageException(String code) {
        super(code);
    }

    /**
     * 构造函数
     *
     * @param code 错误码
     * @param message 错误信息
     */
    public MessageException(String code, String message) {
        super(code, message);
    }

}
