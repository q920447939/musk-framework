package org.example.musk.plugin.web.upload.entity.uploadFileMemberByDayStatistics;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;
import org.example.musk.common.pojo.db.BaseDO;

/**
 *  DO
 *
 * @author 代码生成器
 */
@TableName("upload_file_member_by_day_statistics")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileMemberByDayStatisticsDO extends BaseDO {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private Integer tenantId;

    /**
     * 会员ID
     */
    @TableField("member_id")
    private Integer memberId;

    /**
     * 统计日期
     */
    @TableField("statistics_date")
    private LocalDate statisticsDate;

    /**
     * 文件类型(1:图片.2:视频)
     */
    @TableField("file_type")
    private Integer fileType;

    /**
     * 上传文件总次数
     */
    @TableField("total_file_count")
    private Long totalFileCount;

    /**
     * 上传文件的总大小
     */
    @TableField("total_file_size")
    private Double totalFileSize;

}
