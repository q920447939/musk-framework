package org.example.musk.plugin.web.upload.limit.tenant;


import jakarta.annotation.Resource;
import org.example.musk.plugin.web.upload.config.UploadFileLimitProperties;
import org.example.musk.plugin.web.upload.entity.uploadFileTenantTotalStatistics.UploadFileTenantTotalStatisticsDO;
import org.example.musk.plugin.web.upload.service.uploadFileTenantTotalStatistics.UploadFileTenantTotalStatisticsService;
import org.springframework.stereotype.Service;

@Service
public class DefaultTenantUploadFileLimit implements TenantUploadFileLimit {

    @Resource
    private UploadFileTenantTotalStatisticsService uploadFileTenantTotalStatisticsService;

    @Resource
    private UploadFileLimitProperties uploadFileLimitProperties;


    @Override
    public boolean fileExceedLimit(int fileCount, long fileBytes) {
        UploadFileTenantTotalStatisticsDO uploadFileTenantTotalStatisticsDO = uploadFileTenantTotalStatisticsService.getUploadFileTenantTotalStatistics();
        long dbFileCount = 0;
        double dbFileBytes = 0;
        if (null != uploadFileTenantTotalStatisticsDO) {
            dbFileCount = uploadFileTenantTotalStatisticsDO.getTotalFileCount();
            dbFileBytes = uploadFileTenantTotalStatisticsDO.getTotalFileSize();
        }
        return (fileCount + dbFileCount) > uploadFileLimitProperties.getTenantFileTotalNumber() || (fileBytes + dbFileBytes) > uploadFileLimitProperties.getTenantFileTotalBytes();
    }
}
