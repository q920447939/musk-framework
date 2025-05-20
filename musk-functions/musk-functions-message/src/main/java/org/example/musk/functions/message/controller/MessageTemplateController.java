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
import org.example.musk.functions.message.model.entity.SystemMessageTemplateDO;
import org.example.musk.functions.message.model.vo.MessageTemplateCreateReqVO;
import org.example.musk.functions.message.model.vo.MessageTemplateRespVO;
import org.example.musk.functions.message.service.MessageTemplateService;
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
 * 消息模板 Controller
 *
 * @author musk-functions-message
 */
@RestController
@RequestMapping("/api/message-template")
@Validated
@Slf4j
public class MessageTemplateController {

    @Resource
    private MessageTemplateService messageTemplateService;

    /**
     * 创建消息模板
     *
     * @param createReqVO 创建消息模板请求
     * @return 模板ID
     */
    @PostMapping("/create")
    @RequirePermission(resourceType = ResourceTypeEnum.MESSAGE, operationType = OperationTypeEnum.CREATE)
    public CommonResult<Integer> createMessageTemplate(@Valid @RequestBody MessageTemplateCreateReqVO createReqVO) {
        // 设置租户ID
        Integer tenantId = ThreadLocalTenantContext.getTenantId();
        // 设置域ID，如果请求中没有指定，则使用当前上下文的域ID
        Integer domainId = createReqVO.getDomainId() != null ?
                createReqVO.getDomainId() : ThreadLocalTenantContext.getDomainId();
        createReqVO.setTenantId(tenantId);
        createReqVO.setDomainId(domainId);

        return success(messageTemplateService.createMessageTemplate(createReqVO));
    }

    /**
     * 更新消息模板
     *
     * @param template 模板对象
     * @return 是否更新成功
     */
    @PutMapping("/update")
    @RequirePermission(resourceType = ResourceTypeEnum.MESSAGE, operationType = OperationTypeEnum.UPDATE)
    public CommonResult<Boolean> updateMessageTemplate(@Valid @RequestBody SystemMessageTemplateDO template) {
        return success(messageTemplateService.updateMessageTemplate(template));
    }

    /**
     * 删除消息模板
     *
     * @param id 模板ID
     * @return 是否删除成功
     */
    @DeleteMapping("/delete")
    @RequirePermission(resourceType = ResourceTypeEnum.MESSAGE, operationType = OperationTypeEnum.DELETE)
    public CommonResult<Boolean> deleteMessageTemplate(@RequestParam("id") Integer id) {
        return success(messageTemplateService.deleteMessageTemplate(id));
    }

    /**
     * 获取消息模板详情
     *
     * @param id 模板ID
     * @return 模板详情
     */
    @GetMapping("/get")
    @RequirePermission(resourceType = ResourceTypeEnum.MESSAGE, operationType = OperationTypeEnum.READ)
    public CommonResult<MessageTemplateRespVO> getMessageTemplate(@RequestParam("id") Integer id) {
        SystemMessageTemplateDO template = messageTemplateService.getMessageTemplate(id);
        return CommonResultUtils.wrapEmptyObjResult(template,
                () -> BeanUtils.toBean(template, MessageTemplateRespVO.class));
    }

    /**
     * 分页获取消息模板列表
     *
     * @param templateType 模板类型
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 模板分页列表
     */
    @GetMapping("/page")
    @RequirePermission(resourceType = ResourceTypeEnum.MESSAGE, operationType = OperationTypeEnum.READ)
    public CommonResult<PageResult<MessageTemplateRespVO>> getMessageTemplatePage(
            @RequestParam(value = "templateType", required = false) Integer templateType,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize) {

        Integer tenantId = ThreadLocalTenantContext.getTenantId();
        Integer domainId = ThreadLocalTenantContext.getDomainId();

        return success(messageTemplateService.getMessageTemplatePage(tenantId, domainId, templateType, pageNum, pageSize));
    }
}
