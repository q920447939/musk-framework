package org.example.musk.middleware.mybatisplus.datasource.config;

import com.alibaba.druid.spring.boot3.autoconfigure.properties.DruidStatProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 数据库配置类
 *
 * @author
 */
@AutoConfiguration
@EnableTransactionManagement(proxyTargetClass = true) // 启动事务管理
@EnableConfigurationProperties(DruidStatProperties.class)
public class MuskDataSourceAutoConfiguration {

}
