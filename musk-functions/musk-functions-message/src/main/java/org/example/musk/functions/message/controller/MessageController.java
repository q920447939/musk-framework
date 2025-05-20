package org.example.musk.functions.message.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.common.util.commonResult.CommonResultUtils;
import org.example.musk.common.util.object.BeanUtils;
import org.example.musk.framework.permission.enums.OperationTypeEnum;
import org.example.musk.framework.permission.enums.ResourceTypeEnum;
import org.example.musk.framework.permission.web.anno.RequirePermission;
import org.example.musk.functions.message.model.entity.SystemMessageDO;
import org.example.musk.functions.message.model.vo.MessageCreateReqVO;
import org.example.musk.functions.message.model.vo.MessageDetailVO;
import org.example.musk.functions.message.model.vo.MessageRespVO;
import org.example.musk.functions.message.model.vo.MessageSendReqVO;
import org.example.musk.functions.message.model.vo.MessageTemplateSendReqVO;
import org.example.musk.functions.message.model.vo.MessageUpdateReqVO;
import org.example.musk.functions.message.model.vo.UserMessageVO;
import org.example.musk.functions.message.service.MessageManageService;
import org.example.musk.functions.message.service.MessageQueryService;
import org.example.musk.functions.message.service.MessageReadService;
import org.example.musk.functions.message.service.MessageSendService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.example.musk.common.pojo.CommonResult.success;

/**
 * 消息 Controller
 *
 * @author musk-functions-message
 */
@RestController
@RequestMapping("/api/message")
@Validated
@Slf4j
public class MessageController {

    @Resource
    private MessageManageService messageManageService;

    @Resource
    private MessageSendService messageSendService;

    @Resource
    private MessageQueryService messageQueryService;

    @Resource
    private MessageReadService messageReadService;

    /**
     * 创建消息
     *
     * @param createReqVO 创建消息请求
     * @return 消息ID
     */
    @PostMapping("/create")
    @RequirePermission(resourceType = ResourceTypeEnum.MESSAGE, operationType = OperationTypeEnum.CREATE)
    public CommonResult<Integer> createMessage(@Valid @RequestBody MessageCreateReqVO createReqVO) {
        // 设置租户ID
        Integer tenantId = ThreadLocalTenantContext.getTenantId();
        // 设置域ID，如果请求中没有指定，则使用当前上下文的域ID
        Integer domainId = createReqVO.getDomainId() != null ?
                createReqVO.getDomainId() : ThreadLocalTenantContext.getDomainId();
        createReqVO.setDomainId(domainId);

        return success(messageManageService.createMessage(createReqVO));
    }

    /**
     * 更新消息
     *
     * @param updateReqVO 更新消息请求
     * @return 是否更新成功
     */
    @PutMapping("/update")
    @RequirePermission(resourceType = ResourceTypeEnum.MESSAGE, operationType = OperationTypeEnum.UPDATE)
    public CommonResult<Boolean> updateMessage(@Valid @RequestBody MessageUpdateReqVO updateReqVO) {
        return success(messageManageService.updateMessage(updateReqVO));
    }

    /**
     * 删除消息
     *
     * @param id 消息ID
     * @return 是否删除成功
     */
    @DeleteMapping("/delete")
    @RequirePermission(resourceType = ResourceTypeEnum.MESSAGE, operationType = OperationTypeEnum.DELETE)
    public CommonResult<Boolean> deleteMessage(@RequestParam("id") Integer id) {
        return success(messageManageService.deleteMessage(id));
    }

    /**
     * 获取消息详情
     *
     * @param id 消息ID
     * @return 消息详情
     */
    @GetMapping("/get")
    @RequirePermission(resourceType = ResourceTypeEnum.MESSAGE, operationType = OperationTypeEnum.READ)
    public CommonResult<MessageRespVO> getMessage(@RequestParam("id") Integer id) {
        SystemMessageDO message = messageManageService.getMessage(id);
        return CommonResultUtils.wrapEmptyObjResult(message,
                () -> BeanUtils.toBean(message, MessageRespVO.class));
    }

    /**
     * 分页获取消息列表
     *
     * @param platformType 平台类型
     * @param status 状态
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 消息分页列表
     */
    @GetMapping("/page")
    @RequirePermission(resourceType = ResourceTypeEnum.MESSAGE, operationType = OperationTypeEnum.READ)
    public CommonResult<PageResult<MessageRespVO>> getMessagePage(
            @RequestParam(value = "platformType", required = false) Integer platformType,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize) {

        Integer tenantId = ThreadLocalTenantContext.getTenantId();
        Integer domainId = ThreadLocalTenantContext.getDomainId();

        return success(messageManageService.getMessagePage(tenantId, domainId, platformType, status, pageNum, pageSize));
    }

