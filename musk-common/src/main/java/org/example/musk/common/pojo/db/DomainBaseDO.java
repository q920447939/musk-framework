package org.example.musk.common.pojo.db;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 拓展多租户的 BaseDO 基类
 *
 * @author
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class DomainBaseDO extends TenantBaseDO {

    /**
     * 多租户编号
     */
    private Integer domainId;

}
