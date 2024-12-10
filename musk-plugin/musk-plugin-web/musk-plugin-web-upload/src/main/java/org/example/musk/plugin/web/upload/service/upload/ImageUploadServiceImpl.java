package org.example.musk.plugin.web.upload.service.upload;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.common.threadVirtual.ThreadVirtualUtils;
import org.example.musk.plugin.service.dynamic.source.anno.PluginDynamicSource;
import org.example.musk.plugin.web.upload.config.UploadProperties;
import org.example.musk.plugin.web.upload.enums.FileTypeEnums;
import org.example.musk.plugin.web.upload.service.uploadFileMemberByDayStatistics.UploadFileMemberByDayStatisticsService;
import org.example.musk.plugin.web.upload.service.uploadFileMemberTotalStatistics.UploadFileMemberTotalStatisticsService;
import org.example.musk.plugin.web.upload.service.uploadFileTenantTotalStatistics.UploadFileTenantTotalStatisticsService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;


@Service("imageUploadService")
@Slf4j
@PluginDynamicSource(group = "plugin", ds = "upload")
public class ImageUploadServiceImpl implements UploadService {

    @Resource
    private UploadFileTenantTotalStatisticsService uploadFileTenantTotalStatisticsService;
    @Resource
    private UploadFileMemberByDayStatisticsService uploadFileMemberByDayStatisticsService;
    @Resource
    private UploadFileMemberTotalStatisticsService uploadFileMemberTotalStatisticsService;

    private final UploadProperties uploadProperties;

    public ImageUploadServiceImpl(UploadProperties uploadProperties) {
        this.uploadProperties = uploadProperties;
    }

    private String getTenantBasePath(){
        return uploadProperties.getBasePath() + ThreadLocalTenantContext.getTenantId() + File.separator;
    }

    @Override
    public String save(MultipartFile file) {
        if (null == file) {
            return StrUtil.EMPTY;
        }
        File targetFile = null;
        String basePath = getTenantBasePath();
        try {
            //获取源文件
            String filePath = basePath;
            String filename = file.getOriginalFilename();//获取图片名
            String[] names = filename.split("\\.");//获取后缀格式
            String uploadFileName = UUID.randomUUID().toString() + "." + names[names.length - 1];//生成新图片
            targetFile = new File(filePath, uploadFileName);//目标文件
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            //传图片一步到位
            file.transferTo(targetFile);
            File finalTargetFile = targetFile;
            ThreadVirtualUtils.run(()->{
                int fileSize = FileUtil.readBytes(finalTargetFile).length;
                int times = 1;
                uploadFileTenantTotalStatisticsService.saveUploadFileTenantTotalStatistics(ThreadLocalTenantContext.getTenantId(), FileTypeEnums.IMAGE, times, fileSize);
                uploadFileMemberByDayStatisticsService.saveUploadFileMemberByDayStatistics(ThreadLocalTenantContext.getMemberId(), LocalDateTimeUtil.now().toLocalDate(), FileTypeEnums.IMAGE,times, fileSize);
                uploadFileMemberTotalStatisticsService.saveUploadFileMemberTotalStatistics(ThreadLocalTenantContext.getMemberId(),FileTypeEnums.IMAGE,times, fileSize);
            });
            return getDomainFilePath(uploadFileName);
        } catch (Exception e) {
            log.error("上传文件失败,filename={}",file.getOriginalFilename(), e);
            if (null != targetFile) {
                FileUtil.del(targetFile);
            }
            return StrUtil.EMPTY;
        }
    }

   // @Override
    public String getServerFilePath(String filePath) {
        return (uploadProperties.getDomain() + filePath).replaceAll("\\\\", "/");
    }

    public String getDomainFilePath(String filePath) {
        return (uploadProperties.getDomainProjectPath() + ThreadLocalTenantContext.getTenantId() + "/"+ filePath).replaceAll("\\\\", "/").replaceAll("//","/");
    }
}
