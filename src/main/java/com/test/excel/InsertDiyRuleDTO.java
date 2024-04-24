package com.test.excel;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author amos.tong
 */
@Data
public class InsertDiyRuleDTO {
    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 规则描述
     */
    private String ruleDescribe;

    /**
     * 规则条件
     */
    private String ruleCondition;

    /**
     * 跳转策略，参考枚举：NextStrategyEnum
     */
    private String nextStrategyType;

    /**
     * 下一个提问槽位
     */
    private String nextAskSlot;

    @NotNull
    private Integer templateId;
}
