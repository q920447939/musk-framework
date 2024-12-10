package org.example.musk.plugin.web.upload.mapper.uploadFileMemberTotalStatistics;

import org.apache.ibatis.annotations.Mapper;
import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.middleware.mybatisplus.mybatis.core.mapper.BaseMapperX;
import org.example.musk.middleware.mybatisplus.mybatis.core.query.LambdaQueryWrapperX;
import org.example.musk.plugin.web.upload.entity.uploadFileMemberTotalStatistics.UploadFileMemberTotalStatisticsDO;
import org.example.musk.plugin.web.upload.entity.uploadFileMemberTotalStatistics.bo.UploadFileMemberTotalStatisticsPageReqBO;

/**
 *  Mapper
 *
 * @author 代码生成器
 */
@Mapper
public interface UploadFileMemberTotalStatisticsMapper extends BaseMapperX<UploadFileMemberTotalStatisticsDO> {

    default PageResult<UploadFileMemberTotalStatisticsDO> selectPage(UploadFileMemberTotalStatisticsPageReqBO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<UploadFileMemberTotalStatisticsDO>()

.eqIfPresent(UploadFileMemberTotalStatisticsDO::getMemberId, reqVO.getMemberId())
                .eqIfPresent(UploadFileMemberTotalStatisticsDO::getFileType, reqVO.getFileType())
                .eqIfPresent(UploadFileMemberTotalStatisticsDO::getTotalFileCount, reqVO.getTotalFileCount())
                .eqIfPresent(UploadFileMemberTotalStatisticsDO::getTotalFileSize, reqVO.getTotalFileSize())
                .betweenIfPresent(UploadFileMemberTotalStatisticsDO::getCreateTime, reqVO.getCreateTime()[0], reqVO.getCreateTime()[1])
                .orderByDesc(UploadFileMemberTotalStatisticsDO::getId));
    }

}
