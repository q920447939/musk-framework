package org.example.musk.plugin.web.upload.limit.member;


import cn.hutool.core.collection.CollUtil;
import jakarta.annotation.Resource;
import org.example.musk.plugin.web.upload.config.UploadFileLimitProperties;
import org.example.musk.plugin.web.upload.entity.uploadFileMemberTotalStatistics.UploadFileMemberTotalStatisticsDO;
import org.example.musk.plugin.web.upload.service.uploadFileMemberTotalStatistics.UploadFileMemberTotalStatisticsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultTenantMemberUploadFileLimit implements TenantMemberUploadFileLimit {

    @Resource
    private UploadFileMemberTotalStatisticsService uploadFileMemberTotalStatisticsService;

    @Resource
    private UploadFileLimitProperties uploadFileLimitProperties;


    @Override
    public boolean fileExceedLimit(int fileCount, long fileBytes, int memberId) {
        List<UploadFileMemberTotalStatisticsDO> uploadFileMemberTotalStatisticsDOList = uploadFileMemberTotalStatisticsService.listUploadFileMemberTotalStatistics(memberId);
        long dbFileCount = 0;
        double dbFileBytes = 0;
        if (CollUtil.isNotEmpty(uploadFileMemberTotalStatisticsDOList)) {
            for (UploadFileMemberTotalStatisticsDO uploadFileMemberTotalStatisticsDO : uploadFileMemberTotalStatisticsDOList) {
                dbFileCount += uploadFileMemberTotalStatisticsDO.getTotalFileCount();
                dbFileBytes = uploadFileMemberTotalStatisticsDO.getTotalFileSize();
            }
        }
        return (fileCount + dbFileCount) > uploadFileLimitProperties.getTenantMemberFileTotalNumber() || (fileBytes + dbFileBytes) > uploadFileLimitProperties.getTenantMemberFileTotalBytes();
    }
}
