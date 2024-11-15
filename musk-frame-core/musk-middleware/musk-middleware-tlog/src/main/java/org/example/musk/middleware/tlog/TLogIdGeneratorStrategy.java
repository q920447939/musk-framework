package org.example.musk.middleware.tlog;

import com.yomahub.tlog.id.TLogIdGenerator;

import java.util.UUID;

/**
 * ClassName: TLogIdGeneratorStrategy
 *
 * @author
 * @Description: tlog id生成策略
 * @date 2022年08月19日
 */
public class TLogIdGeneratorStrategy extends TLogIdGenerator {
    @Override
    public String generateTraceId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "");
    }
}
