package org.example.musk.auth.service.core.helper.member;


import com.github.javafaker.Faker;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MemberNickNameHelper {
    private static final Faker FAKER = new Faker();


    private static final List<String> FAMILY_NAMES = Arrays.asList(
            "赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈",
            "褚", "卫", "蒋", "沈", "韩", "杨", "朱", "秦", "尤", "许",
            "何", "吕", "施", "张", "孔", "曹", "严", "华", "金", "魏",
            "陶", "姜", "戚", "谢", "邹", "喻", "柏", "水", "窦", "章",
            "云", "苏", "潘", "葛", "奚", "范", "彭", "郎", "鲁", "韦",
            "昌", "马", "苗", "凤", "花", "方", "俞", "任", "袁", "柳",
            "酆", "鲍", "史", "唐", "费", "廉", "岑", "薛", "雷", "贺",
            "倪", "汤", "滕", "殷", "罗", "毕", "郝", "邬", "安", "常",
            "乐", "于", "时", "傅", "皮", "卞", "齐", "康", "伍", "余",
            "元", "卜", "顾", "孟", "平", "黄", "和", "穆", "萧", "尹",
            "姚", "邵", "湛", "汪", "祁", "毛", "禹", "狄", "米", "贝",
            "明", "臧", "计", "伏", "成", "戴", "谈", "宋", "茅", "庞",
            "熊", "纪", "舒", "屈", "项", "祝", "董", "梁", "杜", "阮",
            "蓝", "闵", "席", "季", "麻", "强", "贾", "路", "娄", "危",
            "江", "童", "颜", "郭", "梅", "盛", "林", "刁", "钟", "徐",
            "邱", "骆", "高", "夏", "蔡", "田", "樊", "胡", "凌", "霍",
            "虞", "万", "支", "柯", "昝", "管", "卢", "莫", "经", "房",
            "裘", "缪", "干", "解", "应", "宗", "丁", "宣", "贲", "邓",
            "郁", "单", "杭", "洪", "包", "诸", "左", "石", "崔", "吉",
            "钮", "龚", "程", "嵇", "邢", "滑", "裴", "陆", "荣", "翁",
            "荀", "羊", "於", "惠", "甄", "麴", "家", "封", "芮", "羿",
            "储", "靳", "汲", "邴", "糜", "松", "井", "段", "富", "巫",
            "乌", "焦", "巴", "弓", "牧", "隗", "山", "谷", "车", "侯",
            "宓", "蓬", "全", "郗", "班", "仰", "秋", "仲", "伊", "宫",
            "宁", "仇", "栾", "暴", "甘", "钭", "厉", "戎", "祖", "武",
            "符", "刘", "景", "詹", "束", "龙", "叶", "幸", "司", "韶",
            "郜", "黎", "蓟", "薄", "印", "宿", "白", "怀", "蒲", "邰",
            "从", "鄂", "索", "咸", "籍", "赖", "卓", "蔺", "屠", "蒙",
            "池", "乔", "阴", "郁", "胥", "能", "苍", "双", "闻", "莘",
            "党", "翟", "谭", "贡", "劳", "逄", "姬", "申", "扶", "堵",
            "冉", "宰", "郦", "雍", "却", "璩", "桑", "桂", "濮", "牛",
            "寿", "通", "边", "扈", "燕", "冀", "郏", "浦", "尚", "农",
            "温", "别", "庄", "晏", "柴", "瞿", "阎", "充", "慕", "连",
            "茹", "习", "宦", "艾", "鱼", "容", "向", "古", "易", "慎",
            "戈", "廖", "庾", "终", "暨", "居", "衡", "步", "都", "耿",
            "满", "弘", "匡", "国", "文", "寇", "广", "禄", "阙", "东",
            "殴", "殳", "沃", "利", "蔚", "越", "夔", "隆", "师", "巩",
            "厍", "聂", "晁", "勾", "敖", "融", "冷", "訾", "辛", "阚",
            "那", "简", "饶", "空", "曾", "毋", "沙", "乜", "养", "鞠",
            "须", "丰", "巢", "关", "蒯", "相", "查", "后", "荆", "红",
            "游", "竺", "权", "逯", "盖", "益", "桓", "公", "万", "俟",
            "司马", "上官", "欧阳", "夏侯", "诸葛", "闻人", "东方", "赫连", "皇甫", "尉迟",
            "公羊", "澹台", "公冶", "宗政", "濮阳", "淳于", "单于", "太叔", "申屠", "公孙",
            "仲孙", "轩辕", "令狐", "钟离", "宇文", "长孙", "慕容", "鲜于", "闾丘", "司徒",
            "司空", "亓官", "司寇", "仉", "督", "子车", "颛孙", "端木", "巫马", "公西",
            "漆雕", "乐正", "壤驷", "公良", "拓跋", "夹谷", "宰父", "谷梁", "晋", "楚",
            "阎", "法", "汝", "鄢", "涂", "钦", "段干", "百里", "东郭", "南门",
            "呼延", "归", "海", "羊舌", "微生", "岳", "帅", "缑", "亢", "况",
            "后", "有", "琴", "梁丘", "左丘", "东门", "西门", "商", "牟", "佘",
            "佴", "伯", "赏", "南宫", "墨", "哈", "谯", "笪", "年", "爱",
            "阳", "佟"
    );

    public static String generatorName() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int nextInt = random.nextInt(0, 4);
        return switch (nextInt) {
            case 2 -> generateFixedNameWithRandomNumbers();
            case 3 -> generateEnglish();
            default -> generatorChineseFullName();
        };
    }

    public static String generatorHidePartName() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int nextInt = random.nextInt(0, 4);
        return switch (nextInt) {
            case 2 -> {
                String fixedNameWithRandomNumbers = generateFixedNameWithRandomNumbers();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < fixedNameWithRandomNumbers.length() - 2; i++) {
                    sb.append("*");
                }
                yield fixedNameWithRandomNumbers.substring(0,2) + "****" + fixedNameWithRandomNumbers.substring(fixedNameWithRandomNumbers.length() - 4 ); // 添加 yield 关键字
            }
            case 3 -> {
                String englishName = generateEnglish();
                if (englishName.length() <= 1) {
                    yield englishName; // 添加 yield 关键字
                } else if (englishName.length() == 2) {
                    yield englishName.charAt(0) + "*";
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < englishName.length() - 2; i++) {
                        sb.append("*");
                    }
                    yield englishName.charAt(0) + sb.toString() + englishName.substring(englishName.length() - 1); // 添加 yield 关键字
                }
            }
            default -> {
                String chineseFullName = generatorChineseFullName();
                if (chineseFullName.length() <= 1) {
                    yield chineseFullName; // 添加 yield 关键字
                } else if (chineseFullName.length() == 2) {
                    yield chineseFullName.charAt(0) + "*";
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < chineseFullName.length() - 2; i++) {
                        sb.append("*");
                    }
                    yield chineseFullName.charAt(0) + sb.toString() + chineseFullName.substring(chineseFullName.length() - 1); // 添加 yield 关键字
                }
            }
        };
    }

    public static String generatorChineseFullName() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        String firstName = FAMILY_NAMES.get(random.nextInt(0, FAMILY_NAMES.size() + 1));

        int size = random.nextInt(1, 3);
        StringBuilder sb = new StringBuilder(firstName);
        for (int i = 0; i < size; i++) {
            sb.append(getRandomChar());
        }
        return sb.toString();
    }

    public static String generateFixedNameWithRandomNumbers() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int randomNumber = random.nextInt(10000000, 99999999); // 生成0到99999999之间的随机数
        return String.format("用户%08d", randomNumber);
    }

    public static String generateEnglish() {
        return FAKER.name().fullName();
    }

    private static char getRandomChar() {
        String str;

        int hightPos;

        int lowPos;

        ThreadLocalRandom random = ThreadLocalRandom.current();

        hightPos = (176 + Math.abs(random.nextInt(39)));

        lowPos = (161 + Math.abs(random.nextInt(93)));

        byte[] b = new byte[2];

        b[0] = (Integer.valueOf(hightPos)).byteValue();

        b[1] = (Integer.valueOf(lowPos)).byteValue();

        try {
            str = new String(b, "GBK");
        } catch (UnsupportedEncodingException e) {
            return getRandomChar();
        }
        return str.charAt(0);
    }

}
