package org.example.musk.common.context;


import com.alibaba.ttl.TransmittableThreadLocal;

import java.time.LocalDateTime;

public class ThreadLocalTenantContext {

    private static TransmittableThreadLocal<Integer> tenantThreadLocal = new TransmittableThreadLocal<>();
    private static TransmittableThreadLocal<Integer> memberThreadLocal = new TransmittableThreadLocal<>();
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
}
