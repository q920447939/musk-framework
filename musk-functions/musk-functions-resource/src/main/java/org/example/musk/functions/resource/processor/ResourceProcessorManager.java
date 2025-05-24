package org.example.musk.functions.resource.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 资源处理器管理器
 *
 * @author musk-functions-resource
 */
@Component
@Slf4j
public class ResourceProcessorManager {

    /**
     * 处理器列表
     */
    private final List<ResourceProcessor> processors = new ArrayList<>();

    /**
     * 所有处理器
     */
    @Resource
    private List<ResourceProcessor> allProcessors;

    /**
     * 初始化处理器
     */
    @PostConstruct
    public void init() {
        // 添加处理器
        processors.addAll(allProcessors);

        // 按优先级排序
        processors.sort(Comparator.comparingInt(ResourceProcessor::getOrder));

        // 打印处理器信息
        log.info("已加载 {} 个资源处理器", processors.size());
        for (ResourceProcessor processor : processors) {
            log.info("资源处理器: {}, 优先级: {}", processor.getName(), processor.getOrder());
        }
    }

    /**
     * 处理资源
     *
     * @param inputStream 原始资源输入流
     * @param fileType    文件类型
     * @return 处理后的资源输入流
     */
    public InputStream process(InputStream inputStream, String fileType) {
        if (inputStream == null) {
            return null;
        }

        InputStream result = inputStream;
        for (ResourceProcessor processor : processors) {
            if (processor.supports(fileType)) {
                try {
                    result = processor.process(result, fileType);
                } catch (Exception e) {
                    log.error("处理资源失败: {}", processor.getName(), e);
                }
            }
        }
        return result;
    }
}
