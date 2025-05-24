package org.example.musk.functions.resource.config;

import org.example.musk.functions.cache.config.MuskFunctionsCacheAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * 资源模块自动配置类
 *
 * @author musk-functions-resource
 */
@AutoConfiguration(after = {MuskFunctionsCacheAutoConfiguration.class})
@ComponentScan(basePackages = "org.example.musk.functions.resource")
@EnableConfigurationProperties(ResourceProperties.class)
public class MuskFunctionsResourceAutoConfiguration {
}
