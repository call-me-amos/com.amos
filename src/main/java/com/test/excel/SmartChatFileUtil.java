package com.test.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import io.netty.util.internal.ThrowableUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

@Slf4j
public class SmartChatFileUtil {
    public static Set<String> STANDARD_ANSWER = Sets.newHashSet("毛坯","旧房改造","精装房","北欧","新中式","日式","轻奢","简约",
            "半年后","半年内","3个月内","下个月","本月","出租","自己住","奶油","美式","北欧","新中式","日式","轻奢","简约");
    private static final List<String> ADVISER_NAMES = Arrays.asList("蒋斐宸", "张润武", "钟志贤", "朱佳美", "吴红洋", "王光元");



    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("============  start");
        writeToExcel("C:\\Users\\amos.tong\\Desktop\\条件跳转\\智能应答策略2.0.xlsx", "E:\\test\\temp.xlsx");

        // 智能客服聊天记录解析
        List<ChatInstance> chatInstanceList = parseSmartChat();

//        writeToExcel("E:\\test\\smartChatRecord-" + DateUtils.currentSeconds() + ".xlsx", chatInstanceList);
        print(chatInstanceList);
        System.out.println("===============");
    }

    private static List<ChatInstance> parseSmartChat() throws FileNotFoundException {
        File file = new File("E:\\test\\聊天记录-2023-03-29.xlsx");
        List<LinkedHashMap<Integer, String>> excelModelFromFileList = importExcel(new FileInputStream(file), 0);

        LinkedHashMap<Integer, String> firstRowForChatId = excelModelFromFileList.get(0);
        int maxColumn = firstRowForChatId.size();
        // 初始化excel的内存对象.以第一行为准，第一行的长度就是总共有多少列
        List<ChatInstance> excelModelForMemoryList = new ArrayList<>();

        // 【1】先读第一行，获取群id
        for (int column = 0; column < maxColumn; column++) {
            ChatInstance chatInstance = new ChatInstance();
            excelModelForMemoryList.add(chatInstance);
            // 设置每一列的群id
            chatInstance.setChatId(firstRowForChatId.get(column));
            chatInstance.setChatRecordList(new ArrayList<>());
        }

        // 【2】再读第二行，获取机器人名称和群创建的时间
        LinkedHashMap<Integer, String> secondRowForRobotName = excelModelFromFileList.get(1);
        for (int column = 0; column < maxColumn; column++) {
            ChatInstance chatInstance = excelModelForMemoryList.get(column);
            String valueSecond = secondRowForRobotName.get(column);
            if (StringUtils.isEmpty(valueSecond)){
                // 异常数据
                continue;
            }
            chatInstance.setRobotName(valueSecond.split(" ")[0].trim());
            chatInstance.setGroupCreateTimeStr(valueSecond.substring(valueSecond.split(" ")[0].length()).trim());
        }

        // 【4】后面两行两行的读，第一行用来判断是不是顾问，如果不是就是用户，第二行就是谁说的。
        for (int rowNum = 3; rowNum < excelModelFromFileList.size(); rowNum = rowNum + 2) {
            LinkedHashMap<Integer, String> firstRow = excelModelFromFileList.get(rowNum);
            LinkedHashMap<Integer, String> secondRow = excelModelFromFileList.get(rowNum + 1);

            for (int columnNum = 0; columnNum < maxColumn; columnNum++) {
                // 坐标为：（rowNum，columnNum）的单元格数据
                ChatInstance chatInstance = excelModelForMemoryList.get(columnNum);
                // 第一行：设计师助理-小张 2023-03-07 18:09:47
                String firstRowValue = firstRow.get(columnNum);
                // 第二行：收到，亲，您是毛坯房还是旧房改造呀
                String secondRowValue = secondRow.get(columnNum);
                if (null != firstRowValue && !"".equals(firstRowValue)) {
                    ChatRecord chatRecord = new ChatRecord();
                    String firstRowName = firstRowValue.split(" ")[0].trim();
                    String firstRowTime = firstRowValue.substring(firstRowName.length()).trim();
                    chatRecord.setWhoSaid(firstRowName);
                    chatRecord.setTime(firstRowTime);
                    chatRecord.setMessage(secondRowValue);
                    if (chatInstance.getRobotName().equals(firstRowName)) {
                        // 机器人说的话
                        chatRecord.setType(1);

                        // 转人工标志
                        if ("已邀请您对用户进行人工服务".equals(secondRowValue)){
                            chatInstance.setTransferToManualTimeStr(firstRowTime);
                        }
                    } else if (ADVISER_NAMES.contains(firstRowName)) {
                        // 顾问说话
                        chatRecord.setType(3);

                        // 顾问第一次回复的时间
                        if (null == chatInstance.getAdviserName()){
                            chatInstance.setAdviserName(firstRowName);
                            chatInstance.setAdviserFirstReplyTimeStr(firstRowTime);
                        } else {
                            chatInstance.setAdviserLastReplyTimeStr(firstRowTime);
                        }
                    } else {
                        // 用户说的话
                        chatRecord.setType(2);

                        // 用户第一次回复
                        if (null == chatInstance.getUserName()) {
                            chatInstance.setUserName(firstRowName);
                            chatInstance.setUserFirstReplyTimeStr(firstRowTime);
                        } else {
                            chatInstance.setUserLastReplyTimeStr(firstRowTime);
                        }
                    }
                    chatInstance.getChatRecordList().add(chatRecord);

                    // 标准答案
                    if (STANDARD_ANSWER.contains(secondRowValue)){
                        // 没有转人工
                        if (StringUtils.isEmpty(chatInstance.getTransferToManualTimeStr())){
                            chatInstance.setReplyNum(chatInstance.getReplyNum() + 1);
                        }
                    }
                }
            }
        }
        return excelModelForMemoryList;
    }

    private static void writeToExcel(String toFilePath, List<ChatInstance> excelModelForMemoryList){
        ChatInstance head = new ChatInstance();

        head.setChatId("群id");
        head.setRobotName("机器人名称");
        head.setUserName("用户名称");
        head.setAdviserName("顾问名称");
        head.setGroupCreateTimeStr("群创建时间");
        head.setTransferToManualTimeStr("转人工时间");
        head.setUserFirstReplyTimeStr("用户第一次回复时间");
        head.setUserLastReplyTimeStr("用户最后回复时间");
        head.setAdviserFirstReplyTimeStr("顾问最后回复时间");
        head.setAdviserLastReplyTimeStr("顾问最后回复时间");
        head.setReplyNum(0);




        List<ChatInstance> newChatInstanceList =new ArrayList<>();
        newChatInstanceList.add(head);
        newChatInstanceList.addAll(excelModelForMemoryList);

        newChatInstanceList.forEach(e->{
            e.setChatRecordList(null);
        });

        List<List<String>> heads = new ArrayList<>();
//        heads.add(Lists.newArrayList("序号"));
//        heads.add(Lists.newArrayList("群id"));
//        heads.add(Lists.newArrayList("机器人名称"));
//        heads.add(Lists.newArrayList("用户名称"));
//        heads.add(Lists.newArrayList("顾问名称"));
//        heads.add(Lists.newArrayList("群创建时间"));
//        heads.add(Lists.newArrayList("转人工时间"));
//        heads.add(Lists.newArrayList("用户第一次回复时间"));
//        heads.add(Lists.newArrayList("用户最后回复时间"));
//        heads.add(Lists.newArrayList("顾问第一次回复时间"));
//        heads.add(Lists.newArrayList("顾问最后回复时间"));
//        heads.add(Lists.newArrayList("转人工前回复标准问题数"));

        EasyExcel.write(toFilePath, JSONObject.class).head(heads).excelType(ExcelTypeEnum.XLSX).sheet("模板-01").doWrite(newChatInstanceList);
        System.out.println("=========    writeToExcel    ================");
    }
    private static void print(List<ChatInstance> excelModelForMemoryList){
        excelModelForMemoryList.forEach(e -> {
            // System.out.println("当前聊天对话： 群id："+e.getChatId()+"-"+e.getRobotName()+"-"+e.getUserName());
            e.getChatRecordList().forEach(chatRecord -> {
                if ("2".equals(chatRecord.getType().toString())) {
//                    System.out.println(chatRecord);
                      System.out.println(e.getChatId() + "====" +chatRecord.getTime() + "======" + chatRecord.getMessage());
                }
            });
        });
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

    /**
     * @param classType com.test.excel.ExcelModel
     * @param rowNumber 从第几行开始解析
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(InputStream inputStream, Class<T> classType, int rowNumber) {
        List<T> list;
        try {
            list = EasyExcel.read(inputStream, classType, null).sheet().headRowNumber(rowNumber).doReadSync();
        } catch (Exception e) {
            log.error("e============{}", ThrowableUtil.stackTraceToString(e));
            throw new RuntimeException();
        }
        return list;

    }

    public static void writeToExcel(String fromFilePath, String toFilePath) throws FileNotFoundException {
        File file = new File(fromFilePath);
        List<LinkedHashMap<Integer, String>> excelModelFromFileList = importExcel(new FileInputStream(file), 0);

        EasyExcel.write(toFilePath, JSONObject.class).excelType(ExcelTypeEnum.XLSX).sheet("模板-01").doWrite(excelModelFromFileList);
//        //如果这里想使用3则传入excelType参数即可
//        EasyExcel.write(fileName, ExcelStudentData.class).excelType(ExcelTypeEnum.XLSX).sheet("模板-01").doWrite(data());

    }

    private static List<ToExcelData> data() {
        List<ToExcelData> list = new ArrayList<>();
        for (int i = 0; i < 65535; i++) {
            ToExcelData data = new ToExcelData();
            data.setBirthday(new Date());
            data.setSalary(0.56);
            list.add(data);
        }
        return list;
    }
}






















