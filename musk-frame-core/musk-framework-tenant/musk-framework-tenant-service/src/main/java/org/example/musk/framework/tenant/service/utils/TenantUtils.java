package org.example.musk.framework.tenant.service.utils;


import org.example.musk.common.context.ThreadLocalTenantContext;

import java.util.Map;
import java.util.concurrent.Callable;


/**
 * 多租户 Util
 *
 * @author musk
 */
public class TenantUtils {

    /**
     * 使用指定租户，执行对应的逻辑
     *
     * 注意，如果当前是忽略租户的情况下，会被强制设置成不忽略租户
     * 当然，执行完成后，还是会恢复回去
     *
     * @param tenantId 租户编号
     * @param runnable 逻辑
     */
    public static void execute(Long tenantId, Runnable runnable) {
        Integer oldTenantId = ThreadLocalTenantContext.getTenantId();
        try {
            ThreadLocalTenantContext.setTenantId(Math.toIntExact(tenantId));
            // 执行逻辑
            runnable.run();
        } finally {
            ThreadLocalTenantContext.setTenantId(oldTenantId);
        }
    }

}
