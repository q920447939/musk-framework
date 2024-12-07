package org.example.musk.plugin.service.dynamic.source;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(PluginServiceDynamicDataSourceProperties.class)
@Slf4j
public class PluginServiceDynamicDataSourceAutoConfiguration {

    @Bean
    public PluginServiceDynamicDataSourceAspect authDataSourceAspect(
            PluginServiceDynamicDataSourceProperties properties,
            DynamicRoutingDataSource dataSource) {
        // 验证配置的数据源是否存在
        validateDataSources(properties, dataSource);
        return new PluginServiceDynamicDataSourceAspect(properties);
    }

    private void validateDataSources(PluginServiceDynamicDataSourceProperties properties,
                                     DynamicRoutingDataSource dataSource) {
        Map<String, Map<String, String>> groupDsMap = properties.getDs().toMap();
        Map<String, DataSource> availableDataSources = dataSource.getDataSources();

        for (String group : groupDsMap.keySet()) {
            Map<String, String> dsMapping = groupDsMap.get(group);
            for (Map.Entry<String, String> entry : dsMapping.entrySet()) {
                String module = entry.getKey();
                String dsKey = entry.getValue();
                if (!availableDataSources.containsKey(dsKey)) {
                   /* throw new IllegalStateException(
                            String.format("Module '%s' configured datasource '%s' does not exist",
                                    module, dsKey));*/
                    log.warn("Module '{}' configured datasource '{}' does not exist,use default 'master' dataSource", module, dsKey);
                    properties.getDs().setDefaultDs(group,dsKey);
                }
            }
        }
    }
}
