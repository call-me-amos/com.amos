package com.test.smart;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Getter
@Slf4j
public enum CheckTypeEnum {
    /**
     * 默认填充编码
     */
    DEFAULT_CODE("7!711!71102!0","默认填充编码"),
    /**
     * 辅助资料地址：用户产品->OMS运营->核需字段
     */
    TIME("7!711!71102!1", "装修时间"),
    AREA("7!711!71102!2", "房屋面积"),
    HOUSE_ADDRESS("7!711!71102!3", "小区名称"),
    HOUSE_TYPE("7!711!71102!4", "房屋类型"),
    DECORATION_STYLE("7!711!71102!5", "装修风格"),
    MEASUREMENT_TIME("7!711!71102!6", "意向量房时间"),
    RESIDENTIAL_TYPE("7!711!71102!7", "居住类型"),
    ENGINEERING_QUANTITY("7!711!71102!8", "工程量"),
    STREET("7!711!71102!9", "街道"),
    REGION("7!711!71102!10", "区县"),
    LAST_NAME("7!711!71102!11", "姓氏"),
    IS_WHOLE_HOUSE_CUSTOMIZATION("7!711!71102!12", "是否全屋定制"),
    COMPLETION_DATE("7!711!71102!13", "交房时间"),
    COMMON_TIME("7!711!71102!14", "时间"),
    budget("7!711!71102!15", "装修预算"),
    CITY("7!711!71102!16", "城市"),
    PHONE("7!711!71102!17", "电话"),
    WHOLE_HOUSE("7!711!71102!18", "预约全屋定制"),
    HARD_DECORATION("7!711!71102!19", "硬装需求"),
    IS_COMPLETION("7!711!71102!20", "是否交房"),
    PHONE_NUMBER("7!711!71102!21", "手机号"),
    DECORATION_USE("7!711!71102!22", "装修用途"),
    BACK_HOME_TIME("7!711!71102!23", "外出回来时间"),
    ENGINEERING_QUANTITY_SUB_INTENTION_1("7!711!71102!24","工程量-只有局改空间"),
    ENGINEERING_QUANTITY_SUB_INTENTION_2("7!711!71102!25","工程量-只有局改详情"),
    @Deprecated
    ENGINEERING_QUANTITY_SUB_INTENTION_3("7!711!71102!26","工程量-缺少水电"),
    @Deprecated
    ENGINEERING_QUANTITY_SUB_INTENTION_4("7!711!71102!27","工程量-只做墙面"),
    @Deprecated
    ENGINEERING_QUANTITY_SUB_INTENTION_5("7!711!71102!28","工程量-非局改范围"),
    ENGINEERING_QUANTITY_SUB_INTENTION_6("7!711!71102!29","工程量-无空间和局改详情"),
    ENGINEERING_QUANTITY_SUB_INTENTION_7("7!711!71102!31","工程量-只有否定局改空间"),
    ENGINEERING_QUANTITY_SUB_INTENTION_8("7!711!71102!32","工程量-只有否定局改详情"),
    ENGINEERING_QUANTITY_SUB_INTENTION_9("7!711!71102!33","工程量-未识别到工程量"),
    ENGINEERING_QUANTITY_ALREADY_HAVE("7!711!71102!37", "工程量-已有"),
    ENGINEERING_PIC_VERIFY_QUANTITIES("7!711!71102!68","工程量-图核工程量"),
    ENGINEERING_NEGATE_INTENTION_ASK("7!711!71102!70","工程量-否定意图追问"),

    DECORATION_TYPE("7!711!71102!34", "装修类型"),
    DECORATION_TIME_NOT_SATISFIABLE("7!711!71102!35", "装修时间-三个月外"),
    MEASUREMENT_TIME_NOT_SATISFIABLE("7!711!71102!36", "意向量房时间-一个月外"),
    COMPLETION_DATE_NOT_SATISFIABLE("7!711!71102!50", "交房时间-三个月后交房"),

