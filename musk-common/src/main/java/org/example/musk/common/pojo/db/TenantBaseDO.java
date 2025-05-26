package org.example.musk.common.pojo.db;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 拓展多租户的 BaseDO 基类
 *
 * @author
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public abstract class TenantBaseDO extends BaseDO {

    /**
     * 多租户编号
     */
    private Integer tenantId;

}
