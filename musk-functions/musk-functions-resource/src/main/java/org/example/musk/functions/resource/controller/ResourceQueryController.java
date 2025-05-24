package org.example.musk.functions.resource.controller;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.functions.resource.entity.SystemResourceCategoryDO;
import org.example.musk.functions.resource.entity.SystemResourceDO;
import org.example.musk.functions.resource.service.ResourceCategoryService;
import org.example.musk.functions.resource.service.ResourceQueryService;
import org.example.musk.functions.resource.service.ResourceSecurityService;
import org.example.musk.functions.resource.vo.ResourcePageReqVO;
import org.example.musk.functions.resource.vo.ResourceRespVO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.example.musk.common.pojo.CommonResult.success;

/**
 * 资源查询控制器
 *
 * @author musk-functions-resource
 */
@RestController
@RequestMapping("/api/resource")
@Validated
@Slf4j
public class ResourceQueryController {

    @Resource
    private ResourceQueryService resourceQueryService;

    @Resource
    private ResourceCategoryService resourceCategoryService;

    @Resource
    private ResourceSecurityService resourceSecurityService;

    /**
     * 获取资源信息
     *
     * @param id 资源ID
     * @return 资源信息
     */
    @GetMapping("/get/{id}")
    public CommonResult<ResourceRespVO> getResource(@PathVariable("id") Integer id) {
        SystemResourceDO resource = resourceQueryService.getResource(id);
        ResourceRespVO respVO = BeanUtil.copyProperties(resource, ResourceRespVO.class);
        // 设置分类名称
        if (resource.getCategoryId() != null) {
            SystemResourceCategoryDO category = resourceCategoryService.getCategory(resource.getCategoryId());
            if (category != null) {
                respVO.setCategoryName(category.getCategoryName());
            }
        }
        return success(respVO);
    }

    /**
     * 分页查询资源
     *
     * @param pageReqVO 查询条件
     * @return 资源分页结果
     */
    @GetMapping("/page")
    public CommonResult<PageResult<ResourceRespVO>> pageResources(ResourcePageReqVO pageReqVO) {
        PageResult<SystemResourceDO> pageResult = resourceQueryService.pageResources(pageReqVO);
        // 获取所有分类
        List<SystemResourceCategoryDO> categories = resourceCategoryService.listCategories();
        Map<Integer, SystemResourceCategoryDO> categoryMap = categories.stream()
                .collect(Collectors.toMap(SystemResourceCategoryDO::getId, Function.identity()));
        // 转换为响应VO
        List<ResourceRespVO> respVOs = pageResult.getList().stream().map(resource -> {
            ResourceRespVO respVO = BeanUtil.copyProperties(resource, ResourceRespVO.class);
            // 设置分类名称
            if (resource.getCategoryId() != null) {
                SystemResourceCategoryDO category = categoryMap.get(resource.getCategoryId());
                if (category != null) {
                    respVO.setCategoryName(category.getCategoryName());
                }
            }
            return respVO;
        }).collect(Collectors.toList());
        return success(new PageResult<>(respVOs, pageResult.getTotal()));
    }

    /**
     * 根据分类获取资源列表
     *
     * @param categoryId 分类ID
     * @return 资源列表
     */
    @GetMapping("/list-by-category")
    public CommonResult<List<ResourceRespVO>> getResourcesByCategory(@RequestParam("categoryId") Integer categoryId) {
        List<SystemResourceDO> resources = resourceQueryService.getResourcesByCategory(categoryId);
        // 转换为响应VO
        List<ResourceRespVO> respVOs = resources.stream()
                .map(resource -> BeanUtil.copyProperties(resource, ResourceRespVO.class))
                .collect(Collectors.toList());
        return success(respVOs);
    }

    /**
     * 根据标签获取资源列表
     *
     * @param tag 标签
     * @return 资源列表
     */
    @GetMapping("/list-by-tag")
    public CommonResult<List<ResourceRespVO>> getResourcesByTag(@RequestParam("tag") String tag) {
        List<SystemResourceDO> resources = resourceQueryService.getResourcesByTag(tag);
        // 转换为响应VO
        List<ResourceRespVO> respVOs = resources.stream()
                .map(resource -> BeanUtil.copyProperties(resource, ResourceRespVO.class))
                .collect(Collectors.toList());
        return success(respVOs);
    }

