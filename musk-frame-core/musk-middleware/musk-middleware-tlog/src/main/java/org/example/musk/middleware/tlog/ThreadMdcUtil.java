package org.example.musk.middleware.tlog;

import com.yomahub.tlog.context.TLogContext;
import com.yomahub.tlog.core.rpc.TLogLabelBean;
import com.yomahub.tlog.core.rpc.TLogRPCHandler;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.Callable;


public class ThreadMdcUtil {

    private static final ThreadLocal<TLogRPCHandler> THREAD_LOCAL = new ThreadLocal<>();

    public static void setTraceIdIfAbsent() {
        String traceId = TLogContext.getTraceId();
        if (null == traceId || "".equals(traceId)) {
            TLogRPCHandler tLogRPCHandler = new TLogRPCHandler();
            tLogRPCHandler.processProviderSide(new TLogLabelBean());
            THREAD_LOCAL.set(tLogRPCHandler);
        }
    }

    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }


    public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                runnable.run();
            } finally {
                if (null != THREAD_LOCAL.get()) {
                    THREAD_LOCAL.get().cleanThreadLocal();
                }
                THREAD_LOCAL.remove();
                MDC.clear();
            }
        };
    }

}
