package org.example.musk.plugin.web.upload.controller.upload;

import jakarta.annotation.Resource;
import org.example.musk.common.exception.BusinessException;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.plugin.web.upload.controller.upload.vo.UploadResVO;
import org.example.musk.plugin.web.upload.service.upload.UploadService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.example.musk.common.exception.BusinessPageExceptionEnum.UPLOAD_FILE_REQUEST_IS_EMPTY;


@RequestMapping(value = "/api/upload")
@Controller
@RestController
public class UploadFileController {

    /**
     * 图像上传服务
     */
    @Resource
    private UploadService imageUploadService;

    /**
     * 上传文件
     *
     * @param file 文件
     * @return {@link CommonResult }<{@link List }<{@link UploadResVO }>>
     */
    @PostMapping(value = "/uploadFile")
    public CommonResult<UploadResVO> uploadFile(@RequestParam(value = "file") MultipartFile file) {
        if (null == file) {
            throw new BusinessException(UPLOAD_FILE_REQUEST_IS_EMPTY);
        }
        return CommonResult.success(new UploadResVO(imageUploadService.save(file)));
    }
}
