package org.example.musk.common.util;

import org.example.musk.common.context.ThreadLocalTenantContext;

import java.util.function.Supplier;

/**
 * 域操作工具类
 * 
 * 用于在指定域下执行操作
 *
 * @author musk-common
 */
public class DomainOperationUtil {
    
    /**
     * 在指定域下执行操作
     *
     * @param domainId 域ID
     * @param operation 要执行的操作
     * @param <T> 操作返回类型
     * @return 操作结果
     */
    public static <T> T executeWithDomain(Integer domainId, Supplier<T> operation) {
        Integer originalDomainId = ThreadLocalTenantContext.getDomainId();
        try {
            ThreadLocalTenantContext.setDomainId(domainId);
            return operation.get();
        } finally {
            ThreadLocalTenantContext.setDomainId(originalDomainId);
        }
    }
    
    /**
     * 在指定域下执行无返回值操作
     *
     * @param domainId 域ID
     * @param operation 要执行的操作
     */
    public static void executeWithDomain(Integer domainId, Runnable operation) {
        Integer originalDomainId = ThreadLocalTenantContext.getDomainId();
        try {
            ThreadLocalTenantContext.setDomainId(domainId);
            operation.run();
        } finally {
            ThreadLocalTenantContext.setDomainId(originalDomainId);
        }
    }
}
