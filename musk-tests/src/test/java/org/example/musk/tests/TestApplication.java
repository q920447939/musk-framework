package org.example.musk.tests;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 测试应用程序配置类
 *
 * @author musk-functions-message
 */
@SpringBootApplication(scanBasePackages = "org.example") // 排除数据源自动配置，我们会在测试配置中手动配置
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
