package org.example.musk.auth.entity.logintimebydaystatistics;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.entity.db.TenantBaseDO;

import java.time.LocalDate;

/**
 * 会员登录次数按天统计 DO
 *
 * @author 马斯克源码
 */
@TableName("member_login_time_by_day_statistics")
@KeySequence("member_login_time_by_day_statistics_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginTimeByDayStatisticsDO extends TenantBaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 会员id
     */
    private Integer memberId;
    /**
     * 登录日期
     */
    private LocalDate loginDate;
    /**
     * 登录次数
     */
    private Integer count;

}
