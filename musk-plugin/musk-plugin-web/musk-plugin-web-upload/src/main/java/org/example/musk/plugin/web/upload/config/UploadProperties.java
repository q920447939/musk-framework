package org.example.musk.plugin.web.upload.config;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

@ConfigurationProperties(prefix = "musk.plugin.web.upload")
@Data
public class UploadProperties {

    private String basePath;

    private String domain;
    private String domainProjectPath;

    @Resource
    private Environment environment;

    @PostConstruct
    public void init() {
        if (StrUtil.isBlank(basePath)) {
            basePath = System.getProperty("user.dir") + File.separator + "upload" + File.separator + DateUtil.today() + File.separator;
        }
        if (StrUtil.isBlank(domain)) {
            domain = getServerUrl() + "/" + getContextPath();
            domainProjectPath = domain + "/" +  "static/file/"  + "/" + DateUtil.today() + "/";
        }
    }

    public int getServerPort() {
        // 获取配置的端口
        String port = environment.getProperty("server.port");
        // 如果没有配置，默认返回8080
        return port == null ? 8080 : Integer.parseInt(port);
    }

    public String getContextPath() {
        // 获取上下文路径
        String contextPath = environment.getProperty("server.servlet.context-path");
        // 如果没有配置，默认返回/
        return contextPath == null ? "/" : contextPath;
    }

    @SneakyThrows
    public String getServerIp() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            return localHost.getHostAddress();
        } catch (UnknownHostException e) {
            return "localhost";
        }
    }


    // 获取完整的服务地址
    public String getServerUrl() {
        return String.format("http://%s:%d", getServerIp(), getServerPort());
    }

}
