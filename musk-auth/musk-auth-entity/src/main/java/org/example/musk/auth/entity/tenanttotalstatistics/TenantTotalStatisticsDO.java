package org.example.musk.auth.entity.tenanttotalstatistics;

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
 * 租户统计 DO
 *
 * @author 马斯克源码
 */
@TableName("member_tenant_total_statistics")
@KeySequence("member_tenant_total_statistics_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantTotalStatisticsDO extends TenantBaseDO {

    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 累计注册会员数
     */
    private Integer totalMember;
}
