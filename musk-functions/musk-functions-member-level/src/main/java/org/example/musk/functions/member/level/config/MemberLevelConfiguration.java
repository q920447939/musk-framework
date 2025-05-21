package org.example.musk.functions.member.level.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 会员等级模块配置类
 *
 * @author musk-functions-member-level
 */
@Configuration
@ComponentScan(basePackages = "org.example.musk.functions.member.level")
@MapperScan(basePackages = "org.example.musk.functions.member.level.dao")
public class MemberLevelConfiguration {
}
