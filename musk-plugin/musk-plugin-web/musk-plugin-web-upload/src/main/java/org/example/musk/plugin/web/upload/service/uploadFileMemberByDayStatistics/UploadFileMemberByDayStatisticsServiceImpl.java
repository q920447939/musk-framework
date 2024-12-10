package org.example.musk.plugin.web.upload.service.uploadFileMemberByDayStatistics;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.validation.*;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.common.util.object.BeanUtils;
import org.example.musk.middleware.mybatisplus.mybatis.core.query.LambdaQueryWrapperX;
import org.example.musk.plugin.lock.config.anno.PluginLockSafeExec;
import org.example.musk.plugin.lock.enums.LockGroupEnums;
import org.example.musk.plugin.service.dynamic.source.anno.PluginDynamicSource;
import org.example.musk.plugin.web.upload.entity.uploadFileMemberByDayStatistics.UploadFileMemberByDayStatisticsDO;
import org.example.musk.plugin.web.upload.entity.uploadFileMemberByDayStatistics.bo.UploadFileMemberByDayStatisticsPageReqBO;
import org.example.musk.plugin.web.upload.entity.uploadFileTenantTotalStatistics.UploadFileTenantTotalStatisticsDO;
import org.example.musk.plugin.web.upload.enums.FileTypeEnums;
import org.example.musk.plugin.web.upload.mapper.uploadFileMemberByDayStatistics.UploadFileMemberByDayStatisticsMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

/**
 *  Service 实现类
 *
 * @author 代码生成器
 */
@Service
@Validated
@Slf4j
@PluginDynamicSource(group = "plugin", ds = "upload")
public class UploadFileMemberByDayStatisticsServiceImpl extends ServiceImpl<UploadFileMemberByDayStatisticsMapper, UploadFileMemberByDayStatisticsDO> implements UploadFileMemberByDayStatisticsService {

    @Override
    public Integer createUploadFileMemberByDayStatistics(@Valid UploadFileMemberByDayStatisticsDO uploadFileMemberByDayStatistics) {
        this.baseMapper.insert(uploadFileMemberByDayStatistics);
        return uploadFileMemberByDayStatistics.getId();
    }
    @Override
    public void updateUploadFileMemberByDayStatisticsById(Integer id, @Valid UploadFileMemberByDayStatisticsDO uploadFileMemberByDayStatistics) {
        // 校验存在
        validateUploadFileMemberByDayStatisticsExists(id);
        // 更新
        this.baseMapper.updateById(uploadFileMemberByDayStatistics);
    }
    @Override
    public void deleteUploadFileMemberByDayStatistics(Integer id) {
        // 校验存在
        validateUploadFileMemberByDayStatisticsExists(id);
        // 删除
        this.baseMapper.deleteById(id);
    }
    private void validateUploadFileMemberByDayStatisticsExists(Integer id) {
        if (getUploadFileMemberByDayStatistics(id) == null) {
            throw new RuntimeException("未获取到信息");
        }
    }
    @Override
    public UploadFileMemberByDayStatisticsDO getUploadFileMemberByDayStatistics(Integer id) {
        return this.baseMapper.selectById(id);
    }
    @Override
    public PageResult<UploadFileMemberByDayStatisticsDO> getUploadFileMemberByDayStatisticsPage(UploadFileMemberByDayStatisticsPageReqBO pageReqBO) {
        return this.baseMapper.selectPage(pageReqBO);
    }


    @Override
    @PluginLockSafeExec(group = LockGroupEnums.GROUP_TENANT)
    public void saveUploadFileMemberByDayStatistics(Integer memberId, LocalDate statisticsDate, FileTypeEnums fileTypeEnums, long times, double fileSize) {
        UploadFileMemberByDayStatisticsDO result = this.baseMapper.selectOne(new LambdaQueryWrapperX<UploadFileMemberByDayStatisticsDO>()
                .eq(UploadFileMemberByDayStatisticsDO::getFileType,fileTypeEnums.getFileType())
        );
        if (null == result) {
            @Valid UploadFileMemberByDayStatisticsDO info = new UploadFileMemberByDayStatisticsDO();
            info.setMemberId(memberId);
            info.setStatisticsDate(statisticsDate);
            info.setFileType(fileTypeEnums.getFileType());
            info.setTotalFileCount(times);
            info.setTotalFileSize(fileSize);
            createUploadFileMemberByDayStatistics(info);
            return;
        }
        UploadFileMemberByDayStatisticsDO update = new UploadFileMemberByDayStatisticsDO();
        update.setTotalFileCount(result.getTotalFileCount() + times);
        update.setTotalFileSize(result.getTotalFileSize() + fileSize);

        update.setId(result.getId());
        this.baseMapper.updateById(update);
    }
}
