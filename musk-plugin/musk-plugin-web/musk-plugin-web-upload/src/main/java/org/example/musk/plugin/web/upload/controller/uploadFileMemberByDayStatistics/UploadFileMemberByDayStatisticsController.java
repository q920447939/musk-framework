package org.example.musk.plugin.web.upload.controller.uploadFileMemberByDayStatistics;

import jakarta.annotation.Resource;
import jakarta.validation.*;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.common.util.commonResult.CommonResultUtils;
import org.example.musk.common.util.object.BeanUtils;
import org.example.musk.plugin.web.upload.controller.uploadFileMemberByDayStatistics.vo.UploadFileMemberByDayStatisticsAddReqVO;
import org.example.musk.plugin.web.upload.controller.uploadFileMemberByDayStatistics.vo.UploadFileMemberByDayStatisticsPageReqVO;
import org.example.musk.plugin.web.upload.controller.uploadFileMemberByDayStatistics.vo.UploadFileMemberByDayStatisticsRespVO;
import org.example.musk.plugin.web.upload.controller.uploadFileMemberByDayStatistics.vo.UploadFileMemberByDayStatisticsUpdateReqVO;
import org.example.musk.plugin.web.upload.entity.uploadFileMemberByDayStatistics.UploadFileMemberByDayStatisticsDO;
import org.example.musk.plugin.web.upload.entity.uploadFileMemberByDayStatistics.bo.UploadFileMemberByDayStatisticsPageReqBO;
import org.example.musk.plugin.web.upload.service.uploadFileMemberByDayStatistics.UploadFileMemberByDayStatisticsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import static org.example.musk.common.pojo.CommonResult.success;

/**
 *  Controller
 *
 * @author 代码生成器
 */
@RestController
@RequestMapping("/api/uploadFileMemberByDayStatistics")
@Validated
@Slf4j
public class UploadFileMemberByDayStatisticsController {

    @Resource
    private UploadFileMemberByDayStatisticsService uploadFileMemberByDayStatisticsService;

    @PostMapping("/create")
    public CommonResult<Integer> create(@Valid @RequestBody UploadFileMemberByDayStatisticsAddReqVO createReqVO) {
        return success(uploadFileMemberByDayStatisticsService.createUploadFileMemberByDayStatistics(BeanUtils.toBean(createReqVO, UploadFileMemberByDayStatisticsDO.class)));
    }
    @PutMapping("/updateById")
    public CommonResult<Boolean> update(@Valid @RequestBody UploadFileMemberByDayStatisticsUpdateReqVO updateReqVO) {
        uploadFileMemberByDayStatisticsService.updateUploadFileMemberByDayStatisticsById(updateReqVO.getId(), BeanUtils.toBean(updateReqVO, UploadFileMemberByDayStatisticsDO.class));
        return success(true);
    }
    @DeleteMapping("/deleteById")
    public CommonResult<Boolean> delete(@RequestParam("id") Integer id) {
        uploadFileMemberByDayStatisticsService.deleteUploadFileMemberByDayStatistics(id);
        return success(true);
    }
    @GetMapping("/get")
    public CommonResult<UploadFileMemberByDayStatisticsRespVO> get(@RequestParam("id") Integer id) {
        UploadFileMemberByDayStatisticsDO uploadFileMemberByDayStatistics = uploadFileMemberByDayStatisticsService.getUploadFileMemberByDayStatistics(id);
        return CommonResultUtils.wrapEmptyObjResult(uploadFileMemberByDayStatistics, () ->{
            return BeanUtils.toBean(uploadFileMemberByDayStatistics, UploadFileMemberByDayStatisticsRespVO.class);
        });
    }
    @GetMapping("/page")
    public CommonResult<PageResult<UploadFileMemberByDayStatisticsRespVO>> page(@Valid UploadFileMemberByDayStatisticsPageReqVO pageReqVO) {
        PageResult<UploadFileMemberByDayStatisticsDO> pageResult = uploadFileMemberByDayStatisticsService.getUploadFileMemberByDayStatisticsPage(BeanUtils.toBean(pageReqVO, UploadFileMemberByDayStatisticsPageReqBO.class));
        return CommonResultUtils.wrapEmptyPageResult(pageResult, () -> BeanUtils.toBean(pageResult, UploadFileMemberByDayStatisticsRespVO.class));
    }

}
