package org.example.musk.framework.permission.entity;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.musk.common.pojo.db.TenantBaseDO;

/**
 * 领域权限 DO
 * 
 * 用于存储不同租户下不同领域对各类资源的操作权限
 *
 * @author musk-framework-permission
 */
@TableName("domain_permission")
@KeySequence("domain_permission_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DomainPermissionDO extends TenantBaseDO {

    /**
     * 权限ID
     */
    @TableId
    private Long id;
    
    /**
     * 领域ID
     */
    private Integer domainId;
    
    /**
     * 资源类型
     * 例如：MENU、SYSTEM_PARAMS等
     */
    private String resourceType;
    
    /**
     * 资源ID
     * 当为null时表示对整个资源类型的权限控制
     * 当不为null时表示对特定资源实例的权限控制
     * 例如：对于MENU类型，可以是菜单ID；对于SYSTEM_PARAMS类型，可以是配置项的key
     */
    private String resourceId;
    
    /**
     * 操作类型
     * 例如：CREATE、READ、UPDATE、DELETE
     */
    private String operationType;
    
    /**
     * 是否允许
     * true表示允许，false表示不允许
     */
    private Boolean isAllowed;
    
    /**
     * 权限描述
     */
    private String description;
}