    //房屋类型 - 细分意图
    HOUSE_TYPE_PART_MODIFY("7!711!71102!60", "房屋类型-局改"),
    HOUSE_TYPE_SELF_BUILD("7!711!71102!30", "房屋类型-自建房"),
    HOUSE_TYPE_NEW("7!711!71102!61", "房屋类型-新房"),
    HOUSE_TYPE_FINE_DECORATE_HOUSE("7!711!71102!62", "房屋类型-精装房"),
    HOUSE_TYPE_ROUGH_RENT_OUT("7!711!71102!63", "房屋类型-毛坯出租"),
    /**
     * 精装，不同于精装房，需要核实装修是否要精装,还是本身是精装房
     */
    HOUSE_TYPE_FINE_DECORATE("7!711!71102!64","房屋类型-精装"),
    /**
     * 用户标识追问房屋类型的话术1
     */
    HOUSE_TYPE_MAKE_DETAIL_INQUIRY_1("7!711!71102!65","房屋类型-追问1"),
    /**
     * 用户标识追问房屋类型话术2
     */
    HOUSE_TYPE_MAKE_DETAIL_INQUIRY_2("7!711!71102!66", "房屋类型-追问2"),
    HOUSE_TYPE_CLARIFY_SLOT("7!711!71102!67","房屋类型-澄清槽位值"),
    /**
     * 房屋类型-反向 槽位
     */
    HOUSE_TYPE_REVERSE_SLOT("7!711!71102!73","反向-房屋类型"),

    /**
     * 二次追问小区地址的意图。意图配置在小区地址话术的回复意图
     *
     */
    CLARITY_NONE_CITY("7!711!71102!40", "地址追问-城市"),
    CLARITY_CITY("7!711!71102!41", "地址追问-城市澄清问"),
    CLARITY_ADDRESS_SELF_BUILD("7!711!71102!42", "地址追问-小区地址-农村自建房"),
    CLARITY_ADDRESS_NOT_SELF_BUILD("7!711!71102!43", "地址追问-小区地址-非农村自建房"),
    CLARITY_ADDRESS_DIM("7!711!71102!44", "地址追问-小区地址-模糊楼盘"),
    CLARITY_ADDRESS_FLOOR_PLAN("7!711!71102!45", "地址追问-小区地址-收到户型图"),
    CLARITY_ADDRESS_REPLY_HOUSE_INFO("7!711!71102!46", "地址追问-小区地址-回复房屋信息"),
    CLARITY_ADDRESS_DESIGN_SCHEME("7!711!71102!47", "地址追问-小区地址-咨询设计方案"),
    CLARITY_ADDRESS_QUOTE("7!711!71102!48", "地址追问-小区地址-咨询报价"),

    FILL_WITH_SELF_BUILD_ADDRESS("7!711!71102!71", "填充话术-农村自建房-追问小区地址话术"),
    FILL_WITH_NONE_ADDRESS("7!711!71102!72", "填充话术-有城市无小区地址-追问小区地址话术"),

    FILL_WITH_HOUSE_TYPE_DELIVERY_TIME("7!711!71102!74", "填充话术-旧房翻新-替换交房时间话术"),
    REWRITE_DECORATION_TIME_WITH_OPENING_REMARKS_CLARITY("7!711!71102!77", "填充话术-装修时间-带槽位开场白场景替换话术"),


    /**
     * 历史聊天记录提取标签，顾问意图识别
     */
    HISTORY_QUESTION_DECOTIME_DIRECTLY("7!711!71102!53","正问装修时间"),
    HISTORY_QUESTION_DECOTIME_REPLY("7!711!71102!51","反问装修时间"),
    HISTORY_QUESTION_DECOTIME_ENQUIRE("7!711!71102!52","询问装修时间"),


