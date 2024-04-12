package com.test.rule;


import com.google.common.base.Throwables;
import com.to8to.rpc.common.exception.RPCException;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.api.Condition;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.mvel.MVELCondition;
import org.mvel2.MVEL;

import java.io.Serializable;

/**
 * 重写：org.jeasy.rules.mvel.MVELCondition  还要写一大堆 暂时不处理吧
 *
 */
@Slf4j
public class MyMVELCondition implements Condition {

    private String expression;
    private Serializable compiledExpression;

    /**
     * Create a new {@link MVELCondition}.
     *
     * @param expression the condition written in expression language
     */
    public MyMVELCondition(String expression) {
        this.expression = expression;
        compiledExpression = MVEL.compileExpression(expression);
    }

    @Override
    public boolean evaluate(Facts facts) {
        try {
            return (boolean) MVEL.executeExpression(compiledExpression, facts.asMap());
        } catch (Exception e) {
            log.error("表达式执行异常，facts={}, expression={}, e={}", facts, expression, Throwables.getStackTraceAsString(e));
            throw new RPCException("表达式执行异常");
        }
    }
}