    /**
     * 发布消息
     *
     * @param id 消息ID
     * @return 是否发布成功
     */
    @PostMapping("/publish")
    @RequirePermission(resourceType = ResourceTypeEnum.MESSAGE, operationType = OperationTypeEnum.UPDATE)
    public CommonResult<Boolean> publishMessage(@RequestParam("id") Integer id) {
        return success(messageManageService.publishMessage(id));
    }

    /**
     * 取消发布消息
     *
     * @param id 消息ID
     * @return 是否取消成功
     */
    @PostMapping("/unpublish")
    @RequirePermission(resourceType = ResourceTypeEnum.MESSAGE, operationType = OperationTypeEnum.UPDATE)
    public CommonResult<Boolean> unpublishMessage(@RequestParam("id") Integer id) {
        return success(messageManageService.unpublishMessage(id));
    }

    /**
     * 发送消息
     *
     * @param sendReqVO 发送消息请求
     * @return 是否发送成功
     */
    @PostMapping("/send")
    @RequirePermission(resourceType = ResourceTypeEnum.MESSAGE, operationType = OperationTypeEnum.CREATE)
    public CommonResult<Boolean> sendMessage(@Valid @RequestBody MessageSendReqVO sendReqVO) {
        return success(messageSendService.sendMessage(sendReqVO));
    }

    /**
     * 使用模板发送消息
     *
     * @param templateSendReqVO 使用模板发送消息请求
     * @return 消息ID
     */
    @PostMapping("/send-template")
    @RequirePermission(resourceType = ResourceTypeEnum.MESSAGE, operationType = OperationTypeEnum.CREATE)
    public CommonResult<Integer> sendTemplateMessage(@Valid @RequestBody MessageTemplateSendReqVO templateSendReqVO) {
        return success(messageSendService.sendTemplateMessage(templateSendReqVO));
    }

    /**
     * 获取用户未读消息数量
     *
     * @return 未读消息数量
     */
    @GetMapping("/unread-count")
    public CommonResult<Integer> getUnreadMessageCount() {
        Integer userId = ThreadLocalTenantContext.getMemberId();
        Integer tenantId = ThreadLocalTenantContext.getTenantId();
        Integer domainId = ThreadLocalTenantContext.getDomainId();

        return success(messageQueryService.getUnreadMessageCount(userId, tenantId, domainId));
    }

    /**
     * 获取用户消息列表
     *
     * @param isRead 是否已读（null表示全部）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 消息分页列表
     */
    @GetMapping("/user-messages")
    public CommonResult<PageResult<UserMessageVO>> getUserMessages(
            @RequestParam(value = "isRead", required = false) Boolean isRead,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize) {

        Integer userId = ThreadLocalTenantContext.getMemberId();
        Integer tenantId = ThreadLocalTenantContext.getTenantId();
        Integer domainId = ThreadLocalTenantContext.getDomainId();

        return success(messageQueryService.getUserMessages(userId, tenantId, domainId, pageNum, pageSize, isRead));
    }

    /**
     * 获取消息详情
     *
     * @param messageId 消息ID
     * @return 消息详情
     */
    @GetMapping("/detail")
    public CommonResult<MessageDetailVO> getMessageDetail(@RequestParam("messageId") Integer messageId) {
        Integer userId = ThreadLocalTenantContext.getMemberId();
        return success(messageQueryService.getMessageDetail(messageId, userId));
    }

    /**
     * 标记消息为已读
     *
     * @param messageId 消息ID
     * @return 是否标记成功
     */
    @PostMapping("/mark-read")
    public CommonResult<Boolean> markAsRead(@RequestParam("messageId") Integer messageId) {
        Integer userId = ThreadLocalTenantContext.getMemberId();
        return success(messageReadService.markAsRead(messageId, userId));
    }

    /**
     * 标记所有消息为已读
     *
     * @return 标记成功的消息数量
     */
    @PostMapping("/mark-all-read")
    public CommonResult<Integer> markAllAsRead() {
        Integer userId = ThreadLocalTenantContext.getMemberId();
        Integer tenantId = ThreadLocalTenantContext.getTenantId();
        Integer domainId = ThreadLocalTenantContext.getDomainId();

        return success(messageReadService.markAllAsRead(userId, tenantId, domainId));
    }

    /**
     * 用户删除消息
     *
     * @param messageId 消息ID
     * @return 是否删除成功
     */
    @DeleteMapping("/user-delete")
    public CommonResult<Boolean> deleteUserMessage(@RequestParam("messageId") Integer messageId) {
        Integer userId = ThreadLocalTenantContext.getMemberId();
        return success(messageReadService.deleteUserMessage(messageId, userId));
    }
}
