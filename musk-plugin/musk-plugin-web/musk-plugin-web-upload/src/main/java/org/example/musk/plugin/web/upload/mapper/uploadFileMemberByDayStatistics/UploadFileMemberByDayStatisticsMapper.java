package org.example.musk.plugin.web.upload.mapper.uploadFileMemberByDayStatistics;

import org.apache.ibatis.annotations.Mapper;
import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.middleware.mybatisplus.mybatis.core.mapper.BaseMapperX;
import org.example.musk.middleware.mybatisplus.mybatis.core.query.LambdaQueryWrapperX;
import org.example.musk.plugin.web.upload.entity.uploadFileMemberByDayStatistics.UploadFileMemberByDayStatisticsDO;
import org.example.musk.plugin.web.upload.entity.uploadFileMemberByDayStatistics.bo.UploadFileMemberByDayStatisticsPageReqBO;

/**
 *  Mapper
 *
 * @author 代码生成器
 */
@Mapper
public interface UploadFileMemberByDayStatisticsMapper extends BaseMapperX<UploadFileMemberByDayStatisticsDO> {

    default PageResult<UploadFileMemberByDayStatisticsDO> selectPage(UploadFileMemberByDayStatisticsPageReqBO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<UploadFileMemberByDayStatisticsDO>()

.eqIfPresent(UploadFileMemberByDayStatisticsDO::getMemberId, reqVO.getMemberId())
                .betweenIfPresent(UploadFileMemberByDayStatisticsDO::getStatisticsDate, reqVO.getStatisticsDate()[0], reqVO.getStatisticsDate()[1])
                .eqIfPresent(UploadFileMemberByDayStatisticsDO::getFileType, reqVO.getFileType())
                .eqIfPresent(UploadFileMemberByDayStatisticsDO::getTotalFileCount, reqVO.getTotalFileCount())
                .eqIfPresent(UploadFileMemberByDayStatisticsDO::getTotalFileSize, reqVO.getTotalFileSize())
                .betweenIfPresent(UploadFileMemberByDayStatisticsDO::getCreateTime, reqVO.getCreateTime()[0], reqVO.getCreateTime()[1])
                .orderByDesc(UploadFileMemberByDayStatisticsDO::getId));
    }

}
