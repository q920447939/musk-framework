package org.example.musk.auth.event.login;

/**
 * ClassName: InterfaceImplementationSorter
 *
 * @author
 * @Description:
 * @date 2024年11月13日
 */

import jakarta.annotation.Resource;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InterfaceImplementationSorter {

    @Resource
    private ApplicationContext applicationContext;

    public <T> List<T> getSortedImplementations(Class<T> interfaceClass) {
        // 获取所有实现类
        Map<String, T> implementations = applicationContext.getBeansOfType(interfaceClass);

        // 转换为列表并排序
        return implementations.values().stream()
                .sorted((a, b) -> {
                    Order orderA = a.getClass().getAnnotation(Order.class);
                    Order orderB = b.getClass().getAnnotation(Order.class);

                    // 如果都没有@Order注解，保持原有顺序
                    if (orderA == null && orderB == null) {
                        return 0;
                    }

                    // 没有@Order注解的实现类排在后面
                    if (orderA == null) {
                        return 1;
                    }
                    if (orderB == null) {
                        return -1;
                    }

                    // 按@Order值排序
                    return Integer.compare(orderA.value(), orderB.value());
                })
                .collect(Collectors.toList());
    }
}
