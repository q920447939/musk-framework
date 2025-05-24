package org.example.musk.functions.resource.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.functions.resource.entity.SystemResourceDO;
import org.example.musk.functions.resource.service.ResourceUploadService;
import org.example.musk.functions.resource.vo.ResourceRespVO;
import org.example.musk.functions.resource.vo.ResourceUploadRespVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.musk.common.pojo.CommonResult.success;

/**
 * 资源上传控制器
 *
 * @author musk-functions-resource
 */
@RestController
@RequestMapping("/api/resource")
@Validated
@Slf4j
public class ResourceUploadController {

    @Resource
    private ResourceUploadService resourceUploadService;

    /**
     * 上传单个文件
     *
     * @param file        文件
     * @param categoryId  分类ID
     * @param tags        标签
     * @param description 描述
     * @return 上传结果
     */
    @PostMapping("/upload")
    public CommonResult<ResourceUploadRespVO> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "tags", required = false) String tags,
            @RequestParam(value = "description", required = false) String description) {
        // 上传文件
        SystemResourceDO resource = resourceUploadService.uploadFile(file, categoryId, tags, description);
        // 转换为响应VO
        ResourceUploadRespVO respVO = new ResourceUploadRespVO();
        respVO.setResourceId(resource.getId());
        respVO.setResourceName(resource.getResourceName());
        respVO.setResourceType(resource.getResourceType());
        respVO.setFileType(resource.getFileType());
        respVO.setFileSize(resource.getFileSize());
        respVO.setAccessUrl(resource.getAccessUrl());
        return success(respVO);
    }

    /**
     * 批量上传文件
     *
     * @param files       文件列表
     * @param categoryId  分类ID
     * @param tags        标签
     * @param description 描述
     * @return 上传结果列表
     */
    @PostMapping("/batch-upload")
    public CommonResult<List<ResourceUploadRespVO>> batchUploadFiles(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "tags", required = false) String tags,
            @RequestParam(value = "description", required = false) String description) {
        // 批量上传文件
        List<SystemResourceDO> resources = resourceUploadService.batchUploadFiles(files, categoryId, tags, description);
        // 转换为响应VO
        List<ResourceUploadRespVO> respVOs = resources.stream().map(resource -> {
            ResourceUploadRespVO respVO = new ResourceUploadRespVO();
            respVO.setResourceId(resource.getId());
            respVO.setResourceName(resource.getResourceName());
            respVO.setResourceType(resource.getResourceType());
            respVO.setFileType(resource.getFileType());
            respVO.setFileSize(resource.getFileSize());
            respVO.setAccessUrl(resource.getAccessUrl());
            return respVO;
        }).collect(Collectors.toList());
        return success(respVOs);
    }

    /**
     * 上传文件分片
     *
     * @param file       文件分片
     * @param fileName   文件名
     * @param chunkIndex 分片索引
     * @param chunks     总分片数
     * @param md5        文件MD5
     * @return 上传结果
     */
    @PostMapping("/chunk-upload")
    public CommonResult<Boolean> uploadChunk(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileName") String fileName,
            @RequestParam("chunkIndex") Integer chunkIndex,
            @RequestParam("chunks") Integer chunks,
            @RequestParam("md5") String md5) {
        // 上传文件分片
        boolean result = resourceUploadService.uploadChunk(file, fileName, chunkIndex, chunks, md5);
        return success(result);
    }

    /**
     * 合并文件分片
     *
     * @param fileName    文件名
     * @param chunks      总分片数
     * @param md5         文件MD5
     * @param categoryId  分类ID
     * @param tags        标签
     * @param description 描述
     * @return 合并结果
     */
    @PostMapping("/merge-chunks")
    public CommonResult<ResourceUploadRespVO> mergeChunks(
            @RequestParam("fileName") String fileName,
            @RequestParam("chunks") Integer chunks,
            @RequestParam("md5") String md5,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "tags", required = false) String tags,
            @RequestParam(value = "description", required = false) String description) {
        // 合并文件分片
        SystemResourceDO resource = resourceUploadService.mergeChunks(fileName, chunks, md5, categoryId, tags, description);
        // 转换为响应VO
        ResourceUploadRespVO respVO = new ResourceUploadRespVO();
        respVO.setResourceId(resource.getId());
        respVO.setResourceName(resource.getResourceName());
        respVO.setResourceType(resource.getResourceType());
        respVO.setFileType(resource.getFileType());
        respVO.setFileSize(resource.getFileSize());
        respVO.setAccessUrl(resource.getAccessUrl());
        return success(respVO);
    }
}
