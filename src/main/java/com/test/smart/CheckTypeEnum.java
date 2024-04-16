package com.test.smart;

import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Getter
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
    MEASUREMENT_TIME_NOT_SATISFIABLE("7!711!71102!36", "量房时间-一个月外"),
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

    DEMAND_TYPE("7!711!71102!84","需求类型"),
    HOUSE_TYPE_NOT_RIGHT_TIME("7!711!71102!85","房屋类型-暂不方便沟通"),
    VALUE_POINT_HOUSE_TYPE_("7!711!71102!86","价值点-房屋类型-追问1"),
    ENGINEERING_QUANTITY_WHOLE_BOARD_REFORM("7!711!71102!87","工程量（整局改）"),
    COMMUNICATE_TIME("7!711!71102!88","方便沟通时间"),
    IS_TIME_COMMUNICATE("7!711!71102!89","是否方便沟通"),

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


    /******************************************************* AI外呼相关槽位 **********************************************************/
    AI_CALL_ADD_WECHAT_OPEN_REMARK("7!711!71102!79", "仅加微-开场白"),
    AI_CALL_ADD_WECHAT_INFORM_REASON("7!711!71102!80", "仅加微-告知加微原因"),
    AI_CALL_ADD_WECHAT_RETENTION_WORD("7!711!71102!81", "仅加微-挽留话术"),
    AI_CALL_ADD_WECHAT_GUIDE_OPERATION("7!711!71102!82", "仅加微-引导操作加微"),
    AI_CALL_ADD_WECHAT_INFORM_PLATFORM("7!711!71102!83", "仅加微-告知平台服务"),

    ;

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