    /**
     * 根据资源类型获取资源列表
     *
     * @param resourceType 资源类型
     * @return 资源列表
     */
    @GetMapping("/list-by-type")
    public CommonResult<List<ResourceRespVO>> getResourcesByType(@RequestParam("resourceType") Integer resourceType) {
        List<SystemResourceDO> resources = resourceQueryService.getResourcesByType(resourceType);
        // 转换为响应VO
        List<ResourceRespVO> respVOs = resources.stream()
                .map(resource -> BeanUtil.copyProperties(resource, ResourceRespVO.class))
                .collect(Collectors.toList());
        return success(respVOs);
    }

    /**
     * 搜索资源
     *
     * @param keyword 关键字
     * @return 资源列表
     */
    @GetMapping("/search")
    public CommonResult<List<ResourceRespVO>> searchResources(@RequestParam("keyword") String keyword) {
        List<SystemResourceDO> resources = resourceQueryService.searchResources(keyword);
        // 转换为响应VO
        List<ResourceRespVO> respVOs = resources.stream()
                .map(resource -> BeanUtil.copyProperties(resource, ResourceRespVO.class))
                .collect(Collectors.toList());
        return success(respVOs);
    }

    /**
     * 下载资源
     *
     * @param id      资源ID
     * @param request HTTP请求
     * @return 资源内容
     */
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadResource(
            @PathVariable("id") Integer id,
            HttpServletRequest request) throws IOException {
        // 检查Referer
        String referer = request.getHeader("Referer");
        if (!resourceSecurityService.checkReferer(referer)) {
            return ResponseEntity.status(403).build();
        }

        // 获取资源信息
        SystemResourceDO resource = resourceQueryService.getResource(id);

        // 获取资源内容
        try (InputStream inputStream = resourceQueryService.getResourceContent(id)) {
            byte[] bytes = inputStream.readAllBytes();

            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentLength(bytes.length);
            String encodedFileName = URLEncoder.encode(resource.getResourceName(), StandardCharsets.UTF_8.toString())
                    .replaceAll("\\+", "%20");
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(bytes);
        }
    }

    /**
     * 预览资源
     *
     * @param id      资源ID
     * @param request HTTP请求
     * @return 资源内容
     */
    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewResource(
            @PathVariable("id") Integer id,
            HttpServletRequest request) throws IOException {
        // 检查Referer
        String referer = request.getHeader("Referer");
        if (!resourceSecurityService.checkReferer(referer)) {
            return ResponseEntity.status(403).build();
        }

        // 获取资源信息
        SystemResourceDO resource = resourceQueryService.getResource(id);

        // 获取资源内容
        try (InputStream inputStream = resourceQueryService.getResourceContent(id)) {
            byte[] bytes = inputStream.readAllBytes();

            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            MediaType mediaType = getMediaType(resource.getFileType());
            headers.setContentType(mediaType);
            headers.setContentLength(bytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(bytes);
        }
    }

    /**
     * 获取资源访问URL
     *
     * @param id            资源ID
     * @param expireSeconds 过期时间（秒）
     * @return 访问URL
     */
    @GetMapping("/access-url/{id}")
    public CommonResult<String> getResourceAccessUrl(
            @PathVariable("id") Integer id,
            @RequestParam(value = "expireSeconds", required = false, defaultValue = "3600") Integer expireSeconds) {
        String accessUrl = resourceQueryService.getResourceAccessUrl(id, expireSeconds);
        return success(accessUrl);
    }

    /**
     * 根据文件类型获取MediaType
     *
     * @param fileType 文件类型
     * @return MediaType
     */
    private MediaType getMediaType(String fileType) {
        if (fileType == null) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }

        switch (fileType.toLowerCase()) {
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG;
            case "png":
                return MediaType.IMAGE_PNG;
            case "gif":
                return MediaType.IMAGE_GIF;
            case "pdf":
                return MediaType.APPLICATION_PDF;
            case "html":
                return MediaType.TEXT_HTML;
            case "txt":
                return MediaType.TEXT_PLAIN;
            case "xml":
                return MediaType.APPLICATION_XML;
            case "json":
                return MediaType.APPLICATION_JSON;
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
