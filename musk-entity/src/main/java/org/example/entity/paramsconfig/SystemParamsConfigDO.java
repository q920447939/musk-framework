package org.example.entity.paramsconfig;

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
 * 参数配置 DO
 *
 * @author 马斯克源码
 */
@TableName("system_params_config")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemParamsConfigDO extends TenantBaseDO {

    /**
     * 主键
     */
    @TableId
    private Integer id;
    /**
     * 域
     */
    private Short domain;
    /**
     * 业务所属组编码
     */
    private String bGroup;
    /**
     * 参数类型编码
     */
    private String type;
    /**
     * 参数类型描述
     */
    private String typeDesc;
    /**
     * value1
     */
    private String value1;
    /**
     * value2
     */
    private String value2;
    /**
     * value3
     */
    private String value3;

    private Short status;
    /**
     * 备注
     */
    private String remark;

}
