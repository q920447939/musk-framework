package org.example.musk.auth.entity.tenantregisterdailystatistics;

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

import java.time.LocalDate;

/**
 * 租户注册按日期统计 DO
 *
 * @author
 */
@TableName("member_tenant_register_daily_statistics")
@KeySequence("member_tenant_register_daily_statistics_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantRegisterDailyStatisticsDO extends TenantBaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 注册日期
     */
    private LocalDate registerDate;
    /**
     * 注册数量
     */
    private Integer num;

}
