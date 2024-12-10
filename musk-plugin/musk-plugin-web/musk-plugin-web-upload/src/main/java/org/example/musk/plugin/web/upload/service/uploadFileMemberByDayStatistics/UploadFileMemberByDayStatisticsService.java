package org.example.musk.plugin.web.upload.service.uploadFileMemberByDayStatistics;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.*;
import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.plugin.web.upload.entity.uploadFileMemberByDayStatistics.UploadFileMemberByDayStatisticsDO;
import org.example.musk.plugin.web.upload.entity.uploadFileMemberByDayStatistics.bo.UploadFileMemberByDayStatisticsPageReqBO;
import org.example.musk.plugin.web.upload.enums.FileTypeEnums;

import java.time.LocalDate;

/**
 *  Service 接口
 *
 * @author 代码生成器
 */
public interface UploadFileMemberByDayStatisticsService extends IService<UploadFileMemberByDayStatisticsDO> {

    /**
     * 创建
     *
     * @param uploadFileMemberByDayStatisticsDO 创建信息
     * @return ID
     */
    Integer createUploadFileMemberByDayStatistics(@Valid UploadFileMemberByDayStatisticsDO uploadFileMemberByDayStatisticsDO);
    /**
     * 更新
     *
     * @param updateUploadFileMemberByDayStatistics 更新信息
     */
    void updateUploadFileMemberByDayStatisticsById(Integer id, @Valid UploadFileMemberByDayStatisticsDO updateUploadFileMemberByDayStatistics);
    /**
     * 删除
     *
     * @param id 编号
     */
    void deleteUploadFileMemberByDayStatistics(Integer id);
    /**
     * 获得
     *
     * @param id 编号
     * @return
     */
    UploadFileMemberByDayStatisticsDO getUploadFileMemberByDayStatistics(Integer id);
    /**
     * 获得分
     *
     * @param pageReqBO 分页查询
     * @return 分页
     */
    PageResult<UploadFileMemberByDayStatisticsDO> getUploadFileMemberByDayStatisticsPage(UploadFileMemberByDayStatisticsPageReqBO pageReqBO);

    void saveUploadFileMemberByDayStatistics(Integer memberId, LocalDate statisticsDate, FileTypeEnums fileTypeEnums, long times, double fileSize);
}
