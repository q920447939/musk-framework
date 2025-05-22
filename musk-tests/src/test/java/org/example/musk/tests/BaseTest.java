package org.example.musk.tests;

import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.middleware.mq.redis.config.MuskRedisMQConsumerAutoConfiguration;
import org.example.musk.middleware.mq.redis.config.MuskRedisMQProducerAutoConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * ClassName: BaseTest
 *
 * @author
 * @Description:
 * @date 2025年05月20日
 */
@SpringBootTest(
        classes = TestApplication.class,
        properties = {
                "spring.profiles.active=test",
                "log4j-env=dev"
        }
)
@EnableAutoConfiguration(exclude= {MuskRedisMQConsumerAutoConfiguration.class,})
public class BaseTest {
    @BeforeAll
    static void setUpBeforeClass() {
        // 这里放置一些在所有测试用例执行之前需要运行的代码
        ThreadLocalTenantContext.setTenantId(1);
        ThreadLocalTenantContext.setDomainId(2);
    }

}
