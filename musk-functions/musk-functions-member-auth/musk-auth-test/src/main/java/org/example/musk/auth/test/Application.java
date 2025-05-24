package org.example.musk.auth.test;

import org.apache.ibatis.annotations.Mapper;
import org.example.musk.middleware.mybatisplus.anno.MuskMapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ClassName: Application
 *
 * @author
 * @Description:
 * @date 2024年11月13日
 */
@SpringBootApplication(scanBasePackages = {"org.example"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
