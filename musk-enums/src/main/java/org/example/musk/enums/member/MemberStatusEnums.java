package org.example.musk.enums.member;

/**
 * 会员状态美剧
 * 状态(0:禁用,1:生效)
 */
public enum MemberStatusEnums {

        /**
        * 禁用
        */
        DISABLE(0, "禁用"),
        /**
        * 生效
        */
        EFFECTIVE(1, "生效");

        /**
        * 状态值
        */
        private final Integer status;
        /**
        * 状态名
        */
        private final String desc;

        MemberStatusEnums(Integer status, String desc) {
            this.status = status;
            this.desc = desc;
        }

        public Integer getStatus() {
            return status;
        }

        public String getDesc() {
            return desc;
        }

        /**
        * 通过状态值，获得对应的枚举
        *
        * @param status 状态
        * @return 枚举
        */
        public static MemberStatusEnums getByStatus(Integer status) {
            for (MemberStatusEnums enums : MemberStatusEnums.values()) {
                if (enums.getStatus().equals(status)) {
                    return enums;
                }
            }
            return null;
        }
}
