package com.test.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.test.smart.CheckTypeEnum;
import io.netty.util.internal.ThrowableUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

@Slf4j
public class ForRuleConditionMain {
    private static final Map<String, String> PARAMETER_MAP = getInitSlot();
    private static final Set<String> NO_CONFIG_NAME = Sets.newHashSet();


    /**
     * INSERT INTO tls_smart_chat_diy_rule (rule_name, rule_condition, next_strategy_type, next_ask_slot, template_id) VALUES
     * <p>
     * ('第一句话', 'HOUSE_TYPE_times=0', 2, '7!711!71102!4', 'TAG20240409000001'),
     * ('未识别到意图', 'intention=''其他''', 0, '', 'TAG20240409000001');
     *
     */
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("============  start");
        writeToExcel("C:\\Users\\amos.tong\\Desktop\\条件跳转\\智能应答策略2.0-3.xlsx", "C:\\Users\\amos.tong\\Desktop\\条件跳转\\temp.xlsx");

        System.out.println(JSONObject.toJSONString(NO_CONFIG_NAME));
        System.out.println("===============");
    }


    /**
     * @param rowNumber 从第几行开始解析
     * @return 返回每一行
     */
    public static List<LinkedHashMap<Integer, String>> importExcel(InputStream inputStream, int rowNumber) {
        List<LinkedHashMap<Integer, String>> list;
        try {
            list = EasyExcel.read(inputStream, null).sheet().headRowNumber(rowNumber).doReadSync();
        } catch (Exception e) {
            log.error("e============{}", ThrowableUtil.stackTraceToString(e));
            throw new RuntimeException();
        }
        return list;
    }

    public static void writeToExcel(String fromFilePath, String toFilePath) throws FileNotFoundException {
        File file = new File(fromFilePath);
        List<LinkedHashMap<Integer, String>> excelModelFromFileList = importExcel(new FileInputStream(file), 0);

        List<ToExcelData> toExcelDataList = new ArrayList<>();
        excelModelFromFileList.forEach(row -> {
            StringBuffer sb = new StringBuffer();
            sb.append("('");
            sb.append(row.get(0));
            sb.append("', '");
            sb.append(convertToMvelExpression(row.get(1)));// 条件
            sb.append("', '");
            sb.append(row.get(2));
            sb.append("','");
            sb.append(row.get(3));
            sb.append("'),");
            // TODO 话术可以拼接 curl

            System.out.println(sb);
            ToExcelData data = new ToExcelData();
            data.setSql(sb.toString());
            toExcelDataList.add(data);
        });

        EasyExcel.write(toFilePath, ToExcelData.class).excelType(ExcelTypeEnum.XLSX).sheet("模板-01").doWrite(toExcelDataList);

    }


    private static String convertToMvelExpression(String logicDescription) {
        String[] lines = logicDescription.split("\\r?\\n");
        StringBuilder mvelExpressionBuilder = new StringBuilder();

        for (String line : lines) {
            String[] parts = line.split(" and ");
            for (int i = 0; i < parts.length; i++) {
                String part = parts[i];
                if (part.contains("in")) {
                    String[] tokens = part.split(" in ");
                    String variableName = tokens[0].trim();
                    String valuesString = tokens[1].trim();
                    List<String> values;
                    if(valuesString.contains("、")){
                        values = Arrays.asList(valuesString.substring(1, valuesString.length() - 1).split("、"));
                    } else if (valuesString.contains("，")){
                        values = Arrays.asList(valuesString.substring(1, valuesString.length() - 1).split("，"));
                    } else {
                        values = Arrays.asList(valuesString);
                    }
                    mvelExpressionBuilder.append(findExpressName(variableName)).append(" in (");
                    for (String value : values) {
                        mvelExpressionBuilder.append("\\'").append(value).append("\\', ");
                    }
                    mvelExpressionBuilder.delete(mvelExpressionBuilder.length() - 2, mvelExpressionBuilder.length());
                    mvelExpressionBuilder.append(")");
                } else if (part.contains("=")) {
                    String[] tokens = part.split("=");
                    String variableName = tokens[0].trim();
                    String value = tokens[1].trim();
                    mvelExpressionBuilder.append(findExpressName(variableName)).append(" == ").append(value);
                } else if (part.contains("不为空")) {
                    String variableName = part.substring(0, part.indexOf("不为空")).trim();
                    variableName = variableName.replace(":","");
                    variableName = variableName.replace("：","");
                    mvelExpressionBuilder.append(findExpressName(variableName)).append(" != ''");
                } else if (part.contains("为空")) {
                    String variableName = part.substring(0, part.indexOf("为空")).trim();
                    variableName = variableName.replace(":","");
                    variableName = variableName.replace("：","");
                    mvelExpressionBuilder.append(findExpressName(variableName)).append(" == ''");
                } else if (part.contains("不等于")) {
                    String[] tokens = part.split("不等于");
                    String variableName = tokens[0].trim();
                    String value = tokens[1].trim();
                    mvelExpressionBuilder.append(findExpressName(variableName)).append(" != ").append(value);
                } else if (part.contains("<>")) {
                    String[] tokens = part.split("<>");
                    String variableName = tokens[0].trim();
                    String value = tokens[1].trim();
                    mvelExpressionBuilder.append(findExpressName(variableName)).append(" != ").append(value);
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
                }

                if (i != parts.length - 1) {
                    mvelExpressionBuilder.append(" && ");
                }
            }
           // mvelExpressionBuilder.append("\n");
        }

        return mvelExpressionBuilder.toString();
    }


    private static String findExpressName(String logicName) {
        for (Map.Entry<String, String> entry : PARAMETER_MAP.entrySet()) {
            if (entry.getValue().equals(logicName)) {
                return entry.getKey();
            }
        }
        NO_CONFIG_NAME.add(logicName);
        return "ERROR：" + logicName;
    }

    private static Map<String, String> getInitSlot() {
        Map<String, String> map = Maps.newHashMap();
        map.put("currentIntention", "意图");
        map.put("currentIntentionList", "多意图");
        map.put("currentAskSlot", "本轮询问槽位");

        for (CheckTypeEnum e : CheckTypeEnum.values()) {
            map.put(e + "_VALUE", "槽位-" + e.getMsg());
            map.put(e + "_TIMES", e.getMsg()+ "-询问次数");
        }

        return map;
    }

}





















