package org.example.musk.enums.member;

/**
 * 会员等级枚举
 */
public enum MemberLevelEnums {

        /**
        * 普通会员
        */
        NORMAL(1, "普通会员"),
        /**
        * 银卡会员
        */
        SILVER(2, "银卡会员"),
        /**
        * 金卡会员
        */
        GOLD(3, "金卡会员"),
        /**
        * 钻石会员
        */
        DIAMOND(4, "钻石会员"),
        /**
        * VIP会员
        */
        VIP(5, "VIP会员");

        /**
        * 等级值
        */
        private final Integer value;
        /**
        * 等级名
        */
        private final String name;

        MemberLevelEnums(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        /**
        * 通过等级值，获得对应的枚举
        *
        * @param value 等级值
        * @return 枚举
        */
        public static MemberLevelEnums getByValue(Integer value) {
            for (MemberLevelEnums enums : MemberLevelEnums.values()) {
                if (enums.getValue().equals(value)) {
                    return enums;
                }
            }
            return null;
        }
}