    /**
     * 开场白澄清式槽位
     */
    OPENING_REMARKS_CLARITY("7!711!71102!54", "开场白-澄清槽位"),
    OPENING_REMARKS_CLARITY_FOR_HOUSE_TYPE("7!711!71102!55", "开场白-澄清槽位-房屋类型", "7!711!71102!4"),
    OPENING_REMARKS_CLARITY_FOR_CITY("7!711!71102!56", "开场白-澄清槽位-城市", "7!711!71102!16"),
    OPENING_REMARKS_CLARITY_FOR_AREA("7!711!71102!57", "开场白-澄清槽位-房屋面积", "7!711!71102!2"),
    OPENING_REMARKS_CLARITY_FOR_DECORATION_USE("7!711!71102!58", "开场白-澄清槽位-装修用途", "7!711!71102!22"),
    OPENING_REMARKS_CLARITY_FOR_DECORATION_TIME("7!711!71102!59", "开场白-澄清槽位-装修时间", "7!711!71102!1"),
    OPENING_REMARKS_CLARITY_FOR_TIMEOUT("7!711!71102!69", "开场白-澄清槽位-超时促开口话术"),

    /***************** start 中间态槽位 ********************/
    DEMAND_TYPE("7!711!71102!84","需求类型"),
    HOUSE_TYPE_NOT_RIGHT_TIME("7!711!71102!85","房屋类型-暂不方便沟通"),
    VALUE_POINT_HOUSE_TYPE_("7!711!71102!86","价值点-房屋类型-追问1"),
    ENGINEERING_QUANTITY_WHOLE_BOARD_REFORM("7!711!71102!87","工程量（整局改）"),
    COMMUNICATE_TIME("7!711!71102!88","方便沟通时间"),
    IS_TIME_COMMUNICATE("7!711!71102!89","是否方便沟通"),
    HOUSE_TYPE_NEW_CLARITY_1("7!711!71102!90","房屋类型-新房澄清问1"),
    SLOT_FOR_RULE_01("7!711!71102!91","城市-暂不方便沟通"),
    SLOT_FOR_RULE_02("7!711!71102!92","城市-追问1"),
    SLOT_FOR_RULE_03("7!711!71102!93","城市-追问2"),
    SLOT_FOR_RULE_04("7!711!71102!94","电话-追问1"),
    SLOT_FOR_RULE_05("7!711!71102!95","电话-追问2"),
    SLOT_FOR_RULE_06("7!711!71102!96","房屋类型_澄清槽位"),
    SLOT_FOR_RULE_07("7!711!71102!97","房屋面积暂不方便沟通"),
    SLOT_FOR_RULE_08("7!711!71102!98","房屋面积-追问1"),
    SLOT_FOR_RULE_09("7!711!71102!99","房屋面积-追问2"),
    SLOT_FOR_RULE_10("7!711!71102!100","价值点-房屋面积-追问1"),
    SLOT_FOR_RULE_11("7!711!71102!101","交房时间-暂不方便沟通"),
    SLOT_FOR_RULE_12("7!711!71102!102","交房时间-追问1"),
    SLOT_FOR_RULE_13("7!711!71102!103","是否交房-暂不方便沟通"),
    SLOT_FOR_RULE_14("7!711!71102!104","是否交房-追问1"),
    SLOT_FOR_RULE_15("7!711!71102!105","小区地址-暂不方便沟通"),
    SLOT_FOR_RULE_16("7!711!71102!106","小区地址-追问1"),
    SLOT_FOR_RULE_17("7!711!71102!107","小区地址-追问2"),
    SLOT_FOR_RULE_18("7!711!71102!108","需求类型-简装"),
    SLOT_FOR_RULE_19("7!711!71102!109","需求类型-精装"),
    SLOT_FOR_RULE_20("7!711!71102!110","需求类型-精装房/简装房"),
    SLOT_FOR_RULE_21("7!711!71102!111","需求类型-暂不方便沟通"),
    SLOT_FOR_RULE_22("7!711!71102!112","需求类型-追问1"),
    SLOT_FOR_RULE_23("7!711!71102!113","需求信息"),
    SLOT_FOR_RULE_24("7!711!71102!114","装修用途-暂不方便沟通"),
    SLOT_FOR_RULE_25("7!711!71102!115","装修用途-追问1"),
    SLOT_FOR_RULE_26("7!711!71102!116","装修用途-追问2"),
    SLOT_FOR_RULE_27("7!711!71102!117","自建房是否交房"),
    SLOT_FOR_RULE_28("7!711!71102!118","自建房是否交房-暂不方便沟通"),
    SLOT_FOR_RULE_29("7!711!71102!119","自建房是否交房-追问1"),
    SLOT_FOR_RULE_30("7!711!71102!120","自建房小区地址"),
    SLOT_FOR_RULE_31("7!711!71102!121","自建房小区地址-暂不方便沟通"),
    SLOT_FOR_RULE_32("7!711!71102!122","自建房小区地址-追问1"),
    SLOT_FOR_RULE_33("7!711!71102!123","自建房小区地址-追问2"),
    SLOT_FOR_RULE_34("7!711!71102!124","房屋面积-暂不方便沟通"),
    SLOT_FOR_RULE_35("7!711!71102!125","小区地址-回复暂不方便沟通"),
    SLOT_FOR_RULE_36("7!711!71102!126","自建房小区地址-回复暂不方便沟通"),
    SLOT_FOR_RULE_37("7!711!71102!127","电话-暂不方便沟通"),
    SLOT_FOR_RULE_38("7!711!71102!128","需求信息-追问1"),
    SLOT_FOR_RULE_39("7!711!71102!129","装修时间（初轮）"),
    SLOT_FOR_RULE_40("7!711!71102!130","装修时间（引导）"),
    SLOT_FOR_RULE_41("7!711!71102!131","装修时间-追问1"),
    SLOT_FOR_RULE_42("7!711!71102!132","装修时间-追问2"),
    SLOT_FOR_RULE_43("7!711!71102!133","装修时间-已交房"),
    SLOT_FOR_RULE_44("7!711!71102!134","装修时间-暂不方便沟通"),
    SLOT_FOR_RULE_45("7!711!71102!135","装修时间-未交房"),
    SLOT_FOR_RULE_46("7!711!71102!136","装修时间-初轮"),
    SLOT_FOR_RULE_47("7!711!71102!137","房子交房前能否提前进去看"),
    SLOT_FOR_RULE_48("7!711!71102!138","房子交房前能否提前进去看-追问1"),
    SLOT_FOR_RULE_49("7!711!71102!139","房子交房前能否提前进去看-暂不方便沟通"),
    SLOT_FOR_RULE_50("7!711!71102!140","意向量房时间-开始时间"),
    SLOT_FOR_RULE_51("7!711!71102!141","意向量房时间-结束时间"),
    SLOT_FOR_RULE_52("7!711!71102!142","不方便时间"),
    SLOT_FOR_RULE_53("7!711!71102!143","不方便时间-开始时间"),
    SLOT_FOR_RULE_54("7!711!71102!144","不方便时间-结束时间"),
    SLOT_FOR_RULE_55("7!711!71102!145","方便时间"),
    SLOT_FOR_RULE_56("7!711!71102!146","方便时间-开始时间"),
    SLOT_FOR_RULE_57("7!711!71102!147","方便时间-结束时间"),
    SLOT_FOR_RULE_58("7!711!71102!148","意向量房时间-澄清值"),

