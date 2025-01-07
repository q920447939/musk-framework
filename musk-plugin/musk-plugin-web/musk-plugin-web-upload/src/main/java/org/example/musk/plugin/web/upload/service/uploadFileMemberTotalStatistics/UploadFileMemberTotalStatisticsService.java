package org.example.musk.plugin.web.upload.service.uploadFileMemberTotalStatistics;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.*;
import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.plugin.web.upload.entity.uploadFileMemberTotalStatistics.UploadFileMemberTotalStatisticsDO;
import org.example.musk.plugin.web.upload.entity.uploadFileMemberTotalStatistics.bo.UploadFileMemberTotalStatisticsPageReqBO;
import org.example.musk.plugin.web.upload.enums.FileTypeEnums;

import java.util.List;

/**
 *  Service 接口
 *
 * @author 代码生成器
 */
public interface UploadFileMemberTotalStatisticsService extends IService<UploadFileMemberTotalStatisticsDO> {

    /**
     * 创建
     *
     * @param uploadFileMemberTotalStatisticsDO 创建信息
     * @return ID
     */
    Integer createUploadFileMemberTotalStatistics(@Valid UploadFileMemberTotalStatisticsDO uploadFileMemberTotalStatisticsDO);
    /**
     * 更新
     *
     * @param updateUploadFileMemberTotalStatistics 更新信息
     */
    void updateUploadFileMemberTotalStatisticsById(Integer id, @Valid UploadFileMemberTotalStatisticsDO updateUploadFileMemberTotalStatistics);
    /**
     * 删除
     *
     * @param id 编号
     */
    void deleteUploadFileMemberTotalStatistics(Integer id);
    /**
     * 获得
     *
     * @param id 编号
     * @return
     */
    UploadFileMemberTotalStatisticsDO getUploadFileMemberTotalStatistics(Integer id);
    /**
     * 获得分
     *
     * @param pageReqBO 分页查询
     * @return 分页
     */
    PageResult<UploadFileMemberTotalStatisticsDO> getUploadFileMemberTotalStatisticsPage(UploadFileMemberTotalStatisticsPageReqBO pageReqBO);

    void saveUploadFileMemberTotalStatistics(Integer memberId, FileTypeEnums fileTypeEnums, long times, double fileSize);

    List<UploadFileMemberTotalStatisticsDO> listUploadFileMemberTotalStatistics(int memberId);
}
