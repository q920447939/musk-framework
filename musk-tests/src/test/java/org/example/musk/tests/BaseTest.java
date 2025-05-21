package org.example.musk.tests;

import org.example.musk.common.context.ThreadLocalTenantContext;
import org.junit.jupiter.api.BeforeAll;

/**
 * ClassName: BaseTest
 *
 * @author
 * @Description:
 * @date 2025年05月20日
 */
public class BaseTest {
    @BeforeAll
    static void setUpBeforeClass() {
        // 这里放置一些在所有测试用例执行之前需要运行的代码
        ThreadLocalTenantContext.setTenantId(1);
        ThreadLocalTenantContext.setDomainId(1);
    }

}
