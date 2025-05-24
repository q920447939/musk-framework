package org.example.musk.functions.resource.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.musk.common.pojo.db.DomainBaseDO;

/**
 * 资源表 DO
 *
 * @author musk-functions-resource
 */
@TableName("system_resource")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemResourceDO extends DomainBaseDO {

    /**
     * 资源ID
     */
    @TableId
    private Integer id;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源编码（可选）
     */
    private String resourceCode;

    /**
     * 资源类型（1:图片 2:文档 3:视频 4:音频 5:压缩包 6:其他）
     */
    private Integer resourceType;

    /**
     * 文件类型（扩展名）
     */
    private String fileType;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 存储类型（1:本地 2:阿里云 3:腾讯云 等）
     */
    private Integer storageType;

    /**
     * 存储路径
     */
    private String storagePath;

    /**
     * 访问URL
     */
    private String accessUrl;

    /**
     * 文件MD5值
     */
    private String md5;

    /**
     * 状态（0正常 1禁用）
     */
    private Integer status;

    /**
     * 分类ID
     */
    private Integer categoryId;

    /**
     * 标签（逗号分隔）
     */
    private String tags;

    /**
     * 描述
     */
    private String description;
}
