package org.example.musk.plugin.web.upload.service.upload;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.plugin.web.upload.config.UploadProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;


@Service("imageUploadService")
@Slf4j
public class ImageUploadServiceImpl implements UploadService {

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
