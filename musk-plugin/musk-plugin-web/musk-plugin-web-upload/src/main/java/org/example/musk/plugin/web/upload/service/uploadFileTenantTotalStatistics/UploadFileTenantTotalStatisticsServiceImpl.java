package org.example.musk.plugin.web.upload.service.uploadFileTenantTotalStatistics;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.validation.*;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.middleware.mybatisplus.mybatis.core.query.LambdaQueryWrapperX;
import org.example.musk.plugin.lock.config.anno.PluginLockSafeExec;
import org.example.musk.plugin.lock.core.Lock;
import org.example.musk.plugin.service.dynamic.source.anno.PluginDynamicSource;
import org.example.musk.plugin.web.upload.entity.uploadFileTenantTotalStatistics.UploadFileTenantTotalStatisticsDO;
import org.example.musk.plugin.web.upload.entity.uploadFileTenantTotalStatistics.bo.UploadFileTenantTotalStatisticsPageReqBO;
import org.example.musk.plugin.web.upload.enums.FileTypeEnums;
import org.example.musk.plugin.web.upload.mapper.uploadFileTenantTotalStatistics.UploadFileTenantTotalStatisticsMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 *  Service 实现类
 *
 * @author 代码生成器
 */
@Service
@Validated
@Slf4j
@PluginDynamicSource(group = "plugin", ds = "upload")
public class UploadFileTenantTotalStatisticsServiceImpl extends ServiceImpl<UploadFileTenantTotalStatisticsMapper, UploadFileTenantTotalStatisticsDO> implements UploadFileTenantTotalStatisticsService {

    @Resource
    private Lock lock;

    @Override
    public Integer createUploadFileTenantTotalStatistics(@Valid UploadFileTenantTotalStatisticsDO uploadFileTenantTotalStatistics) {
        this.baseMapper.insert(uploadFileTenantTotalStatistics);
        return uploadFileTenantTotalStatistics.getId();
    }
    @Override
    public void updateUploadFileTenantTotalStatisticsById(Integer id, @Valid UploadFileTenantTotalStatisticsDO uploadFileTenantTotalStatistics) {
        // 校验存在
        validateUploadFileTenantTotalStatisticsExists(id);
        // 更新
        this.baseMapper.updateById(uploadFileTenantTotalStatistics);
    }
    @Override
    public void deleteUploadFileTenantTotalStatistics(Integer id) {
        // 校验存在
        validateUploadFileTenantTotalStatisticsExists(id);
        // 删除
        this.baseMapper.deleteById(id);
    }
    private void validateUploadFileTenantTotalStatisticsExists(Integer id) {
        if (getUploadFileTenantTotalStatisticsTotalFileSize(id) == null) {
            throw new RuntimeException("未获取到信息");
        }
    }
    @Override
    public UploadFileTenantTotalStatisticsDO getUploadFileTenantTotalStatisticsTotalFileSize(Integer id) {
        return this.baseMapper.selectById(id);
    }
    @Override
    public PageResult<UploadFileTenantTotalStatisticsDO> getUploadFileTenantTotalStatisticsPage(UploadFileTenantTotalStatisticsPageReqBO pageReqBO) {
        return this.baseMapper.selectPage(pageReqBO);
    }

    @Override
    @PluginLockSafeExec
    public void saveUploadFileTenantTotalStatistics(Integer tenantId, FileTypeEnums fileTypeEnums, long addFileCount, double fileSize) {
        UploadFileTenantTotalStatisticsDO uploadFileTenantTotalStatisticsDO = this.baseMapper.selectOne(new LambdaQueryWrapperX<UploadFileTenantTotalStatisticsDO>()
                .eq(UploadFileTenantTotalStatisticsDO::getFileType,fileTypeEnums.getFileType())
        );
        if (null == uploadFileTenantTotalStatisticsDO) {
            @Valid UploadFileTenantTotalStatisticsDO info = new UploadFileTenantTotalStatisticsDO();
            info.setTenantId(tenantId);
            info.setFileType(fileTypeEnums.getFileType());
            info.setTotalFileCount(addFileCount);
            info.setTotalFileSize(fileSize);
            createUploadFileTenantTotalStatistics(info);
            return;
        }
        UploadFileTenantTotalStatisticsDO update = new UploadFileTenantTotalStatisticsDO();
        update.setTotalFileCount(uploadFileTenantTotalStatisticsDO.getTotalFileCount() + addFileCount);
        update.setTotalFileSize(uploadFileTenantTotalStatisticsDO.getTotalFileSize() + fileSize);

        update.setId(uploadFileTenantTotalStatisticsDO.getId());
        this.baseMapper.updateById(update);
    }

    @Override
    public UploadFileTenantTotalStatisticsDO getUploadFileTenantTotalStatistics() {
        return this.baseMapper.selectOne(null);
    }
}
