package org.example.musk.auth.entity.loginlog;

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
 * 会员登录记录 DO
 *
 * @author 马斯克源码
 */
@TableName("member_login_log")
@KeySequence("member_login_log_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginLogDO extends TenantBaseDO {

    /**
     * 访问ID
     */
    @TableId
    private Long id;
    /**
     * 会员ID(当登录失败时，此字段无效)
     */
    private Integer memberId;
    /**
     * 会员账号
     */
    private String memberCode;
    /**
     * 登录状态(1:成功,2:失败)
     */
    private Short loginStatus;
    /**
     * 登录参数
     */
    private String loginParams;
    /**
     * 登录失败原因
     */
    private String failReason;
    /**
     * 链路追踪编号
     */
    private String traceId;
    /**
     * 请求IP
     */
    private String requestIp;
    /**
     * 浏览器 UA
     */
    private String userAgent;

}
