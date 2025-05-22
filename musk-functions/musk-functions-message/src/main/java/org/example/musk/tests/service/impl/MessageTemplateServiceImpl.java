package org.example.musk.tests.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.common.util.object.BeanUtils;
import org.example.musk.functions.cache.annotation.CacheEvict;
import org.example.musk.functions.cache.annotation.Cacheable;
import org.example.musk.functions.message.constant.MessageConstant;
import org.example.musk.functions.message.dao.SystemMessageTemplateMapper;
import org.example.musk.functions.message.exception.MessageException;
import org.example.musk.functions.message.model.entity.SystemMessageTemplateDO;
import org.example.musk.functions.message.model.vo.MessageTemplateCreateReqVO;
import org.example.musk.functions.message.model.vo.MessageTemplateRespVO;
import org.example.musk.tests.service.MessageTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 消息模板服务实现类
 *
 * @author musk-functions-message
 */
@Service
@Validated
@Slf4j
@DS(MessageConstant.DB_NAME)
public class MessageTemplateServiceImpl extends ServiceImpl<SystemMessageTemplateMapper, SystemMessageTemplateDO> implements MessageTemplateService {

    @Resource
    private SystemMessageTemplateMapper systemMessageTemplateMapper;

    // 模板变量正则表达式
    private static final Pattern TEMPLATE_VARIABLE_PATTERN = Pattern.compile("\\$\\{([^}]+)}");

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    public Integer createMessageTemplate(MessageTemplateCreateReqVO createReqVO) {
        // 校验模板编码唯一性
        validateTemplateCodeUnique(createReqVO.getTemplateCode(), createReqVO.getTenantId(), createReqVO.getDomainId(), null);

        // 转换请求VO为DO
        SystemMessageTemplateDO template = BeanUtils.toBean(createReqVO, SystemMessageTemplateDO.class);

        // 设置默认值
        if (template.getPriority() == null) {
            template.setPriority(0); // 默认普通优先级
        }
        if (template.getIsForced() == null) {
            template.setIsForced(false); // 默认非强制消息
        }
        if (template.getActionType() == null) {
            template.setActionType(0); // 默认无操作
        }
        if (template.getStatus() == null) {
            template.setStatus(1); // 默认启用
        }

        // 保存模板
        save(template);

        log.info("[createMessageTemplate][创建消息模板成功：{}]", template.getId());
        return template.getId();
    }

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    @CacheEvict(namespace = "MESSAGE", pattern = "'template:*'")
    public boolean updateMessageTemplate(SystemMessageTemplateDO template) {
        // 校验模板存在
        validateTemplateExists(template.getId());

        // 校验模板编码唯一性
        validateTemplateCodeUnique(template.getTemplateCode(), template.getTenantId(), template.getDomainId(), template.getId());

        // 更新模板
        boolean success = updateById(template);

        if (success) {
            log.info("[updateMessageTemplate][更新消息模板成功：{}]", template.getId());
        }

        return success;
    }

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    @CacheEvict(namespace = "MESSAGE", pattern = "'template:*'")
    public boolean deleteMessageTemplate(Integer templateId) {
        // 校验模板存在
        validateTemplateExists(templateId);

        // 删除模板
        boolean success = removeById(templateId);

        if (success) {
            log.info("[deleteMessageTemplate][删除消息模板成功：{}]", templateId);
        }

        return success;
    }

    @Override
    @Cacheable(namespace = "MESSAGE", key = "'template:id:' + #templateId", expireSeconds = MessageConstant.MESSAGE_TEMPLATE_CACHE_EXPIRE_SECONDS)
    public SystemMessageTemplateDO getMessageTemplate(Integer templateId) {
        return getById(templateId);
    }

    @Override
    @Cacheable(namespace = "MESSAGE", key = "'template:code:' + #templateCode + ':' + #tenantId + ':' + #domainId", expireSeconds = MessageConstant.MESSAGE_TEMPLATE_CACHE_EXPIRE_SECONDS)
    public SystemMessageTemplateDO getMessageTemplateByCode(String templateCode, Integer tenantId, Integer domainId) {
        LambdaQueryWrapper<SystemMessageTemplateDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemMessageTemplateDO::getTemplateCode, templateCode)
                .eq(SystemMessageTemplateDO::getDomainId, domainId)
                .eq(SystemMessageTemplateDO::getStatus, 1); // 只查询启用状态的模板

