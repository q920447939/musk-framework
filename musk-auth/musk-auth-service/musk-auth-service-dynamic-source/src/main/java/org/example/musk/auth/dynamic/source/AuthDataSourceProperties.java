package org.example.musk.auth.dynamic.source;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "musk.auth")
@Data
public class AuthDataSourceProperties {
    private DsConfig ds = new DsConfig();

    @Data
    public static class DsConfig {
        // 存储不同模块对应的数据源key
        private String member;
        private String log;
        private String statistics;


        // 将配置转换为Map，方便后续使用
        public Map<String, String> toMap() {
            Map<String, String> map = new HashMap<>();
            if (member != null) map.put("member", member);
            if (log != null) map.put("log", log);
            if (statistics != null) map.put("statistics", statistics);
            return map;
        }
    }

    public DsConfig getDs() {
        return ds;
    }

    public void setDs(DsConfig ds) {
        this.ds = ds;
    }
}
