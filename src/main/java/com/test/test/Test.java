package com.test.test;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) {
        List<SmartChatConversationDetail> smartChatConversationDetailList = new ArrayList<>();
        SmartChatConversationDetail a1 = new SmartChatConversationDetail();
        a1.setCheckTypeCode("a1");
        a1.setExecuteStatus(11);
        SmartChatConversationDetail a2 = new SmartChatConversationDetail();
        a2.setCheckTypeCode("a2");
        a2.setExecuteStatus(0);
        SmartChatConversationDetail a3 = new SmartChatConversationDetail();
        a3.setCheckTypeCode("a3");
        a3.setExecuteStatus(3);
        SmartChatConversationDetail a4 = new SmartChatConversationDetail();
        a4.setCheckTypeCode("a4");
        a4.setExecuteStatus(66);
        smartChatConversationDetailList.add(a1);
        smartChatConversationDetailList.add(a2);
        smartChatConversationDetailList.add(a3);
        smartChatConversationDetailList.add(a4);



        List<String> robotAskCheckTypeCodeList = smartChatConversationDetailList.stream().filter(detail->
                ExecuteStatusEnum.SEND_SUCCESS.getCode().equals(detail.getExecuteStatus()) || ExecuteStatusEnum.SENT.getCode().equals(detail.getExecuteStatus()))
                .map(SmartChatConversationDetail::getCheckTypeCode).collect(Collectors.toList());
        robotAskCheckTypeCodeList.forEach(e->{
            System.out.println("e="+e);
        });
    }
}
@Data
class SmartChatConversationDetail{
    private Integer executeStatus;
    private String checkTypeCode;
}
@AllArgsConstructor
enum ExecuteStatusEnum {
    /**
     * 执行状态|0-待发送 1-已发送 2-已追问 3-发送失败 4-已失效 5-取消发送 6-发送成功
     */
    TO_BE_SENT(0, "待发送"),
    SENT(1, "已发送（发送到延迟队列）"),
    QUESTION_CLOSELY(2, "已追问（已作废）"),
    SEND_FAIL(3, "发送失败"),
    EXPIRED(4, "已失效"),
    CANCEL_SEND(5, "取消发送"),
    SEND_SUCCESS(6, "发送成功"),
    ;


    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    private Integer code;
    private String message;

    public static ExecuteStatusEnum of(Integer type) {
        for (ExecuteStatusEnum statusEnum : ExecuteStatusEnum.values()) {
            if (statusEnum.code.equals(type)) {
                return statusEnum;
            }
        }
        return null;
    }

}