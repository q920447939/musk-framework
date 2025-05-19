package org.example.musk.common.context;


import com.alibaba.ttl.TransmittableThreadLocal;

import java.time.LocalDateTime;

/**
 * 线程上下文工具类
 *
 * 用于在线程中存储和获取租户ID、领域ID、会员ID等信息
 *
 * @author musk
 */
public class ThreadLocalTenantContext {

    private static TransmittableThreadLocal<Integer> tenantThreadLocal = new TransmittableThreadLocal<>();
    private static TransmittableThreadLocal<Integer> memberThreadLocal = new TransmittableThreadLocal<>();
    private static TransmittableThreadLocal<Integer> domainThreadLocal = new TransmittableThreadLocal<>();
    private static TransmittableThreadLocal<LocalDateTime> requestTimeThreadLocal = new TransmittableThreadLocal<>();

    public static void setTenantId(Integer tenantId) {
        assert null != tenantId;
        tenantThreadLocal.set(tenantId);
    }

    public static Integer getTenantId() {

        return tenantThreadLocal.get();
    }


    public static void tenantClean() {
        tenantThreadLocal.remove();
    }


    public static void setMemberThread(Integer memberId) {
        assert null != memberId;

        memberThreadLocal.set(memberId);
    }

    public static Integer getMemberId() {
        return memberThreadLocal.get();
    }


    public static void memberThreadClean() {
        memberThreadLocal.remove();
    }

    public static void setRequestTimeThreadLocal(LocalDateTime time) {
        assert null != time;

        requestTimeThreadLocal.set(time);
    }

    public static LocalDateTime getRequestTime() {
        return requestTimeThreadLocal.get();
    }


    public static void requestTimeThreadClean() {
        requestTimeThreadLocal.remove();
    }

    /**
     * 设置领域ID
     *
     * @param domainId 领域ID
     */
    public static void setDomainId(Integer domainId) {
        assert null != domainId;
        domainThreadLocal.set(domainId);
    }

    /**
     * 获取领域ID
     *
     * @return 领域ID
     */
    public static Integer getDomainId() {
        return domainThreadLocal.get();
    }

    /**
     * 清除领域ID
     */
    public static void domainClean() {
        domainThreadLocal.remove();
    }

    /**
     * 清除所有上下文
     */
    public static void cleanAll() {
        tenantClean();
        memberThreadClean();
        domainClean();
        requestTimeThreadClean();
    }
}
