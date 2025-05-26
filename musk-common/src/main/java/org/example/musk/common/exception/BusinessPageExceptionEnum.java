package org.example.musk.common.exception;/**
 * @Project:
 * @Author:
 * @Date: 2021年09月14日
 */

import cn.hutool.core.lang.Assert;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * ClassName: BusinessPageExceptionEnum
 *
 * @author
 * @Description:  页面业务异常，可在页面上展示的异常
 * 开始编码 4100000
 * 结束编码 4200000
 * @date 2021年09月14日
 */
@Getter
public enum BusinessPageExceptionEnum implements IBaseErrorInfo{

    REGISTER_PARAMS_ERROR("4210002", "注册参数异常"),
    REGISTER_USERNAME_EXISTS("4210003", "账号已存在"),
    REGISTER_FAIL("4210004", "注册失败"),
    REGISTER_INVITE_MEMBER_SIMPLE_ID_NOT_EXISTS("4210005", "邀请人不存在"),
    REGISTER_ChANNEL_NOT_EXISTS("4210006", "渠道不存在"),
    GET_VERIFI_CATIONCODE_MISS("4210007", "获取验证码缺少必要参数"),
    VERIFI_CATIONCODE_ERROR("4210008", "验证码错误"),
    TENANT_NOT_EXISTS("4210009", "T_N_E"),
    LOCK_MEMBER_FAIL("42100010", "网络异常,请稍后重试"),
    BACK_LOCK_KEY_FAIL("42100011", "获取锁失败"),
    FACADE_GET_LOCK_FAIL("42100012", "操作太快了,请稍后重试"),


    GENERATOR_VERIFICATION_CODE_IS_NULL("41200013", "未生成验证码"),
    API_INVALID_USERNAME_CODE("41200014", "登录失败, 用户名或密码错误"),

    // 认证相关异常
    PARAMS_ERROR ("41200014", "参数错误"),
    AUTH_PRE_PROCESS_FAILED("41200015", "认证前置处理失败"),
    AUTH_TYPE_NOT_SUPPORTED("41200016", "不支持的认证类型"),
    VERIFICATION_CODE_ERROR("41200017", "验证码错误或已过期"),
    VERIFICATION_CODE_SEND_FAILED("41200018", "验证码发送失败"),
    MEMBER_NOT_EXISTS("41200019", "会员不存在"),
    MEMBER_STATUS_ABNORMAL("41200020", "会员状态异常"),
    AUTH_CHANNEL_ALREADY_EXISTS("41200021", "认证渠道已存在"),
    CANNOT_UNBIND_LAST_AUTH_CHANNEL("41200022", "不能解绑最后一个认证渠道"),
    THIRD_PARTY_AUTH_FAILED("41200023", "第三方认证失败"),
    THIRD_PARTY_CONFIG_ERROR("41200024", "第三方配置错误"),

    // 注册相关异常
    CAPTCHA_VERIFY_FAILED("41200025", "图形验证码验证失败"),
    PASSWORD_NOT_MATCH("41200026", "两次输入的密码不一致"),
    USERNAME_FORMAT_ERROR("41200027", "用户名格式不正确"),
    PASSWORD_WEAK("41200028", "密码强度不够"),
    REGISTER_FAILED("41200029", "注册失败"),

    USER_IS_FORBIDDEN_ERROR("4210100", "用户已被禁用"),
    USER_IS_NOT_EXISTS("4210101", "用户不存在"),


    /**
     * 上传文件部分
     */
    UPLOAD_FILE_REQUEST_IS_EMPTY("4210300", "文件不能为空"),
    UPLOAD_FILE_FAIL("4210301", "上传文件失败"),
    UPLOAD_FILE_SIZE_OR_FILE_NUMBER_FAIL("4210302", "上传文件个数或上传文件大小超过限制"),
    UPLOAD_FILE_SIZE_OR_FILE_NUMBER_BY_MEMBER_FAIL("4210304", "上传文件个数或上传文件大小超过限制"),
    /**
     *系统配置
     */
    APP_PARAM_CONFIG_IS_EMPTY("4210701", "获取配置失败"),
    /**
     * 通用部分 开始
     */
    /**
     * 通用的 bool 的返回为false
     */
    COMMON_RESULT_BOOL_IS_FALSE ("4299900", "操作太快了,请稍后重试"),



    END("4200000", "");
    private String exCode;
    private String exDesc;

    BusinessPageExceptionEnum(String exCode, String exDesc) {
        this.exCode = exCode;
        this.exDesc = exDesc;
    }


    @Override
    public String getResultCode() {
        return this.exCode;
    }

    @Override
    public String getResultMsg() {
        return this.getExDesc();
    }

    public static BusinessPageExceptionEnum getBusinessPageExceptionEnumByCode(String code){
        Assert.notNull(code);
        return Optional.ofNullable(Arrays.stream(BusinessPageExceptionEnum.values()).filter(k->k.getExCode().equals(code)).findFirst().get()).orElse(null);
    }
}