        return getOne(queryWrapper);
    }

    @Override
    public List<SystemMessageTemplateDO> getMessageTemplateList(Integer tenantId, Integer domainId, Integer templateType) {
        LambdaQueryWrapper<SystemMessageTemplateDO> queryWrapper = new LambdaQueryWrapper<>();

        // 设置查询条件
        if (tenantId != null) {
            queryWrapper.eq(SystemMessageTemplateDO::getTenantId, tenantId);
        }
        if (domainId != null) {
            queryWrapper.eq(SystemMessageTemplateDO::getDomainId, domainId);
        }
        if (templateType != null) {
            queryWrapper.eq(SystemMessageTemplateDO::getTemplateType, templateType);
        }

        // 按创建时间降序排序
        queryWrapper.orderByDesc(SystemMessageTemplateDO::getCreateTime);

        return list(queryWrapper);
    }

    @Override
    public PageResult<MessageTemplateRespVO> getMessageTemplatePage(Integer tenantId, Integer domainId, Integer templateType,
                                                                    Integer pageNum, Integer pageSize) {
        // 创建分页对象
        Page<SystemMessageTemplateDO> page = new Page<>(pageNum, pageSize);

        // 构建查询条件
        LambdaQueryWrapper<SystemMessageTemplateDO> queryWrapper = new LambdaQueryWrapper<>();

        // 设置查询条件
        if (tenantId != null) {
            queryWrapper.eq(SystemMessageTemplateDO::getTenantId, tenantId);
        }
        if (domainId != null) {
            queryWrapper.eq(SystemMessageTemplateDO::getDomainId, domainId);
        }
        if (templateType != null) {
            queryWrapper.eq(SystemMessageTemplateDO::getTemplateType, templateType);
        }

        // 按创建时间降序排序
        queryWrapper.orderByDesc(SystemMessageTemplateDO::getCreateTime);

        // 执行分页查询
        Page<SystemMessageTemplateDO> resultPage = page(page, queryWrapper);

        // 转换结果
        List<MessageTemplateRespVO> voList = BeanUtils.toBean(resultPage.getRecords(), MessageTemplateRespVO.class);

        // 设置枚举描述
        voList.forEach(this::setEnumDesc);

        return new PageResult<>(voList, resultPage.getTotal());
    }

    @Override
    public Map<String, String> renderTemplate(SystemMessageTemplateDO template, Map<String, Object> params) {
        if (template == null) {
            throw new MessageException(MessageException.MESSAGE_TEMPLATE_NOT_EXISTS);
        }

        try {
            Map<String, String> result = new HashMap<>();

            // 渲染标题
            String title = renderContent(template.getTitleTemplate(), params);
            result.put("title", title);

            // 渲染内容
            String content = renderContent(template.getContentTemplate(), params);
            result.put("content", content);

            return result;
        } catch (Exception e) {
            log.error("[renderTemplate][渲染模板异常：{}]", template.getId(), e);
            throw new MessageException(MessageException.MESSAGE_TEMPLATE_RENDER_FAILED, e.getMessage());
        }
    }

    /**
     * 渲染内容
     *
     * @param template 模板内容
     * @param params 参数
     * @return 渲染后的内容
     */
    private String renderContent(String template, Map<String, Object> params) {
        if (template == null || template.isEmpty()) {
            return "";
        }

        StringBuffer result = new StringBuffer();
        Matcher matcher = TEMPLATE_VARIABLE_PATTERN.matcher(template);

        while (matcher.find()) {
            String variable = matcher.group(1);
            Object value = params.get(variable);
            String replacement = value != null ? value.toString() : "";
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }

        matcher.appendTail(result);
        return result.toString();
    }

    /**
     * 校验模板是否存在
     *
     * @param templateId 模板ID
     */
    private void validateTemplateExists(Integer templateId) {
        if (getById(templateId) == null) {
            throw new MessageException(MessageException.MESSAGE_TEMPLATE_NOT_EXISTS);
        }
    }

    /**
     * 校验模板编码唯一性
     *
     * @param templateCode 模板编码
     * @param tenantId 租户ID
     * @param domainId 域ID
     * @param templateId 模板ID（更新时使用）
     */
    private void validateTemplateCodeUnique(String templateCode, Integer tenantId, Integer domainId, Integer templateId) {
        LambdaQueryWrapper<SystemMessageTemplateDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemMessageTemplateDO::getTemplateCode, templateCode)
                .eq(SystemMessageTemplateDO::getTenantId, tenantId)
                .eq(SystemMessageTemplateDO::getDomainId, domainId);

        // 排除自身
        if (templateId != null) {
            queryWrapper.ne(SystemMessageTemplateDO::getId, templateId);
        }

        if (count(queryWrapper) > 0) {
            throw new MessageException(MessageException.MESSAGE_TEMPLATE_CODE_EXISTS);
        }
    }

    /**
     * 设置枚举描述
     *
     * @param vo 模板响应VO
     */
    private void setEnumDesc(MessageTemplateRespVO vo) {
        // 设置模板类型描述
        if (vo.getTemplateType() != null) {
            try {
                vo.setTemplateTypeDesc(Objects.requireNonNull(org.example.musk.functions.message.enums.MessageTypeEnum.getByType(vo.getTemplateType())).getDesc());
            } catch (Exception e) {
                vo.setTemplateTypeDesc("未知");
            }
        }

        // 设置优先级描述
        if (vo.getPriority() != null) {
            try {
                vo.setPriorityDesc(Objects.requireNonNull(org.example.musk.functions.message.enums.MessagePriorityEnum.getByPriority(vo.getPriority())).getDesc());
            } catch (Exception e) {
                vo.setPriorityDesc("未知");
            }
        }

        // 设置操作类型描述
        if (vo.getActionType() != null) {
            try {
                vo.setActionTypeDesc(Objects.requireNonNull(org.example.musk.functions.message.enums.MessageActionTypeEnum.getByType(vo.getActionType())).getDesc());
            } catch (Exception e) {
                vo.setActionTypeDesc("未知");
            }
        }

        // 设置平台类型描述
        if (vo.getPlatformType() != null) {
            try {
                vo.setPlatformTypeDesc(Objects.requireNonNull(org.example.musk.functions.message.enums.MessagePlatformTypeEnum.getByType(vo.getPlatformType())).getDesc());
            } catch (Exception e) {
                vo.setPlatformTypeDesc("未知");
            }
        }

        // 设置状态描述
        if (vo.getStatus() != null) {
            vo.setStatusDesc(vo.getStatus() == 1 ? "启用" : "禁用");
        }
    }
}
