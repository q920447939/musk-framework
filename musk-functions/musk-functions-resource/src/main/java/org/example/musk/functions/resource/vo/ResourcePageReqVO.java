package org.example.musk.functions.resource.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.example.musk.common.pojo.db.PageParam;

/**
 * 资源分页查询请求VO
 *
 * @author musk-functions-resource
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ResourcePageReqVO extends PageParam {

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源编码
     */
    private String resourceCode;

    /**
     * 资源类型
     */
    private Integer resourceType;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 分类ID
     */
    private Integer categoryId;

    /**
     * 标签
     */
    private String tag;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 开始时间
     */
    private String beginTime;

    /**
     * 结束时间
     */
    private String endTime;
}
