package com.test.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.List;

@Data
class ChatInstance {
    /** 群id  */
    @ExcelProperty(value="群id", index= 1)
    private String chatId="";
    /** 机器人名称  */
    @ExcelProperty(value="机器人名称", index= 2)
    private String robotName="";
    /** 用户名称  */
    @ExcelProperty(value="用户名称", index= 3)
    private String userName="";
    /** 顾问名称  */
    @ExcelProperty(value="顾问名称", index= 4)
    private String adviserName="";
    @ExcelProperty(value="群创建时间", index= 5)
    private String groupCreateTimeStr="";
    @ExcelProperty(value="转人工时间", index= 6)
    private String transferToManualTimeStr="";
    @ExcelProperty(value="用户第一次回复时间", index= 7)
    private String userFirstReplyTimeStr="";
    @ExcelProperty(value="用户最后回复时间", index= 8)
    private String userLastReplyTimeStr="";
    @ExcelProperty(value="顾问第一次回复时间", index= 9)
    private String adviserFirstReplyTimeStr="";
    @ExcelProperty(value="顾问最后回复时间", index= 10)
    private String adviserLastReplyTimeStr="";
    /** 转人工前回复标准问题数  */
    @ExcelProperty(value="转人工前回复标准问题数", index= 11)
    private int replyNum=0;

    /** 聊天记录  */
    private List<ChatRecord> chatRecordList;

}
