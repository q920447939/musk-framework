package org.example.musk.middleware.mybatisplus.mybatis.db;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.framework.tenant.config.TenantConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * 基于 MyBatis Plus 多租户的功能，实现 DB 层面的多租户的功能
 *
 * @author
 */
public class TenantDatabaseInterceptor implements TenantLineHandler {

    private final Set<String> ignoreTables = new HashSet<>();

    public TenantDatabaseInterceptor(TenantConfig tenantConfig) {
        // 不同 DB 下，大小写的习惯不同，所以需要都添加进去
        tenantConfig.getIgnoreTables().forEach(table -> {
            ignoreTables.add(table.toLowerCase());
            ignoreTables.add(table.toUpperCase());
        });
        // 在 OracleKeyGenerator 中，生成主键时，会查询这个表，查询这个表后，会自动拼接 TENANT_ID 导致报错
        ignoreTables.add("DUAL");
    }

    @Override
    public Expression getTenantId() {
        return new LongValue(ThreadLocalTenantContext.getTenantId());
    }

    @Override
    public boolean ignoreTable(String tableName) {
        return CollUtil.contains(ignoreTables, tableName); // 情况二，忽略多租户的表
    }

}
