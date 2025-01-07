package org.example.musk.plugin.web.upload.service.uploadFileMemberTotalStatistics;

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
import org.example.musk.plugin.web.upload.entity.uploadFileMemberTotalStatistics.UploadFileMemberTotalStatisticsDO;
import org.example.musk.plugin.web.upload.entity.uploadFileMemberTotalStatistics.bo.UploadFileMemberTotalStatisticsPageReqBO;
import org.example.musk.plugin.web.upload.enums.FileTypeEnums;
import org.example.musk.plugin.web.upload.mapper.uploadFileMemberTotalStatistics.UploadFileMemberTotalStatisticsMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 *  Service 实现类
 *
 * @author 代码生成器
 */
@Service
@Validated
@Slf4j
@PluginDynamicSource(group = "plugin", ds = "upload")
public class UploadFileMemberTotalStatisticsServiceImpl extends ServiceImpl<UploadFileMemberTotalStatisticsMapper, UploadFileMemberTotalStatisticsDO> implements UploadFileMemberTotalStatisticsService {

    @Override
    public Integer createUploadFileMemberTotalStatistics(@Valid UploadFileMemberTotalStatisticsDO uploadFileMemberTotalStatistics) {
        this.baseMapper.insert(uploadFileMemberTotalStatistics);
        return uploadFileMemberTotalStatistics.getId();
    }
    @Override
    public void updateUploadFileMemberTotalStatisticsById(Integer id, @Valid UploadFileMemberTotalStatisticsDO uploadFileMemberTotalStatistics) {
        // 校验存在
        validateUploadFileMemberTotalStatisticsExists(id);
        // 更新
        this.baseMapper.updateById(uploadFileMemberTotalStatistics);
    }
    @Override
    public void deleteUploadFileMemberTotalStatistics(Integer id) {
        // 校验存在
        validateUploadFileMemberTotalStatisticsExists(id);
        // 删除
        this.baseMapper.deleteById(id);
    }
    private void validateUploadFileMemberTotalStatisticsExists(Integer id) {
        if (getUploadFileMemberTotalStatistics(id) == null) {
            throw new RuntimeException("未获取到信息");
        }
    }
    @Override
    public UploadFileMemberTotalStatisticsDO getUploadFileMemberTotalStatistics(Integer id) {
        return this.baseMapper.selectById(id);
    }
    @Override
    public PageResult<UploadFileMemberTotalStatisticsDO> getUploadFileMemberTotalStatisticsPage(UploadFileMemberTotalStatisticsPageReqBO pageReqBO) {
        return this.baseMapper.selectPage(pageReqBO);
    }

    @Override
    @PluginLockSafeExec(group = LockGroupEnums.GROUP_TENANT)
    public void saveUploadFileMemberTotalStatistics(Integer memberId, FileTypeEnums fileTypeEnums, long times, double fileSize) {
        UploadFileMemberTotalStatisticsDO result = this.baseMapper.selectOne(new LambdaQueryWrapperX<UploadFileMemberTotalStatisticsDO>()
                .eq(UploadFileMemberTotalStatisticsDO::getFileType,fileTypeEnums.getFileType())
                .eq(UploadFileMemberTotalStatisticsDO::getMemberId,memberId)
        );
        if (null == result) {
            @Valid UploadFileMemberTotalStatisticsDO info = new UploadFileMemberTotalStatisticsDO();
            info.setMemberId(memberId);
            info.setFileType(fileTypeEnums.getFileType());
            info.setTotalFileCount(times);
            info.setTotalFileSize(fileSize);
            createUploadFileMemberTotalStatistics(info);
            return;
        }
        UploadFileMemberTotalStatisticsDO update = new UploadFileMemberTotalStatisticsDO();
        update.setTotalFileCount(result.getTotalFileCount() + times);
        update.setTotalFileSize(result.getTotalFileSize() + fileSize);

        update.setId(result.getId());
        this.baseMapper.updateById(update);
    }


    @Override
    public List<UploadFileMemberTotalStatisticsDO> listUploadFileMemberTotalStatistics(int memberId) {
        return this.baseMapper.selectList(new LambdaQueryWrapperX<UploadFileMemberTotalStatisticsDO>().eq(UploadFileMemberTotalStatisticsDO::getMemberId,memberId));
    }
}
