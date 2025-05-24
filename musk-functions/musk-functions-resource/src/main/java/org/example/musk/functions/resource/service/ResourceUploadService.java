package org.example.musk.functions.resource.service;

import org.example.musk.functions.resource.entity.SystemResourceDO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 资源上传服务接口
 *
 * @author musk-functions-resource
 */
public interface ResourceUploadService {

    /**
     * 上传单个文件
     *
     * @param file        文件
     * @param categoryId  分类ID
     * @param tags        标签
     * @param description 描述
     * @return 资源信息
     */
    SystemResourceDO uploadFile(MultipartFile file, Integer categoryId, String tags, String description);

    /**
     * 批量上传文件
     *
     * @param files       文件列表
     * @param categoryId  分类ID
     * @param tags        标签
     * @param description 描述
     * @return 资源信息列表
     */
    List<SystemResourceDO> batchUploadFiles(List<MultipartFile> files, Integer categoryId, String tags, String description);

    /**
     * 上传文件分片
     *
     * @param file      文件分片
     * @param fileName  文件名
     * @param chunkIndex 分片索引
     * @param chunks    总分片数
     * @param md5       文件MD5
     * @return 是否上传成功
     */
    boolean uploadChunk(MultipartFile file, String fileName, Integer chunkIndex, Integer chunks, String md5);

    /**
     * 合并文件分片
     *
     * @param fileName    文件名
     * @param chunks      总分片数
     * @param md5         文件MD5
     * @param categoryId  分类ID
     * @param tags        标签
     * @param description 描述
     * @return 资源信息
     */
    SystemResourceDO mergeChunks(String fileName, Integer chunks, String md5, Integer categoryId, String tags, String description);
}
