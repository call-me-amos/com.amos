package com.test.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.test.file.ProcessOnToRow;
import com.test.robotAsk.RobotAskManager;
import com.test.smart.CheckTypeEnum;
import io.netty.util.internal.ThrowableUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang.StringUtils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

/**
 * excel表头：策略名称	规则条件	跳转策略	询问槽位
 * 生成sql
 * <p>
 * DELETE FROM tls_smart_chat_diy_rule
 * <p>
 * INSERT INTO tls_smart_chat_diy_rule (version_no, rule_name, rule_describe, rule_condition, next_strategy_type, next_ask_slot, template_id) VALUES
 * ('1','第一句话', 'HOUSE_TYPE_times=0', 2, '7!711!71102!4', 'TAG20240409000001'),
 * ('1','未识别到意图', 'intention=''其他''', 0, '', 'TAG20240409000001');
 * @author amos.tong
 */
@Slf4j
public class ForRuleConditionMain {
    // pos脑图文件路径
    public static final String filePath = "E:\\amos\\文档\\智能研发部\\规则跳转\\条件跳转-脑图\\新策略表格式-0414.pos";
    // private static final String filePath = "C:\\Users\\amos.tong\\Desktop\\开始 (1).pos";

//    private static final String FROM_FILE_NAME = "C:\\Users\\amos.tong\\Desktop\\条件跳转\\智能应答策略2.0-7.xlsx";
//    private static final String TO_FILE_NAME = "C:\\Users\\amos.tong\\Desktop\\条件跳转\\temp.xlsx";


    /**
     * 变量的默认值
     */
    public static final Map<String, String> DEFAULT_PARAMETER_MAP = getInitSlot();
    /**
     * 没有配置的槽位
     */
    private static final Set<String> NO_CONFIG_NAME = Sets.newHashSet();

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("============  start");
        // File file = new File(FROM_FILE_NAME);
        // List<LinkedHashMap<Integer, String>> excelModelFromFileList = importExcel(new FileInputStream(file), 0);
        ProcessOnToRow.initExcelModelFromProcessOn();
        List<LinkedHashMap<Integer, String>> excelModelFromFileList = ProcessOnToRow.EXCEL_MODEL_FROM_PROCESS_ON;
        parseExcelModeToSql(excelModelFromFileList);

        Map<String, Map<String, List<JSONObject>>> robotAskMap = ProcessOnToRow.ROBOT_ASK_LIST;
        createRobotAsk(robotAskMap);

