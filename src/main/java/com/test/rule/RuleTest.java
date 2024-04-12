package com.test.rule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.mvel.MVELRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RuleTest {
    private static final String express_str = "currentAskSlot=='房屋类型' " +
            "&& currentIntentionList.contains('QA问答') " +
            "&& HOUSE_TYPE_TIMES==1 " +
            "&& HOUSE_TYPE_VALUE=='毛坯' " +
            "&& LAST_NAME_TIMES != 1";
    private static final String param_str = "{\"LAST_NAME_VALUE\":\"\",\"LAST_NAME_TIMES\":\"\",\"HOUSE_TYPE_MAKE_DETAIL_INQUIRY_1_TIMES\":\"\",\"currentIntention\":\"回复房屋信息\",\"HOUSE_TYPE_TIMES\":1,\"HOUSE_TYPE_VALUE\":\"毛坯\",\"DECORATION_USE_VALUE\":\"\",\"currentAskSlot\":\"房屋类型\"}";
    private static void test(){
        List<String> tt = new ArrayList<>();
        tt.add("回复房屋信息");
        tt.add("QA问答");
        // 创建事实对象
        Facts facts = new Facts();
        facts.put("currentAskSlot", "房屋类型");
        facts.put("currentIntentionList", tt);

        JSONObject param_json = (JSONObject)JSONObject.parse(param_str);
        param_json.forEach((key,value) ->{
            facts.put(key, value);
        });
        fire(facts, express_str);
    }

    public static void main(String[] args) {
        test();
//        Facts facts = new Facts();
//
//        facts.put("HOUSE_TYPE_TIMES", "1");
//        facts.put("currentAskSlot", "房屋类型");
//        facts.put("HOUSE_TYPE_VALUE", "毛坯");
//        facts.put("LAST_NAME_TIMES", "0");
//
//
//
//        facts.put("name", Arrays.asList("回复房屋信息", "QA问答", "旧房改造"));
//        facts.put("age", "1122");
//
//        Person person = new Person();
//        person.setBookName(Arrays.asList("11", "22", "33"));
//        facts.put("person", person);
//
//        // 执行规则引擎
//        fire(facts);
    }

    public static void fire(Facts facts) {
        String mvelExpression1 = "org.apache.commons.collections.CollectionUtils.isNotEmpty(person.bookName) " +
                "&& person.bookName.contains('22') " +
                "&& name == '张三' " +
                "&& age == 20";
        String mvelExpression2 = "HOUSE_TYPE_TIMES != 1 && (name.contains('毛坯2') || age.contains('111'))";
        //String mvelExpression2 = "name == '张三' && age == 21";
        String mvelExpression3 = "name == '李四' && age == 20";
        String mvelExpression4 = "name == '李四' && age == 21";
        String mvelExpression5 = "name.contains.('QA问答') && currentAskSlot=='房屋类型' " +
                " " +
                "&& HOUSE_TYPE_TIMES==1 " +
                "&& HOUSE_TYPE_VALUE=='毛坯' " +
                "&& LAST_NAME_TIMES != 1";

        int index = 10;
        // 创建规则引擎
        Rules rules = new Rules();
        Rule rule1 = new MVELRule().name("规则1").when(mvelExpression1).then("ruleName='规则1';result2=name+'-2'").priority(--index);
        Rule rule2 = new MVELRule().name("规则2").when(mvelExpression2).then("ruleName='规则2';result = name + '大于20'").priority(--index);
        Rule rule3 = new MVELRule().name("规则3").when(mvelExpression3).then("ruleName='规则3';result = name").priority(--index);
        Rule rule4 = new MVELRule().name("规则4").when(mvelExpression4).then("ruleName='规则4';result = name + '大于20'").priority(--index);
        Rule rule5 = new MVELRule().name("规则5").when(mvelExpression5).then("ruleName='规则5';result = name + '大于20'").priority(--index);
        rules.register(rule1);
        rules.register(rule2);
        rules.register(rule3);
        rules.register(rule4);
        rules.register(rule5);

        // 创建规则引擎
        RulesEngine rulesEngine = new DefaultRulesEngine();
        rulesEngine.getParameters().setSkipOnFirstAppliedRule(true);
        //TODO  还要成功写一大堆
        rulesEngine.getParameters().setSkipOnFirstFailedRule(true);

        // 执行规则引擎
        rulesEngine.fire(rules, facts);

        facts.forEach(key -> {
            System.out.println("执行结果：" + key);
        });
    }


    public static void fire(Facts facts, String mvelExpression1) {
        // 创建规则引擎
        Rules rules = new Rules();
        Rule rule1 = new MVELRule().name("规则1").when(mvelExpression1).then("ruleName='规则1';");
       rules.register(rule1);

        // 创建规则引擎
        RulesEngine rulesEngine = new DefaultRulesEngine();
        rulesEngine.getParameters().setSkipOnFirstAppliedRule(true);
        //TODO  还要成功写一大堆
        rulesEngine.getParameters().setSkipOnFirstFailedRule(true);

        // 执行规则引擎
        rulesEngine.fire(rules, facts);

        facts.forEach(key -> {
            System.out.println("执行结果：" + key);
        });
    }
}