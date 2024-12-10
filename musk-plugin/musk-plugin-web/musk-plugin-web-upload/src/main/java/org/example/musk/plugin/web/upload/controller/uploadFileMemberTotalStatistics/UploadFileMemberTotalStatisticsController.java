package org.example.musk.plugin.web.upload.controller.uploadFileMemberTotalStatistics;

import jakarta.annotation.Resource;
import jakarta.validation.*;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.common.util.commonResult.CommonResultUtils;
import org.example.musk.common.util.object.BeanUtils;
import org.example.musk.plugin.web.upload.controller.uploadFileMemberTotalStatistics.vo.UploadFileMemberTotalStatisticsAddReqVO;
import org.example.musk.plugin.web.upload.controller.uploadFileMemberTotalStatistics.vo.UploadFileMemberTotalStatisticsPageReqVO;
import org.example.musk.plugin.web.upload.controller.uploadFileMemberTotalStatistics.vo.UploadFileMemberTotalStatisticsRespVO;
import org.example.musk.plugin.web.upload.controller.uploadFileMemberTotalStatistics.vo.UploadFileMemberTotalStatisticsUpdateReqVO;
import org.example.musk.plugin.web.upload.entity.uploadFileMemberTotalStatistics.UploadFileMemberTotalStatisticsDO;
import org.example.musk.plugin.web.upload.entity.uploadFileMemberTotalStatistics.bo.UploadFileMemberTotalStatisticsPageReqBO;
import org.example.musk.plugin.web.upload.service.uploadFileMemberTotalStatistics.UploadFileMemberTotalStatisticsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import static org.example.musk.common.pojo.CommonResult.success;

/**
 *  Controller
 *
 * @author 代码生成器
 */
@RestController
@RequestMapping("/api/uploadFileMemberTotalStatistics")
@Validated
@Slf4j
public class UploadFileMemberTotalStatisticsController {

    @Resource
    private UploadFileMemberTotalStatisticsService uploadFileMemberTotalStatisticsService;

    @PostMapping("/create")
    public CommonResult<Integer> create(@Valid @RequestBody UploadFileMemberTotalStatisticsAddReqVO createReqVO) {
        return success(uploadFileMemberTotalStatisticsService.createUploadFileMemberTotalStatistics(BeanUtils.toBean(createReqVO, UploadFileMemberTotalStatisticsDO.class)));
    }
    @PutMapping("/updateById")
    public CommonResult<Boolean> update(@Valid @RequestBody UploadFileMemberTotalStatisticsUpdateReqVO updateReqVO) {
        uploadFileMemberTotalStatisticsService.updateUploadFileMemberTotalStatisticsById(updateReqVO.getId(), BeanUtils.toBean(updateReqVO, UploadFileMemberTotalStatisticsDO.class));
        return success(true);
    }
    @DeleteMapping("/deleteById")
    public CommonResult<Boolean> delete(@RequestParam("id") Integer id) {
        uploadFileMemberTotalStatisticsService.deleteUploadFileMemberTotalStatistics(id);
        return success(true);
    }
    @GetMapping("/get")
    public CommonResult<UploadFileMemberTotalStatisticsRespVO> get(@RequestParam("id") Integer id) {
        UploadFileMemberTotalStatisticsDO uploadFileMemberTotalStatistics = uploadFileMemberTotalStatisticsService.getUploadFileMemberTotalStatistics(id);
        return CommonResultUtils.wrapEmptyObjResult(uploadFileMemberTotalStatistics, () ->{
            return BeanUtils.toBean(uploadFileMemberTotalStatistics, UploadFileMemberTotalStatisticsRespVO.class);
        });
    }
    @GetMapping("/page")
    public CommonResult<PageResult<UploadFileMemberTotalStatisticsRespVO>> page(@Valid UploadFileMemberTotalStatisticsPageReqVO pageReqVO) {
        PageResult<UploadFileMemberTotalStatisticsDO> pageResult = uploadFileMemberTotalStatisticsService.getUploadFileMemberTotalStatisticsPage(BeanUtils.toBean(pageReqVO, UploadFileMemberTotalStatisticsPageReqBO.class));
        return CommonResultUtils.wrapEmptyPageResult(pageResult, () -> BeanUtils.toBean(pageResult, UploadFileMemberTotalStatisticsRespVO.class));
    }

}
