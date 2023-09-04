package com.test.excel;

import lombok.Data;

@Data
class ChatRecord {
    private String message;
    private String time;
    private String whoSaid;
    /**
     * 1：机器人
     * 2：用户
     * 3：顾问
     */
    private Integer type;

    @Override
    public String toString() {
        return "time=" + time + "   " + ",whoSaid =" + whoSaid + "  , message    =" + message;
    }
}
