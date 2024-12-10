package org.example.musk.common.threadVirtual;


import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.map.MapUtil;
import org.example.musk.common.lambdas.Produce;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
public class ThreadVirtualUtils {

    public static void run(List<Produce> produces) {
        if (CollUtil.isEmpty(produces)) {
            return;
        }
        CountDownLatch latch = new CountDownLatch(produces.size());
        for (Produce produce : produces) {
            Thread.ofVirtual()
                    .start(() -> {
                        try {
                            produce.produce();
                        } catch (Exception e) {
                            log.error("【虚拟线程】 处理异常", e);
                        } finally {
                            latch.countDown();
                        }
                    });
        }

        try {
            latch.await(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("【虚拟线程】 处理超时", e);
        }
    }

    public static void run(Produce produce) {
        Thread.ofVirtual()
                .start(() -> {
                    try {
                        produce.produce();
                    } catch (Exception e) {
                        log.error("【虚拟线程】 处理异常", e);
                    }
                });
    }


    public static <Object> Map<String, java.lang.Object> run(Map<String, Supplier<Object>> supplierMap) {
        if (MapUtil.isEmpty(supplierMap)) {
            return MapUtil.empty();
        }
        Map<String, java.lang.Object> resultMap = new HashMap<>(supplierMap.size() << 1);

        CountDownLatch latch = new CountDownLatch(supplierMap.size());
        for (String key : supplierMap.keySet()) {
            Supplier<Object> supplier = supplierMap.get(key);
            Thread.ofVirtual()
                    .start(() -> {
                        try {
                            resultMap.put(key, supplier.get());
                        } catch (Exception e) {
                            log.error("【虚拟线程】 处理异常", e);
                        } finally {
                            latch.countDown();
                        }
                    });
        }
        try {
            latch.await(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("【虚拟线程】 处理超时", e);
        }
        return resultMap;
    }

}
