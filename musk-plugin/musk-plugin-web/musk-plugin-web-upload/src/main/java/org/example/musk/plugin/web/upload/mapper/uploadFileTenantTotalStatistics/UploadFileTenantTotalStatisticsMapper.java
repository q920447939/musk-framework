package org.example.musk.plugin.web.upload.mapper.uploadFileTenantTotalStatistics;

import org.apache.ibatis.annotations.Mapper;
import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.middleware.mybatisplus.mybatis.core.mapper.BaseMapperX;
import org.example.musk.middleware.mybatisplus.mybatis.core.query.LambdaQueryWrapperX;
import org.example.musk.plugin.web.upload.entity.uploadFileTenantTotalStatistics.UploadFileTenantTotalStatisticsDO;
import org.example.musk.plugin.web.upload.entity.uploadFileTenantTotalStatistics.bo.UploadFileTenantTotalStatisticsPageReqBO;

/**
 * Mapper
 *
 * @author 代码生成器
 */
@Mapper
public interface UploadFileTenantTotalStatisticsMapper extends BaseMapperX<UploadFileTenantTotalStatisticsDO> {

    default PageResult<UploadFileTenantTotalStatisticsDO> selectPage(UploadFileTenantTotalStatisticsPageReqBO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<UploadFileTenantTotalStatisticsDO>()
                .eqIfPresent(UploadFileTenantTotalStatisticsDO::getFileType, reqVO.getFileType())
                .eqIfPresent(UploadFileTenantTotalStatisticsDO::getTotalFileCount, reqVO.getTotalFileCount())
                .eqIfPresent(UploadFileTenantTotalStatisticsDO::getTotalFileSize, reqVO.getTotalFileSize())
                .betweenIfPresent(UploadFileTenantTotalStatisticsDO::getCreateTime, reqVO.getCreateTime()[0], reqVO.getCreateTime()[1])
                .orderByDesc(UploadFileTenantTotalStatisticsDO::getId));
    }

}
