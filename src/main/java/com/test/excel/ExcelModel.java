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
    @ExcelProperty(value="编号-初始列为1", index= 1)
    private String id;

    @ExcelProperty(value="名称",index=2)
    private String name;

    @ExcelProperty(value="来源",index=3)
    private String origin;

    @ExcelProperty(value="既率(P)",index=4)
    private String probability;

    @ExcelProperty(value="影响(I)",index=5)
    private String effect;

}

