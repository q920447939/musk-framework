package org.example.musk.plugin.service.dynamic.source;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "musk.plugin.service.dynamic-source")
@Data
public class PluginServiceDynamicDataSourceProperties {
    private DsConfig ds = new DsConfig();

    @Data
    public static class DsConfig {
        // 使用嵌套Map来存储多层级配置
        private Map<String, Map<String, String>> properties = new HashMap<>();

        public Map<String, Map<String, String>> toMap() {
            return Collections.unmodifiableMap(properties);
        }

        // 如果需要获取特定组的配置
        public Map<String, String> getGroupProperties(String groupName) {
            return properties.getOrDefault(groupName, Collections.emptyMap());
        }
        public void setDefaultDs(String groupName,String dsKey) {
            Map<String, String> groupDsMap = properties.get(groupName);
            if (null == groupDsMap) {
                return ;
            }
            //#使用默认的数据源
            groupDsMap.put(dsKey,"master");
        }
    }
}
