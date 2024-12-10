package org.example.musk.plugin.lock.enums;

import lombok.Getter;

/**
 * ClassName: LockGroupEnums
 *
 * @author
 * @Description:
 * @date 2024年12月10日
 */
@Getter
public enum LockGroupEnums {
    GROUP_TENANT("GROUP_TENANT","租户级别锁"),
    GROUP_MEMBER("GROUP_MEMBER","会员级别锁");
    private String code;
    private String desc;

     LockGroupEnums(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