        System.out.println(JSONObject.toJSONString(NO_CONFIG_NAME));
        System.out.println("===============");
    }

    private static void createRobotAsk(Map<String, Map<String, List<JSONObject>>> robotAskMap){
        robotAskMap.forEach((checkTypeCodeName, paraMap)->{
            List<JSONObject> replyList = null == paraMap.get("replyList")? Lists.newArrayList():paraMap.get("replyList");
            List<JSONObject> defaultReplyList = null == paraMap.get("defaultReplyList")? Lists.newArrayList(): paraMap.get("defaultReplyList");
            List<JSONObject> noResponseList = null == paraMap.get("noResponseList")? Lists.newArrayList():paraMap.get("noResponseList");

            String checkTypeCode = CheckTypeEnum.getByName(checkTypeCodeName).getCode();
            JSONObject result = RobotAskManager.queryContentByChatIdAndCheckTypeCode(checkTypeCode);

            Integer robotAskId = null;
            if(null != result.getJSONObject("data") && null != result.getJSONObject("data").getInteger("id")){
                robotAskId = result.getJSONObject("data").getInteger("id");

                int finalRobotAskId = robotAskId;
                defaultReplyList.forEach(defaultReply->{
                    defaultReply.put("robotAskId", finalRobotAskId);
                });
            }
            result = RobotAskManager.createOrUpdateRobotAskId(robotAskId, checkTypeCode, replyList, defaultReplyList, noResponseList);
            if(null == robotAskId){
                robotAskId = result.getInteger("data");
                System.out.println("新增话术id=" + robotAskId);
                RobotAskManager.effectOrInvalid(1, Arrays.asList(robotAskId));
            }
        });
    }

    /**
     * @param rowNumber 从第几行开始解析
     * @return 返回每一行
     */
    private static List<LinkedHashMap<Integer, String>> importExcel(InputStream inputStream, int rowNumber) {
        List<LinkedHashMap<Integer, String>> list;
        try {
            list = EasyExcel.read(inputStream, null).sheet().headRowNumber(rowNumber).doReadSync();
        } catch (Exception e) {
            log.error("e============{}", ThrowableUtil.stackTraceToString(e));
            throw new RuntimeException();
        }
        return list;
    }

    public static void parseExcelModeToSql(List<LinkedHashMap<Integer, String>> excelModelFromFileList) {
        //List<ToExcelData> toExcelDataList = new ArrayList<>();
        String versionNo = String.valueOf(System.currentTimeMillis());
        excelModelFromFileList.forEach(row -> {
            if ("99999-下一槽位默认调整槽位--测试使用".equals(row.get(0))) {
                System.out.println("");
            }
            StringBuffer sb = new StringBuffer();
            sb.append("(\"");
            sb.append(versionNo);
            sb.append("\", \"");
            sb.append(row.get(0));
            sb.append("\", \"");
            sb.append(row.get(1));
            sb.append("\", \"");
            sb.append(convertToMvelExpression(row.get(1)));// 条件
            sb.append("\", \"");
            sb.append(row.get(2));
            sb.append("\",\"");
            sb.append(row.get(3));
            sb.append("\",\"");
            sb.append(60);
            sb.append("\"),");
            // TODO 话术可以拼接 curl

            System.out.println(sb);

//            ToExcelData data = new ToExcelData();
//            data.setSql(sb.toString());
//            toExcelDataList.add(data);
        });

        // EasyExcel
        // .write(TO_FILE_NAME, ToExcelData.class)
        // .excelType(ExcelTypeEnum.XLSX)
        // .sheet("模板-01")
        // .doWrite(toExcelDataList);
    }

    private static String convertToMvelExpression(String logicDescription) {
        String[] lines = logicDescription.split("\\r?\\n");
        StringBuilder mvelExpressionBuilder = new StringBuilder();
        for (String line : lines) {
            String[] parts = line.split(" and ");
            dealSingleExpression(mvelExpressionBuilder, parts, " && ");
        }
        return mvelExpressionBuilder.toString();
    }

    private static void dealSingleExpression(StringBuilder mvelExpressionBuilder, String[] parts, String operate) {
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part.contains("意图=表达房子出租中")) {
                System.out.print("");
            }
            if (part.contains(" or ")) {
                String[] tokens = part.split(" or ");
                dealSingleExpression(mvelExpressionBuilder, tokens, " || ");
            } else if (part.contains("not in")) {
                String[] tokens = part.split("not in");
                String variableName = tokens[0].trim();
                String valuesString = tokens[1].trim();
                variableName = variableName.replace("（", "(");
                variableName = variableName.replace("）", ")");
                variableName = variableName.replace("(", "");

                valuesString = valuesString.replace("（", "(");
                valuesString = valuesString.replace("）", ")");
                List<String> values;
                if (valuesString.contains("、")) {
                    values = Arrays.asList(valuesString.substring(1, valuesString.length() - 1).split("、"));
                } else if (valuesString.contains("，")) {
                    values = Arrays.asList(valuesString.substring(1, valuesString.length() - 1).split("，"));
                } else {
                    values = Arrays.asList(valuesString);
                }
                StringBuilder subMvelExpressionBuilder = new StringBuilder();
                subMvelExpressionBuilder.append("(");
                for (String value : values) {
                    value = getStringWithSingleQuotationMark(value);
                    subMvelExpressionBuilder
                            .append("!")
                            .append(findExpressName(variableName))
                            .append(".contains(")
                            .append(value)
                            .append(")")
                            .append(" and ");
                }
                subMvelExpressionBuilder.delete(subMvelExpressionBuilder.length() - 5, subMvelExpressionBuilder.length());
                subMvelExpressionBuilder.append(")");

                String newStr = fillQuote(part, subMvelExpressionBuilder);
                mvelExpressionBuilder.append(newStr);
            } else if (part.contains(" in ")) {
                String[] tokens = part.split(" in ");
                String variableName = tokens[0].trim();
                String valuesString = tokens[1].trim();
                variableName = variableName.replace("（", "(");
                variableName = variableName.replace("）", ")");
                variableName = variableName.replace("(", "");

                valuesString = valuesString.replace("（", "(");
                valuesString = valuesString.replace("）", ")");
                List<String> values;
                if (valuesString.contains("、")) {
                    values = Arrays.asList(valuesString.substring(1, valuesString.length() - 1).split("、"));
                } else if (valuesString.contains("，")) {
                    values = Arrays.asList(valuesString.substring(1, valuesString.length() - 1).split("，"));
                } else {
                    values = Arrays.asList(valuesString);
                }
                StringBuilder subMvelExpressionBuilder = new StringBuilder();
                subMvelExpressionBuilder.append("(");
                for (String value : values) {
                    value = getStringWithSingleQuotationMark(value);
                    subMvelExpressionBuilder
                            .append(findExpressName(variableName))
                            .append(".contains(")
                            .append(value)
                            .append(")")
                            .append(" || ");
                }
                subMvelExpressionBuilder.delete(subMvelExpressionBuilder.length() - 4, subMvelExpressionBuilder.length());
                subMvelExpressionBuilder.append(")");

                String newStr = fillQuote(part, subMvelExpressionBuilder);
                mvelExpressionBuilder.append(newStr);
            } else if (part.contains("=")) {
                String[] tokens = part.split("=");
                String variableName = tokens[0].trim();
                String value = tokens[1].trim();
                String expressName = findExpressName(variableName);
                value = getStringWithSingleQuotationMark(value, expressName);
                mvelExpressionBuilder.append(expressName).append(" == ").append(value);
            } else if (part.contains("不为空")) {
                String[] tokens = part.split("不为空");
                String variableName = tokens[0].trim();
                String valuesString = tokens.length > 1 ? tokens[1].trim() : "";
                variableName = variableName.replace(":", "");
                variableName = variableName.replace("：", "");
                mvelExpressionBuilder.append(findExpressName(variableName)).append(" != ''").append(valuesString);
            } else if (part.contains("为空")) {
                String[] tokens = part.split("为空");
                String variableName = tokens[0].trim();
                String valuesString = tokens.length > 1 ? tokens[1].trim() : "";
                variableName = variableName.replace(":", "");
                variableName = variableName.replace("：", "");
                mvelExpressionBuilder.append(findExpressName(variableName)).append(" == ").append("''").append(valuesString);
            } else if (part.contains("不等于")) {
                String[] tokens = part.split("不等于");
                String variableName = tokens[0].trim();
                String value = tokens[1].trim();
                mvelExpressionBuilder.append(findExpressName(variableName)).append(" != ").append(value);
            } else if (part.contains("<>")) {
                String[] tokens = part.split("<>");
                String variableName = tokens[0].trim();
                String value = tokens[1].trim();

                String expressName = findExpressName(variableName);
                value = getStringWithSingleQuotationMark(value, expressName);

                mvelExpressionBuilder.append(expressName).append(" != ").append(value);
            } else if (part.contains("小于")) {
                String[] tokens = part.split("小于");
                String variableName = tokens[0].trim();
                String value = tokens[1].trim();
                mvelExpressionBuilder.append(findExpressName(variableName)).append(" < ").append(value);
            } else if (part.contains("大于")) {
                String[] tokens = part.split("大于");
                String variableName = tokens[0].trim();
                String value = tokens[1].trim();
                mvelExpressionBuilder.append(findExpressName(variableName)).append(" > ").append(value);
            } else if (part.contains("≥")) {
                String[] tokens = part.split("≥");
                String variableName = tokens[0].trim();
                String value = tokens[1].trim();
                mvelExpressionBuilder.append(findExpressName(variableName)).append(" >= ").append(value);
            } else if (part.contains("＞")) {
                String[] tokens = part.split("＞");
                String variableName = tokens[0].trim();
                String value = tokens[1].trim();
                mvelExpressionBuilder.append(findExpressName(variableName)).append(" > ").append(value);
            } else if (part.contains(">")) {
                String[] tokens = part.split(">");
                String variableName = tokens[0].trim();
                String value = tokens[1].trim();
                mvelExpressionBuilder.append(findExpressName(variableName)).append(" > ").append(value);
            }

            if (i != parts.length - 1) {
                mvelExpressionBuilder.append(operate);
            }
        }
    }

    // 补全左右括号
    private static String fillQuote(String part, StringBuilder subMvelExpressionBuilder) {
        int initLeftQuoteSize = countOccurrences(part, '(');
        int initRightQuoteSize = countOccurrences(part, ')');
        int initDiff = initLeftQuoteSize - initRightQuoteSize;
        int endLeftQuoteSize = countOccurrences(subMvelExpressionBuilder.toString(), '(');
        int endRightQuoteSize = countOccurrences(subMvelExpressionBuilder.toString(), ')');
        int endDiff = endLeftQuoteSize - endRightQuoteSize;
        String newStr = "";
        if (initDiff == endDiff) {
            newStr = subMvelExpressionBuilder.toString();
        } else if (initDiff > endDiff) {
            newStr = appendCharacters(1, subMvelExpressionBuilder.toString(), '(', initDiff - endDiff);
        } else {
            newStr = appendCharacters(2, subMvelExpressionBuilder.toString(), ')', endDiff - initDiff);
        }
        return newStr;
    }

    // java判断str包含字符A的个数
    public static int countOccurrences(String str, char targetChar) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == targetChar) {
                count++;
            }
        }
        return count;
    }

    // 判断字符串包含指定字符的个数，如果不够，在字符串后面追加缺失个数
    public static String appendCharacters(int direction, String str, char targetChar, int count) {
        if (count == 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        // 前面追加
        if (1 == direction) {
            for (int i = 0; i < count; i++) {
                sb.append(targetChar);
            }
            sb.append(str);
        } else {
            // 后面追加
            sb.append(str);
            for (int i = 0; i < count; i++) {
                sb.append(targetChar);
            }
        }
        return sb.toString();
    }

    private static String getStringWithSingleQuotationMark(String value, String expressName) {
        if ("''".equals(value)) {
            return value;
        }
        if (expressName.endsWith("_VALUE")
                || Arrays.asList("currentIntention", "currentIntentionList", "currentAskSlot", "skipSlot",
                "(currentIntention", "(currentIntentionList", "(currentAskSlot", "(skipSlot").contains(expressName)) {
            value = getStringWithSingleQuotationMark(value);
        }
        return value;
    }

    private static String getStringWithSingleQuotationMark(String value) {
        String temp = value.replace("(", "").replace(")", "");
        String newTemp = "'" + temp + "'";
        value = value.replace(temp, newTemp);
        return value;
    }

    private static String getStringWithNoQuotationMark(String value) {
        String temp = value.replace("(", "").replace(")", "");
        String newTemp = "" + temp + "";
        value = value.replace(temp, newTemp);
        return value;
    }

    private static String findExpressName(String logicName) {
        String temp = logicName.replace("(", "").replace(")", "").trim();
        for (Map.Entry<String, String> entry : DEFAULT_PARAMETER_MAP.entrySet()) {
            if (entry.getValue().equals(temp)) {
                String key = entry.getKey();
                return logicName.replace(temp, key);
            }
        }
        NO_CONFIG_NAME.add(logicName);
        return "ERROR： " + logicName;
    }

    private static Map<String, String> getInitSlot() {
        Map<String, String> map = Maps.newHashMap();
        map.put("currentIntention", "意图");
        map.put("currentIntentionList", "多意图");
        map.put("currentAskSlot", "当前询问槽位");
        map.put("skipSlot", "跳过槽位");
        for (CheckTypeEnum e : CheckTypeEnum.values()) {
            map.put(e + "_VALUE", "槽位-" + e.getMsg());
            map.put(e + "_TIMES", e.getMsg() + "-询问次数");
        }
        return map;
    }
}






















