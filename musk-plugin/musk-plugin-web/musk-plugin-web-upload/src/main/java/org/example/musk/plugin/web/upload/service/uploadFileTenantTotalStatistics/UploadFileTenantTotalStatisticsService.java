package org.example.musk.plugin.web.upload.service.uploadFileTenantTotalStatistics;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.*;
import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.plugin.web.upload.entity.uploadFileTenantTotalStatistics.UploadFileTenantTotalStatisticsDO;
import org.example.musk.plugin.web.upload.entity.uploadFileTenantTotalStatistics.bo.UploadFileTenantTotalStatisticsPageReqBO;
import org.example.musk.plugin.web.upload.enums.FileTypeEnums;

/**
 *  Service 接口
 *
 * @author 代码生成器
 */
public interface UploadFileTenantTotalStatisticsService extends IService<UploadFileTenantTotalStatisticsDO> {

    /**
     * 创建
     *
     * @param uploadFileTenantTotalStatisticsDO 创建信息
     * @return ID
     */
    Integer createUploadFileTenantTotalStatistics(@Valid UploadFileTenantTotalStatisticsDO uploadFileTenantTotalStatisticsDO);
    /**
     * 更新
     *
     * @param updateUploadFileTenantTotalStatistics 更新信息
     */
    void updateUploadFileTenantTotalStatisticsById(Integer id, @Valid UploadFileTenantTotalStatisticsDO updateUploadFileTenantTotalStatistics);
    /**
     * 删除
     *
     * @param id 编号
     */
    void deleteUploadFileTenantTotalStatistics(Integer id);
    /**
     * 获得
     *
     * @param id 编号
     * @return
     */
    UploadFileTenantTotalStatisticsDO getUploadFileTenantTotalStatistics(Integer id);
    /**
     * 获得分
     *
     * @param pageReqBO 分页查询
     * @return 分页
     */
    PageResult<UploadFileTenantTotalStatisticsDO> getUploadFileTenantTotalStatisticsPage(UploadFileTenantTotalStatisticsPageReqBO pageReqBO);

    void saveUploadFileTenantTotalStatistics(Integer tenantId, FileTypeEnums fileTypeEnums, long addFileCount, double fileSize);
}
