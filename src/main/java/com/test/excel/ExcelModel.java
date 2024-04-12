package com.test.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExcelModel implements Serializable{
    @ExcelProperty(value="策略名称", index= 1)
    private String id;

    @ExcelProperty(value="规则条件",index=2)
    private String name;

    @ExcelProperty(value="跳转策略",index=3)
    private String origin;

    @ExcelProperty(value="询问槽位",index=4)
    private String probability;

    @ExcelProperty(value="话术内容",index=5)
    private String effect;

}

