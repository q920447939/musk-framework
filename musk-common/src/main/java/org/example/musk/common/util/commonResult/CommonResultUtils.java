package org.example.musk.common.util.commonResult;

import org.example.musk.common.exception.BusinessPageExceptionEnum;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.common.pojo.db.PageResult;

import java.util.function.Supplier;

import static org.example.musk.common.pojo.CommonResult.success;
import static org.example.musk.common.pojo.CommonResult.successNoData;

/**
 * ClassName: PageResultUtils
 *
 * @author
 * @Description:
 * @date 2024年11月16日
 */
public class CommonResultUtils {

    /**
     * 包装pageResult为空的场景
     *
     * @param pageResult
     * @param data
     * @param <D>        原类型
     * @param <V>        目标类型
     * @return
     */
    public static <D, V> CommonResult<V> wrapEmptyPageResult(PageResult<D> pageResult, Supplier<V> data) {
        if (null == pageResult || null == pageResult.getList() || pageResult.getList().isEmpty()) {
            return (CommonResult<V>) success(PageResult.empty());
        }
        return success(data.get());
    }

    public static <D, V> CommonResult<V> wrapEmptyObjResult(D source, Supplier<V> data) {
        if (null == source) {
            return successNoData();
        }
        return success(data.get());
    }



}
