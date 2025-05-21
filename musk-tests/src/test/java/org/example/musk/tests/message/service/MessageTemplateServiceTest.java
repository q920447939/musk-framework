package org.example.musk.tests.message.service;

import jakarta.annotation.Resource;
import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.functions.message.enums.MessagePlatformTypeEnum;
import org.example.musk.functions.message.enums.MessageTypeEnum;
import org.example.musk.functions.message.model.entity.SystemMessageTemplateDO;
import org.example.musk.functions.message.model.vo.MessageTemplateCreateReqVO;
import org.example.musk.functions.message.model.vo.MessageTemplateRespVO;
import org.example.musk.tests.TestApplication;
import org.example.musk.tests.service.MessageTemplateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息模板服务测试类
 *
 * @author musk-functions-message
 */
@SpringBootTest(
    classes = TestApplication.class,
    properties = {
        "spring.profiles.active=test"
    }
)
public class MessageTemplateServiceTest {

    @Resource
    private MessageTemplateService messageTemplateService;

    /**
     * 测试创建消息模板
     */
    @Test
    public void testCreateMessageTemplate() {
        // 创建消息模板请求
        MessageTemplateCreateReqVO createReqVO = new MessageTemplateCreateReqVO();
        createReqVO.setTenantId(1);
        createReqVO.setDomainId(1);
        createReqVO.setTemplateCode("test_template");
        createReqVO.setTemplateName("测试模板");
        createReqVO.setTemplateType(MessageTypeEnum.TEXT.getType());
        createReqVO.setTitleTemplate("测试标题 ${param1}");
        createReqVO.setContentTemplate("测试内容 ${param1} ${param2}");
        createReqVO.setPriority(0);
        createReqVO.setIsForced(false);
        createReqVO.setActionType(0);
        createReqVO.setPlatformType(MessagePlatformTypeEnum.ALL.getType());
        createReqVO.setStatus(1);

        // 创建消息模板
        Integer templateId = messageTemplateService.createMessageTemplate(createReqVO);
        Assertions.assertNotNull(templateId);

        // 获取消息模板
        SystemMessageTemplateDO template = messageTemplateService.getMessageTemplate(templateId);
        Assertions.assertNotNull(template);
        Assertions.assertEquals(createReqVO.getTemplateCode(), template.getTemplateCode());
        Assertions.assertEquals(createReqVO.getTemplateName(), template.getTemplateName());
    }

    /**
     * 测试更新消息模板
     */
    @Test
    public void testUpdateMessageTemplate() {
        // 先创建一个消息模板
        MessageTemplateCreateReqVO createReqVO = new MessageTemplateCreateReqVO();
        createReqVO.setTenantId(1);
        createReqVO.setDomainId(1);
        createReqVO.setTemplateCode("update_template");
        createReqVO.setTemplateName("原始模板");
        createReqVO.setTemplateType(MessageTypeEnum.TEXT.getType());
        createReqVO.setTitleTemplate("原始标题 ${param1}");
        createReqVO.setContentTemplate("原始内容 ${param1} ${param2}");
        createReqVO.setPlatformType(MessagePlatformTypeEnum.ALL.getType());
        createReqVO.setStatus(1);

        Integer templateId = messageTemplateService.createMessageTemplate(createReqVO);

        // 更新消息模板
        SystemMessageTemplateDO template = messageTemplateService.getMessageTemplate(templateId);
        template.setTemplateName("更新后的模板");
        template.setTitleTemplate("更新后的标题 ${param1}");
        template.setContentTemplate("更新后的内容 ${param1} ${param2}");

        boolean success = messageTemplateService.updateMessageTemplate(template);
        Assertions.assertTrue(success);

        // 获取更新后的消息模板
        SystemMessageTemplateDO updatedTemplate = messageTemplateService.getMessageTemplate(templateId);
        Assertions.assertNotNull(updatedTemplate);
        Assertions.assertEquals(template.getTemplateName(), updatedTemplate.getTemplateName());
        Assertions.assertEquals(template.getTitleTemplate(), updatedTemplate.getTitleTemplate());
        Assertions.assertEquals(template.getContentTemplate(), updatedTemplate.getContentTemplate());
    }

    /**
     * 测试删除消息模板
     */
    @Test
    public void testDeleteMessageTemplate() {
        // 先创建一个消息模板
        MessageTemplateCreateReqVO createReqVO = new MessageTemplateCreateReqVO();
        createReqVO.setTenantId(1);
        createReqVO.setDomainId(1);
        createReqVO.setTemplateCode("delete_template");
        createReqVO.setTemplateName("待删除模板");
        createReqVO.setTemplateType(MessageTypeEnum.TEXT.getType());
        createReqVO.setTitleTemplate("待删除标题 ${param1}");
        createReqVO.setContentTemplate("待删除内容 ${param1} ${param2}");
        createReqVO.setPlatformType(MessagePlatformTypeEnum.ALL.getType());
        createReqVO.setStatus(1);

        Integer templateId = messageTemplateService.createMessageTemplate(createReqVO);

        // 删除消息模板
        boolean success = messageTemplateService.deleteMessageTemplate(templateId);
        Assertions.assertTrue(success);

        // 获取删除后的消息模板
        SystemMessageTemplateDO template = messageTemplateService.getMessageTemplate(templateId);
        Assertions.assertNull(template);
    }

    /**
     * 测试根据模板编码获取模板
     */
    @Test
    public void testGetMessageTemplateByCode() {
        // 获取已存在的模板
        SystemMessageTemplateDO template = messageTemplateService.getMessageTemplateByCode("system_welcome", 1, 1);
        Assertions.assertNotNull(template);
        Assertions.assertEquals("system_welcome", template.getTemplateCode());
    }

    /**
     * 测试获取消息模板列表
     */
    @Test
    public void testGetMessageTemplateList() {
        // 获取消息模板列表
        List<SystemMessageTemplateDO> templates = messageTemplateService.getMessageTemplateList(1, 1, null);
        Assertions.assertNotNull(templates);
        Assertions.assertFalse(templates.isEmpty());
    }

    /**
     * 测试分页获取消息模板列表
     */
    @Test
    public void testGetMessageTemplatePage() {
        // 分页获取消息模板列表
        PageResult<MessageTemplateRespVO> pageResult = messageTemplateService.getMessageTemplatePage(1, 1, null, 1, 10);
        Assertions.assertNotNull(pageResult);
        Assertions.assertFalse(pageResult.getList().isEmpty());
    }

    /**
     * 测试渲染模板
     */
    @Test
    public void testRenderTemplate() {
        // 获取已存在的模板
        SystemMessageTemplateDO template = messageTemplateService.getMessageTemplateByCode("system_welcome", 1, 1);
        Assertions.assertNotNull(template);

        // 准备模板参数
        Map<String, Object> params = new HashMap<>();
        params.put("userName", "测试用户");

        // 渲染模板
        Map<String, String> renderedContent = messageTemplateService.renderTemplate(template, params);
        Assertions.assertNotNull(renderedContent);
        Assertions.assertEquals("欢迎使用系统", renderedContent.get("title"));
        Assertions.assertEquals("尊敬的测试用户，欢迎使用我们的系统！", renderedContent.get("content"));
    }
}
