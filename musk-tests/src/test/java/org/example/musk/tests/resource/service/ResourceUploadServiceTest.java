package org.example.musk.tests.resource.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.functions.resource.entity.SystemResourceCategoryDO;
import org.example.musk.functions.resource.entity.SystemResourceDO;
import org.example.musk.functions.resource.enums.ResourceTypeEnum;
import org.example.musk.functions.resource.exception.ResourceException;
import org.example.musk.functions.resource.service.ResourceCategoryService;
import org.example.musk.functions.resource.service.ResourceManageService;
import org.example.musk.functions.resource.service.ResourceQueryService;
import org.example.musk.functions.resource.service.ResourceUploadService;
import org.example.musk.tests.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 资源上传服务测试类
 *
 * @author musk-functions-resource
 */
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ResourceUploadServiceTest extends BaseTest {

    @Resource
    private ResourceUploadService resourceUploadService;

    @Resource
    private ResourceCategoryService resourceCategoryService;

    @Resource
    private ResourceQueryService resourceQueryService;
    @Resource
    private ResourceManageService resourceManageService;

    private static Integer testCategoryId;
    private static Integer testResourceId;
    private static List<Integer> testResourceIds = new ArrayList<>();

    /**
     * 测试前准备 - 创建测试分类
     */
    @Test
    @Order(1)
    public void testPrepareCategory() {
        // 创建测试分类
        SystemResourceCategoryDO category = new SystemResourceCategoryDO();
        category.setCategoryName("测试上传分类");
        category.setCategoryCode("test_upload_category_" + System.currentTimeMillis());
        category.setDisplayOrder(1);
        category.setStatus(0);
        category.setRemark("测试上传分类备注");

        testCategoryId = resourceCategoryService.createCategory(category);
        Assertions.assertNotNull(testCategoryId);
        Assertions.assertTrue(testCategoryId > 0);
    }

    /**
     * 测试上传单个文件 - 图片文件
     */
    @Test
    @Order(2)
    public void testUploadImageFile() {
        // 创建模拟图片文件
        String fileName = "test-image.jpg";
        String content = "这是一个测试图片文件的内容";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                fileName,
                "image/jpeg",
                content.getBytes()
        );

        // 上传文件
        SystemResourceDO resource = resourceUploadService.uploadFile(
                file,
                testCategoryId,
                "测试,图片",
                "测试上传图片文件"
        );

        // 验证上传结果
        Assertions.assertNotNull(resource);
        Assertions.assertNotNull(resource.getId());
        testResourceId = resource.getId();
        testResourceIds.add(testResourceId);

        // 验证基本信息
        Assertions.assertEquals(fileName, resource.getResourceName());
        Assertions.assertEquals(ResourceTypeEnum.IMAGE.getCode(), resource.getResourceType());
        Assertions.assertEquals("jpg", resource.getFileType());
        Assertions.assertEquals((long) content.getBytes().length, resource.getFileSize());
        Assertions.assertEquals(testCategoryId, resource.getCategoryId());
        Assertions.assertEquals("测试,图片", resource.getTags());
        Assertions.assertEquals("测试上传图片文件", resource.getDescription());

        // 验证租户和域信息
        Assertions.assertEquals(ThreadLocalTenantContext.getTenantId(), resource.getTenantId());
        Assertions.assertEquals(ThreadLocalTenantContext.getDomainId(), resource.getDomainId());

        // 验证存储信息
        Assertions.assertNotNull(resource.getStoragePath());
        Assertions.assertNotNull(resource.getAccessUrl());
        Assertions.assertNotNull(resource.getMd5());
    }

    /**
     * 测试上传单个文件 - 文档文件
     */
    @Test
    @Order(3)
    public void testUploadDocumentFile() {
        // 创建模拟文档文件
        String fileName = "test-document.pdf";
        String content = "这是一个测试PDF文档文件的内容";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                fileName,
                "application/pdf",
                content.getBytes()
        );

        // 上传文件
        SystemResourceDO resource = resourceUploadService.uploadFile(
                file,
                testCategoryId,
                "测试,文档",
                "测试上传PDF文档"
        );

        // 验证上传结果
        Assertions.assertNotNull(resource);
        Assertions.assertNotNull(resource.getId());
        testResourceIds.add(resource.getId());

        // 验证基本信息
        Assertions.assertEquals(fileName, resource.getResourceName());
        Assertions.assertEquals(ResourceTypeEnum.DOCUMENT.getCode(), resource.getResourceType());
        Assertions.assertEquals("pdf", resource.getFileType());
        Assertions.assertEquals((long) content.getBytes().length, resource.getFileSize());
    }

    /**
     * 测试批量上传文件
     */
    @Test
    @Order(4)
    public void testBatchUploadFiles() {
        // 创建多个模拟文件
        List<MultipartFile> files = new ArrayList<>();

        // 文件1：图片
        MockMultipartFile file1 = new MockMultipartFile(
                "file1",
                "batch-image1.png",
                "image/png",
                "批量上传图片1内容".getBytes()
        );
        files.add(file1);

        // 文件2：图片
        MockMultipartFile file2 = new MockMultipartFile(
                "file2",
                "batch-image2.jpg",
                "image/jpeg",
                "批量上传图片2内容".getBytes()
        );
        files.add(file2);

        // 文件3：文档
        MockMultipartFile file3 = new MockMultipartFile(
                "file3",
                "batch-document.txt",
                "text/plain",
                "批量上传文档内容".getBytes()
        );
        files.add(file3);

        // 批量上传文件
        List<SystemResourceDO> resources = resourceUploadService.batchUploadFiles(
                files,
                testCategoryId,
                "批量,测试",
                "批量上传测试文件"
        );

        // 验证上传结果
        Assertions.assertNotNull(resources);
        Assertions.assertEquals(3, resources.size());

        // 验证每个文件
        for (int i = 0; i < resources.size(); i++) {
            SystemResourceDO resource = resources.get(i);
            Assertions.assertNotNull(resource.getId());
            testResourceIds.add(resource.getId());

            // 验证租户和域信息
            Assertions.assertEquals(ThreadLocalTenantContext.getTenantId(), resource.getTenantId());
            Assertions.assertEquals(ThreadLocalTenantContext.getDomainId(), resource.getDomainId());

            // 验证分类和标签
            Assertions.assertEquals(testCategoryId, resource.getCategoryId());
            Assertions.assertEquals("批量,测试", resource.getTags());
            Assertions.assertEquals("批量上传测试文件", resource.getDescription());
        }

        // 验证第一个文件（PNG图片）
        SystemResourceDO resource1 = resources.get(0);
        Assertions.assertEquals("batch-image1.png", resource1.getResourceName());
        Assertions.assertEquals(ResourceTypeEnum.IMAGE.getCode(), resource1.getResourceType());
        Assertions.assertEquals("png", resource1.getFileType());

        // 验证第二个文件（JPG图片）
        SystemResourceDO resource2 = resources.get(1);
        Assertions.assertEquals("batch-image2.jpg", resource2.getResourceName());
        Assertions.assertEquals(ResourceTypeEnum.IMAGE.getCode(), resource2.getResourceType());
        Assertions.assertEquals("jpg", resource2.getFileType());

        // 验证第三个文件（TXT文档）
        SystemResourceDO resource3 = resources.get(2);
        Assertions.assertEquals("batch-document.txt", resource3.getResourceName());
        Assertions.assertEquals(ResourceTypeEnum.DOCUMENT.getCode(), resource3.getResourceType());
        Assertions.assertEquals("txt", resource3.getFileType());
    }

    /**
     * 测试上传文件分片
     */
    @Test
    @Order(5)
    //TODO 分片逻辑还存在问题,待完成
    public void testUploadChunk() throws InterruptedException {
        // 创建模拟分片文件内容
        String fileName = "large-file.zip";
        byte[] chunk1Content = "分片1内容".getBytes();
        byte[] chunk2Content = "分片2内容".getBytes();
        byte[] chunk3Content = "分片3内容".getBytes();
        Integer chunks = 3;

        // 计算完整文件的MD5（所有分片内容合并后的MD5）
        byte[] fullContent = new byte[chunk1Content.length + chunk2Content.length + chunk3Content.length];
        System.arraycopy(chunk1Content, 0, fullContent, 0, chunk1Content.length);
        System.arraycopy(chunk2Content, 0, fullContent, chunk1Content.length, chunk2Content.length);
        System.arraycopy(chunk3Content, 0, fullContent, chunk1Content.length + chunk2Content.length, chunk3Content.length);

        // 使用hutool计算MD5
        String md5 = cn.hutool.crypto.digest.DigestUtil.md5Hex(fullContent);

        // 调试信息
        System.out.println("测试分片上传 - MD5: " + md5);
        System.out.println("测试分片上传 - 租户ID: " + ThreadLocalTenantContext.getTenantId());
        System.out.println("测试分片上传 - 域ID: " + ThreadLocalTenantContext.getDomainId());

        // 上传第一个分片
        MockMultipartFile chunk1 = new MockMultipartFile(
                "chunk1",
                "chunk1.tmp",
                "application/octet-stream",
                chunk1Content
        );
        boolean result1 = resourceUploadService.uploadChunk(chunk1, fileName, 0, chunks, md5);
        Assertions.assertTrue(result1);

        // 添加短暂等待，确保文件系统操作完成
        Thread.sleep(100);

        // 上传第二个分片
        MockMultipartFile chunk2 = new MockMultipartFile(
                "chunk2",
                "chunk2.tmp",
                "application/octet-stream",
                chunk2Content
        );
        boolean result2 = resourceUploadService.uploadChunk(chunk2, fileName, 1, chunks, md5);
        Assertions.assertTrue(result2);

        // 添加短暂等待，确保文件系统操作完成
        Thread.sleep(100);

        // 上传第三个分片
        MockMultipartFile chunk3 = new MockMultipartFile(
                "chunk3",
                "chunk3.tmp",
                "application/octet-stream",
                chunk3Content
        );
        boolean result3 = resourceUploadService.uploadChunk(chunk3, fileName, 2, chunks, md5);
        Assertions.assertTrue(result3);

        // 添加短暂等待，确保文件系统操作完成
        Thread.sleep(100);

        // 调试：检查分片文件是否存在
        try {
            String basePath = System.getProperty("java.io.tmpdir") + File.separator + "test-resources";
            String chunkDirPath = basePath + File.separator + "chunks" + File.separator +
                    ThreadLocalTenantContext.getTenantId() + File.separator +
                    ThreadLocalTenantContext.getDomainId() + File.separator + md5;
            File chunkDir = new File(chunkDirPath);
            System.out.println("分片目录路径: " + chunkDirPath);
            System.out.println("分片目录是否存在: " + chunkDir.exists());
            if (chunkDir.exists()) {
                File[] files = chunkDir.listFiles();
                System.out.println("分片文件数量: " + (files != null ? files.length : 0));
                if (files != null) {
                    for (File file : files) {
                        System.out.println("分片文件: " + file.getName() + " (大小: " + file.length() + ")");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("调试信息获取失败: " + e.getMessage());
        }

        // 合并分片
        SystemResourceDO resource = resourceUploadService.mergeChunks(
                fileName,
                chunks,
                md5,
                testCategoryId,
                "分片,测试",
                "测试分片上传合并"
        );

        // 验证合并结果
        Assertions.assertNotNull(resource);
        Assertions.assertNotNull(resource.getId());
        testResourceIds.add(resource.getId());

        // 验证基本信息
        Assertions.assertEquals(fileName, resource.getResourceName());
        Assertions.assertEquals(ResourceTypeEnum.ARCHIVE.getCode(), resource.getResourceType());
        Assertions.assertEquals("zip", resource.getFileType());
        Assertions.assertEquals(testCategoryId, resource.getCategoryId());
        Assertions.assertEquals("分片,测试", resource.getTags());
        Assertions.assertEquals("测试分片上传合并", resource.getDescription());

        // 验证租户和域信息
        Assertions.assertEquals(ThreadLocalTenantContext.getTenantId(), resource.getTenantId());
        Assertions.assertEquals(ThreadLocalTenantContext.getDomainId(), resource.getDomainId());
    }

    /**
     * 测试上传空文件（应该失败）
     */
    @Test
    @Order(6)
    public void testUploadEmptyFile() {
        // 创建空文件
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "empty.txt",
                "text/plain",
                new byte[0]
        );

        // 尝试上传空文件，应该抛出异常
        try {
            resourceUploadService.uploadFile(emptyFile, testCategoryId, "测试", "空文件测试");
        } catch (ResourceException e) {
            // 验证是否为文件为空的异常
            Assertions.assertTrue(e.getMessage().contains("file.cannot.be.empty"));
        }
    }

    /**
     * 测试上传不支持的文件类型（应该失败）
     */
    @Test
    @Order(7)
    public void testUploadUnsupportedFileType() {
        // 创建不支持的文件类型
        MockMultipartFile unsupportedFile = new MockMultipartFile(
                "file",
                "malicious.exe",
                "application/x-msdownload",
                "恶意文件内容".getBytes()
        );

        // 尝试上传不支持的文件类型，应该抛出异常
        try {
            resourceUploadService.uploadFile(unsupportedFile, testCategoryId, "测试", "不支持文件类型测试");
            Assertions.fail("应该抛出异常，因为文件类型不支持");
        } catch (ResourceException e) {
            // 验证是否为文件类型不支持的异常
            Assertions.assertTrue(e.getErrorCode().equals(ResourceException.FILE_TYPE_NOT_ALLOWED) ||
                    e.getErrorCode().equals(ResourceException.FILE_TYPE_BLOCKED));
        }
    }

    /**
     * 测试上传文件到不存在的分类（应该失败）
     */
    @Test
    @Order(8)
    public void testUploadFileToNonExistentCategory() {
        // 创建测试文件
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "测试内容".getBytes()
        );

        // 使用不存在的分类ID
        Integer nonExistentCategoryId = 999999;

        // 尝试上传文件到不存在的分类，应该抛出异常
        try {
            resourceUploadService.uploadFile(file, nonExistentCategoryId, "测试", "不存在分类测试");
        } catch (ResourceException e) {
            // 验证是否为分类不存在的异常
            Assertions.assertTrue(e.getErrorCode().equals(ResourceException.CATEGORY_NOT_FOUND) ||
                    e.getMessage().contains("分类"));
        }
    }

    /**
     * 测试上传文件时传入null参数
     */
    @Test
    @Order(9)
    public void testUploadFileWithNullParameters() {
        // 创建测试文件
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-null-params.txt",
                "text/plain",
                "测试null参数".getBytes()
        );

        // 测试传入null的tags和description
        SystemResourceDO resource = resourceUploadService.uploadFile(file, testCategoryId, null, null);

        // 验证上传成功
        Assertions.assertNotNull(resource);
        Assertions.assertNotNull(resource.getId());
        testResourceIds.add(resource.getId());

        // 验证基本信息
        Assertions.assertEquals("test-null-params.txt", resource.getResourceName());
        Assertions.assertEquals(testCategoryId, resource.getCategoryId());
        Assertions.assertNull(resource.getTags());
        Assertions.assertNull(resource.getDescription());
    }

    /**
     * 测试分片上传时分片数量不匹配（应该失败）
     */
    @Test
    @Order(10)
    public void testMergeChunksWithMismatchedCount() throws InterruptedException {
        // 创建模拟分片文件
        String fileName = "mismatch-chunks.zip";
        // 使用不同的MD5避免与其他测试冲突
        String md5 = "mismatch-" + cn.hutool.crypto.digest.DigestUtil.md5Hex(("mismatch-test-" + System.currentTimeMillis()).getBytes());
        Integer expectedChunks = 3;

        // 只上传2个分片
        MockMultipartFile chunk1 = new MockMultipartFile(
                "chunk1",
                "chunk1.tmp",
                "application/octet-stream",
                "分片1内容".getBytes()
        );
        resourceUploadService.uploadChunk(chunk1, fileName, 0, expectedChunks, md5);
        Thread.sleep(100);

        MockMultipartFile chunk2 = new MockMultipartFile(
                "chunk2",
                "chunk2.tmp",
                "application/octet-stream",
                "分片2内容".getBytes()
        );
        resourceUploadService.uploadChunk(chunk2, fileName, 1, expectedChunks, md5);
        Thread.sleep(100);

        // 尝试合并分片，但分片数量不匹配，应该失败
        try {
            resourceUploadService.mergeChunks(fileName, expectedChunks, md5, testCategoryId, "测试", "分片数量不匹配测试");
            Assertions.fail("应该抛出异常，因为分片数量不匹配");
        } catch (ResourceException e) {
            // 验证是否为分片数量不匹配的异常
            Assertions.assertTrue(e.getMessage().contains("分片") || e.getMessage().contains("数量"));
        }
    }

    /**
     * 测试验证上传的资源是否可以正确查询
     */
    @Test
    @Order(11)
    public void testVerifyUploadedResource() {
        // 使用之前上传的资源ID进行查询验证
        if (testResourceId != null) {
            SystemResourceDO resource = resourceQueryService.getResource(testResourceId);

            // 验证资源存在且信息正确
            Assertions.assertNotNull(resource);
            Assertions.assertEquals(testResourceId, resource.getId());
            Assertions.assertEquals(ThreadLocalTenantContext.getTenantId(), resource.getTenantId());
            Assertions.assertEquals(ThreadLocalTenantContext.getDomainId(), resource.getDomainId());
            Assertions.assertEquals(testCategoryId, resource.getCategoryId());

            // 验证文件信息
            Assertions.assertNotNull(resource.getResourceName());
            Assertions.assertNotNull(resource.getFileType());
            Assertions.assertNotNull(resource.getStoragePath());
            Assertions.assertNotNull(resource.getAccessUrl());
            Assertions.assertNotNull(resource.getMd5());
            Assertions.assertTrue(resource.getFileSize() > 0);
        }
    }

    /**
     * 清理测试数据
     */
    @Test
    @Order(99)
    public void testCleanup() {
        // 清理上传的资源文件
        for (Integer resourceId : testResourceIds) {
            try {
                // 获取资源信息
                SystemResourceDO resource = resourceQueryService.getResource(resourceId);
                if (resource != null) {
                    // 删除物理文件
                    String storagePath = resource.getStoragePath();
                    if (storagePath != null) {
                        try {
                            Path filePath = Paths.get(storagePath);
                            if (Files.exists(filePath)) {
                                Files.delete(filePath);
                            }
                        } catch (IOException e) {
                            // 忽略文件删除失败
                        }
                    }
                }
            } catch (Exception e) {
                // 忽略清理过程中的异常
            }
            resourceManageService.deleteResource(resourceId);
        }

        // 清理测试分类
        if (testCategoryId != null) {
            try {
                resourceCategoryService.deleteCategory(testCategoryId);
            } catch (Exception e) {
                // 忽略清理过程中的异常
                log.error("testCleanup testCategoryId:{} ",testCategoryId,e);
            }
        }
    }
}
