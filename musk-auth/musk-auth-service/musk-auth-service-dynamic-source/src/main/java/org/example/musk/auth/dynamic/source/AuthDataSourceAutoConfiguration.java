package org.example.musk.auth.dynamic.source;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(AuthDataSourceProperties.class)
public class AuthDataSourceAutoConfiguration {

    @Bean
    //@ConditionalOnProperty(prefix = "musk.auth.ds",name = {"member","log","statistics",})
    public AuthDataSourceAspect authDataSourceAspect(
            AuthDataSourceProperties properties,
            DynamicRoutingDataSource dataSource) {
        // 验证配置的数据源是否存在
        validateDataSources(properties, dataSource);
        return new AuthDataSourceAspect(properties);
    }

    private void validateDataSources(AuthDataSourceProperties properties,
                                   DynamicRoutingDataSource dataSource) {
        Map<String, String> dsMapping = properties.getDs().toMap();
        Map<String, DataSource> availableDataSources = dataSource.getDataSources();

        for (Map.Entry<String, String> entry : dsMapping.entrySet()) {
            String module = entry.getKey();
            String dsKey = entry.getValue();
            if (!availableDataSources.containsKey(dsKey)) {
                throw new IllegalStateException(
                    String.format("Module '%s' configured datasource '%s' does not exist",
                    module, dsKey));
            }
        }
    }
}
