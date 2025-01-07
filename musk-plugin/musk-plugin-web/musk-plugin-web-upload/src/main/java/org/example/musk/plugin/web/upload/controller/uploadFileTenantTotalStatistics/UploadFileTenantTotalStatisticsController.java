package org.example.musk.plugin.web.upload.controller.uploadFileTenantTotalStatistics;

import jakarta.annotation.Resource;
import jakarta.validation.*;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.common.util.commonResult.CommonResultUtils;
import org.example.musk.common.util.object.BeanUtils;
import org.example.musk.plugin.web.upload.controller.uploadFileTenantTotalStatistics.vo.UploadFileTenantTotalStatisticsAddReqVO;
import org.example.musk.plugin.web.upload.controller.uploadFileTenantTotalStatistics.vo.UploadFileTenantTotalStatisticsPageReqVO;
import org.example.musk.plugin.web.upload.controller.uploadFileTenantTotalStatistics.vo.UploadFileTenantTotalStatisticsRespVO;
import org.example.musk.plugin.web.upload.controller.uploadFileTenantTotalStatistics.vo.UploadFileTenantTotalStatisticsUpdateReqVO;
import org.example.musk.plugin.web.upload.entity.uploadFileTenantTotalStatistics.UploadFileTenantTotalStatisticsDO;
import org.example.musk.plugin.web.upload.entity.uploadFileTenantTotalStatistics.bo.UploadFileTenantTotalStatisticsPageReqBO;
import org.example.musk.plugin.web.upload.service.uploadFileTenantTotalStatistics.UploadFileTenantTotalStatisticsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import static org.example.musk.common.pojo.CommonResult.success;

/**
 *  Controller
 *
 * @author 代码生成器
 */
@RestController
@RequestMapping("/api/uploadFileTenantTotalStatistics")
@Validated
@Slf4j
public class UploadFileTenantTotalStatisticsController {

    @Resource
    private UploadFileTenantTotalStatisticsService uploadFileTenantTotalStatisticsService;

    @PostMapping("/create")
    public CommonResult<Integer> create(@Valid @RequestBody UploadFileTenantTotalStatisticsAddReqVO createReqVO) {
        return success(uploadFileTenantTotalStatisticsService.createUploadFileTenantTotalStatistics(BeanUtils.toBean(createReqVO, UploadFileTenantTotalStatisticsDO.class)));
    }
    @PutMapping("/updateById")
    public CommonResult<Boolean> update(@Valid @RequestBody UploadFileTenantTotalStatisticsUpdateReqVO updateReqVO) {
        uploadFileTenantTotalStatisticsService.updateUploadFileTenantTotalStatisticsById(updateReqVO.getId(), BeanUtils.toBean(updateReqVO, UploadFileTenantTotalStatisticsDO.class));
        return success(true);
    }
    @DeleteMapping("/deleteById")
    public CommonResult<Boolean> delete(@RequestParam("id") Integer id) {
        uploadFileTenantTotalStatisticsService.deleteUploadFileTenantTotalStatistics(id);
        return success(true);
    }
    @GetMapping("/get")
    public CommonResult<UploadFileTenantTotalStatisticsRespVO> get(@RequestParam("id") Integer id) {
        UploadFileTenantTotalStatisticsDO uploadFileTenantTotalStatistics = uploadFileTenantTotalStatisticsService.getUploadFileTenantTotalStatisticsTotalFileSize(id);
        return CommonResultUtils.wrapEmptyObjResult(uploadFileTenantTotalStatistics, () ->{
            return BeanUtils.toBean(uploadFileTenantTotalStatistics, UploadFileTenantTotalStatisticsRespVO.class);
        });
    }
    @GetMapping("/page")
    public CommonResult<PageResult<UploadFileTenantTotalStatisticsRespVO>> page(@Valid UploadFileTenantTotalStatisticsPageReqVO pageReqVO) {
        PageResult<UploadFileTenantTotalStatisticsDO> pageResult = uploadFileTenantTotalStatisticsService.getUploadFileTenantTotalStatisticsPage(BeanUtils.toBean(pageReqVO, UploadFileTenantTotalStatisticsPageReqBO.class));
        return CommonResultUtils.wrapEmptyPageResult(pageResult, () -> BeanUtils.toBean(pageResult, UploadFileTenantTotalStatisticsRespVO.class));
    }

}
