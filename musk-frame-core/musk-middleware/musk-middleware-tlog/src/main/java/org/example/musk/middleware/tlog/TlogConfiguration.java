package org.example.musk.middleware.tlog;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class TlogConfiguration {


    @Bean
    public TLogIdGeneratorStrategy tLogIdGeneratorStrategy() {
        return new TLogIdGeneratorStrategy();
    }
}