    SLOT_FOR_RULE_59("7!711!71102!149","意向量房时间-意向量房时间段追问-开始+9"),
    SLOT_FOR_RULE_61("7!711!71102!151","意向量房时间-下周末追问1"),
    SLOT_FOR_RULE_62("7!711!71102!152","意向量房时间-意向量房时间段追问-开始+9追问2"),
    SLOT_FOR_RULE_63("7!711!71102!153","交房时间-意向量房时间段追问-当前+29"),
    SLOT_FOR_RULE_64("7!711!71102!154","当天时间-星期"),
    SLOT_FOR_RULE_66("7!711!71102!156","意向量房时间-意向量房时间段追问-当前+29追问2"),
    SLOT_FOR_RULE_67("7!711!71102!157","是否在外地"),
    SLOT_FOR_RULE_69("7!711!71102!159","意向量房时间-12点前-周末"),
    SLOT_FOR_RULE_70("7!711!71102!160","意向量房时间-模糊澄清追问2"),
    SLOT_FOR_RULE_71("7!711!71102!161","意向量房时间-12点后-工作日"),
    SLOT_FOR_RULE_72("7!711!71102!162","意向量房时间-12点后-周日"),
    SLOT_FOR_RULE_73("7!711!71102!163","意向量房时间-周末追问1"),
    SLOT_FOR_RULE_74("7!711!71102!164","意向量房时间-下周追问1"),
    SLOT_FOR_RULE_75("7!711!71102!165","交房时间-意向量房时间段追问-开始+9"),
    SLOT_FOR_RULE_76("7!711!71102!166","意向量房时间-20天30天追问2"),
    SLOT_FOR_RULE_77("7!711!71102!167","意向量房时间-12点后-周末"),
    SLOT_FOR_RULE_78("7!711!71102!168","意向量房时间-提取值"),
    SLOT_FOR_RULE_79("7!711!71102!169","意向量房时间-模糊澄清追问1"),
    SLOT_FOR_RULE_80("7!711!71102!170","意向量房时间-12点前-工作日"),
    SLOT_FOR_RULE_81("7!711!71102!171","意向量房时间-意向量房时间段追问-当前+29"),
    SLOT_FOR_RULE_82("7!711!71102!172","意向量房时间-30天内交房"),
    SLOT_FOR_RULE_83("7!711!71102!173","交房时间-开始时间"),
    SLOT_FOR_RULE_84("7!711!71102!174","意向量房时间-开始时间"),
    SLOT_FOR_RULE_85("7!711!71102!175","意向量房时间-结束时间"),
    SLOT_FOR_RULE_86("7!711!71102!176","意向量房时间-提取值"),

