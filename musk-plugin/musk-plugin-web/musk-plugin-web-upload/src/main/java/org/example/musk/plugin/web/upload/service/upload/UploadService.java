
package org.example.musk.plugin.web.upload.service.upload;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 上传服务
 *
 * @author
 * @date 2024/07/08
 */
public interface UploadService {

    /**
     * 保存
     *
     * @param file         文件
     * @return {@link List }<{@link String }>  返回的域名文件路径
     */
    String save(MultipartFile file);

    /**
     * 获得服务器文件路径
     *
     * @param filePath 文件路径
     * @return {@link String }
     */
    //String getServerFilePath(String filePath);
}
