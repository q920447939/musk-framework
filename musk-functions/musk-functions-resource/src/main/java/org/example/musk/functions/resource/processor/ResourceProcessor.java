package org.example.musk.functions.resource.processor;

import java.io.InputStream;

/**
 * 资源处理器接口
 *
 * @author musk-functions-resource
 */
public interface ResourceProcessor {

    /**
     * 处理资源
     *
     * @param inputStream 原始资源输入流
     * @param fileType    文件类型
     * @return 处理后的资源输入流
     */
    InputStream process(InputStream inputStream, String fileType);

    /**
     * 是否支持处理该文件类型
     *
     * @param fileType 文件类型
     * @return 是否支持
     */
    boolean supports(String fileType);

    /**
     * 获取处理器名称
     *
     * @return 处理器名称
     */
    String getName();

    /**
     * 获取处理器优先级
     * 数值越小优先级越高
     *
     * @return 优先级
     */
    default int getOrder() {
        return 0;
    }
}