    SLOT_FOR_RULE_87("7!711!71102!177","小区名称-暂不方便沟通"),
    SLOT_FOR_RULE_88("7!711!71102!178","自建房小区名称"),
    SLOT_FOR_RULE_89("7!711!71102!179","自建房小区名称-追问2"),
    SLOT_FOR_RULE_90("7!711!71102!180","小区名称-追问2"),
    SLOT_FOR_RULE_91("7!711!71102!181","自建房小区名称-暂不方便沟通"),
    SLOT_FOR_RULE_92("7!711!71102!182","自建房小区名称-追问1"),
    SLOT_FOR_RULE_93("7!711!71102!183","小区名称-追问1"),

    SLOT_FOR_RULE_94("7!711!71102!184","小区名称-回复暂不方便沟通"),
    SLOT_FOR_RULE_95("7!711!71102!185","自建房小区名称-回复暂不方便沟通"),
    /***************** end 中间态槽位 ********************/




    GENDER("7!711!71102!201","性别"),
    COMPLETION("7!711!71102!202","交房类型"),
    TO_STORE_TIME("7!711!71102!203","到店时间"),
    HOUSE_USE("7!711!71102!204", "房屋用途"),
    MEASUREMENTT_TIME("7!711!71102!212", "量房时间"),
    COMUNITY_ADDRESS("7!711!71102!214", "小区地址"),

    // 3XX 用作标识数据来源或与识别槽位无直接关系的辅助信息处理
    SYNC_PROJECT_INFO("7!711!71102!300","同步项目信息"),

    /******************************************************* 虚拟槽位（ner未提取） **********************************************************/
    VIRTUALLY_ROUGH_HOUSE("7!711!71102!205","是否毛坯"),
    VIRTUALLY_HARDCOVER_HOUSE("7!711!71102!206","是否精装房"),
    VIRTUALLY_SELF_BUILD_HARDCOVER_HOUSE("7!711!71102!207","是否自建房"),
    VIRTUALLY_ENGINEERINGS("7!711!71102!208","工程项"),
    VIRTUALLY_ENGINEERING_SPACE("7!711!71102!209","工程空间"),
    VIRTUALLY_FINISH_ENGINEERINGS("7!711!71102!210","已做工程项"),
    VIRTUALLY_TODO_ENGINEERINGS("7!711!71102!211","待做工程项"),
    VIRTUALLY_HOUSE_TYPE("7!711!71102!213","虚拟房屋类型"),  //毛坯房，精装房，旧/二手房,, 三者取其一

