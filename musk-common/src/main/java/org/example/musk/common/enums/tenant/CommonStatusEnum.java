package org.example.musk.common.enums.tenant;

import cn.hutool.core.util.ObjUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.musk.common.core.IntArrayValuable;

import java.util.Arrays;

/**
 * 通用状态枚举
 *
 * @author
 */
@Getter
@AllArgsConstructor
public enum CommonStatusEnum implements IntArrayValuable {

    ENABLE(0, "开启"),
    DISABLE(1, "关闭");

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(CommonStatusEnum::getStatus).toArray();

    /**
     * 状态值
     */
    private final Integer status;
    /**
     * 状态名
     */
    private final String name;

    @Override
    public int[] array() {
        return ARRAYS;
    }

    public static boolean isEnable(Integer status) {
        return ObjUtil.equal(ENABLE.status, status);
    }

    public static boolean isDisable(Integer status) {
        return ObjUtil.equal(DISABLE.status, status);
    }

}