    ;

    /**
     * 检查是否存在重复的槽位命名
     */
    public static void checkRepetitionMsg() {
        Set<String> msgSet = new HashSet<>();
        Set<String> repetitionMsg = new HashSet<>();
        for (com.to8to.tbt.tls.smartChat.enums.CheckTypeEnum statusEnum: com.to8to.tbt.tls.smartChat.enums.CheckTypeEnum.values()) {
            if (msgSet.contains(statusEnum.getMsg())) {
                repetitionMsg.add(statusEnum.getMsg());
            } else {
                msgSet.add(statusEnum.getMsg());
            }
        }
        log.info("重复的槽位： {}", JSONObject.toJSONString(repetitionMsg));
    }

    /**
     * 核需槽位全码
     */
    private final String code;
    /**
     * 核需槽位中文描述
     */
    private final String msg;
    /**
     * 中间态槽位，可以通过这个字段关联其他槽位。
     * 比如：【开场白-澄清槽位-房屋类型】可以关联【房屋类型】
     */
    private String relateCheckTypeCode;

    CheckTypeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    CheckTypeEnum(String code, String msg, String relateCheckTypeCode) {
        this.code = code;
        this.msg = msg;
        this.relateCheckTypeCode = relateCheckTypeCode;
    }

    public static CheckTypeEnum getCheckTypeEnumByRelateCheckTypeCode(String relateCheckTypeCode) {
        for (CheckTypeEnum statusEnum: CheckTypeEnum.values()) {
            if (null != statusEnum.getRelateCheckTypeCode() && statusEnum.relateCheckTypeCode.equals(relateCheckTypeCode)) {
                return statusEnum;
            }
        }
        return null;
    }

    /**
     * 按照槽位描述正则规则检索需要的核需集合
     * @param regexMsg
     * @return
     */
    public static List<CheckTypeEnum> getCheckTypeEnumByRegex(String regexMsg){
        List<CheckTypeEnum> resultEnumList = Lists.newArrayList();
        Pattern pattern = Pattern.compile(regexMsg);
        for (CheckTypeEnum em: CheckTypeEnum.values()){
            Matcher matcher = pattern.matcher(em.getMsg());
            if (matcher.matches()){
                resultEnumList.add(em);
            }
        }
        return resultEnumList;
    }

    public static CheckTypeEnum of(String code) {
        for (CheckTypeEnum statusEnum : CheckTypeEnum.values()) {
            if (statusEnum.code.equals(code)) {
                return statusEnum;
            }
        }
        return null;
    }

    /**
     * 通过名称描述获取槽位枚举类
     *
     * @param name
     * @return
     */
    public static CheckTypeEnum getByName(String name) {
        for (CheckTypeEnum statusEnum : CheckTypeEnum.values()) {
            if (statusEnum.msg.equals(name)) {
                return statusEnum;
            }
        }
        return null;
    }

    /**
     * 获取开场白澄清问的相关槽位
     * @return
     */
    public static List<String> getOpeningRemarksClarity(){
        return Arrays.asList(
                OPENING_REMARKS_CLARITY_FOR_HOUSE_TYPE.getCode(),
                OPENING_REMARKS_CLARITY_FOR_CITY.getCode(),
                OPENING_REMARKS_CLARITY_FOR_AREA.getCode(),
                OPENING_REMARKS_CLARITY_FOR_DECORATION_USE.getCode(),
                OPENING_REMARKS_CLARITY_FOR_DECORATION_TIME.getCode()
        );
    }

    /**
     * NER模型能直接提取到的时间相关槽位
     */
    public static final List<String> NER_MODEL_EXTRACT_TIME_SLOT_CODES = Lists.newArrayList(TIME.getCode(),
            COMPLETION_DATE.getCode(), MEASUREMENT_TIME.getCode());
}