package com.test.file;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import com.test.excel.ForRuleConditionMain;
import com.test.file.data.ProcessOnNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author amos.tong
 */
@Slf4j
public class ProcessOnToRow {
    /**
     * pos文件解析成excel行格式的规则
     */
    private static final List<LinkedHashMap<Integer, String>> excelModelFromProcessOn = new ArrayList<>();

    /**
     * key：核需编码
     *      key：replyList和noResponseList
     */
    private static final Map<String, Map<String, List<JSONObject>>> robotAskList = new HashMap<>();

    private static final AtomicInteger ruleIndex = new AtomicInteger();
    /**
     * 跳过槽位，类似于根节点
     */
    private static String skipSlot = "";

    public static void main(String[] args) {
        getExcelModelFromProcessOn();
    }

    public static List<LinkedHashMap<Integer, String>> getExcelModelFromProcessOn(){
        JSONObject jsonObject = getJsonFromFile();

        JSONArray childrenArray = jsonObject.getJSONObject("diagram").getJSONObject("elements").getJSONArray("children");

        childrenArray.forEach(o -> {
            ProcessOnNode secondNode = JSONObject.parseObject(String.valueOf(o), ProcessOnNode.class);
            if(secondNode.getTitle().startsWith("公共策略")
                    || secondNode.getTitle().startsWith("开场白")
                    || secondNode.getTitle().startsWith("房屋类型")){
                if(secondNode.getTitle().startsWith("开场白")){
                    skipSlot = "开场白-澄清槽位";
                } else if(secondNode.getTitle().startsWith("房屋类型")){
                    skipSlot = "房屋类型";
                } else {
                    skipSlot = "";
                }

                if(CollectionUtils.isNotEmpty(secondNode.getChildren())){
                    String finalFatherRule1 = "";
                    sortForProcessOn(secondNode);
                    secondNode.getChildren().forEach(children->{
                        parseNode(children, finalFatherRule1);
                    });
                }
            } else {
                //log.info("其他槽位，暂时不处理, secondNode={}", JSONObject.toJSONString(secondNode));
            }
        });

//        excelModelFromProcessOn.forEach(e->{
//            log.info("{}", JSONObject.toJSONString(e));
//        });


        LinkedHashMap<Integer, String> leafRule = new LinkedHashMap<>();
        leafRule.put(0, "99999-下一槽位默认调整槽位--测试使用");// 规则名称
        leafRule.put(1, "跳过槽位 <> ''");//规则条件
        leafRule.put(2, "指定跳转");//跳转策略
        leafRule.put(3, "姓氏");//跳转槽位
        //log.info("新增一条规则：leafRule={}", JSONObject.toJSONString(leafRule));
        excelModelFromProcessOn.add(leafRule);


        return excelModelFromProcessOn;
    }

    public static JSONObject getJsonFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ForRuleConditionMain.filePath))) {
            StringBuilder content = new StringBuilder();
            String line;
            // 逐行读取文件内容
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            // 将内容转换为 JSON
            JSONObject jsonObject = JSONObject.parseObject(content.toString());
            //System.out.println(jsonObject.toString());
            return jsonObject;
        } catch (IOException e) {
            log.error("e={}", Throwables.getStackTraceAsString(e));
            return null;
        }
    }

    public static void parseNode(ProcessOnNode node, String fatherRule){
        String title = node.getTitle();
        title = title.replace("&nbsp;", " ");
        title = title.replace("<br>", " ");
        title = title.trim();
        if(title.startsWith("备注") || title.startsWith("回复")){
//            if(title.startsWith("回复")){
//                System.out.println("xxx");
//            }
            if(CollectionUtils.isNotEmpty(node.getChildren())){
                String finalFatherRule1 = fatherRule;
                sortForProcessOn(node);
                node.getChildren().forEach(children->{
                    parseNode(children, finalFatherRule1);
                });
            }
        } else if(title.startsWith("跳转策略：")){
            String nextStrategyType = title.replace("跳转策略：", "").trim();
            nextStrategyType = nextStrategyType.replace("肯定+","");
            nextStrategyType = nextStrategyType.replace("回复QA+","");

            String nextAskSlot = "/";
            if(nextStrategyType.contains("下一槽位") || nextStrategyType.contains("转人工")
                    || nextStrategyType.contains("闭环") || nextStrategyType.contains("/")){
            }else{
                nextAskSlot = nextStrategyType;
                nextStrategyType = "指定跳转";
            }

            String ruleCondition = StringUtils.isEmpty(skipSlot)? fatherRule: "(跳过槽位 <> " + skipSlot + ") and "+fatherRule;
            // 新增一个叶子结点：规则名称	规则条件	跳转策略	跳转槽位    核需话术    超时话术
            LinkedHashMap<Integer, String> leafRule = new LinkedHashMap<>();
            int index = ruleIndex.incrementAndGet();
//            if(index == 42){
//                index = index+1-1;
//            }
            leafRule.put(0, index + "-"+node.getId());//规则名称 暂时用id替代
            leafRule.put(1, ruleCondition);//规则条件
            leafRule.put(2, nextStrategyType);//跳转策略
            leafRule.put(3, nextAskSlot);//跳转槽位
            //log.info("新增一条规则：leafRule={}", JSONObject.toJSONString(leafRule));
            excelModelFromProcessOn.add(leafRule);
            
            if(CollectionUtils.isNotEmpty(node.getChildren())){
                parseForRobotAskList(nextAskSlot, node.getChildren());
                sortForProcessOn(node);
                node.getChildren().forEach(childrenNode ->{
                    parseNode(childrenNode, "");
                });
            }
        } else if(title.startsWith("条件")){
            if(StringUtils.isNotEmpty(fatherRule)){
                fatherRule = fatherRule + " and ";
            }
            String subExp = "";
            try {
                Integer.parseInt(title.substring(2, 3));
                subExp = title.substring(4);
            } catch (Exception e){
                subExp = title.substring(3);
            }

            fatherRule = fatherRule + " (" + subExp + ")";
            if(CollectionUtils.isNotEmpty(node.getChildren())){
                String finalFatherRule = fatherRule;
                sortForProcessOn(node);
                node.getChildren().forEach(children->{
                    parseNode(children, finalFatherRule);
                });
            } else {
                log.error("条件后面没有节点 node={}", JSONObject.toJSONString(node));
            }
        } else {
            //log.info("非标准节点，不处理 node={}", JSONObject.toJSONString(node));
        }
    }

    private static void sortForProcessOn(ProcessOnNode node) {
        node.getChildren().sort(new Comparator<ProcessOnNode>() {
            @Override
            public int compare(ProcessOnNode o1, ProcessOnNode o2) {
                int sort1 = 0;
                if(o1.getTitle().startsWith("条件")){
                    try {
                        sort1 = Integer.parseInt(o1.getTitle().substring(2, 3));
                    } catch (Exception e){
                        log.info("配置错误{}", o1.getTitle());
                    }
                }

                int sort2 = 0;
                if(o2.getTitle().startsWith("条件")){
                    try {
                        sort2 = Integer.parseInt(o2.getTitle().substring(2, 3));
                    } catch (Exception e){
                        log.info("配置错误{}", o2.getTitle());
                    }
                }
                return sort1-sort2;
            }
        });
    }

    private static void parseForRobotAskList(String nextAskSlot, List<ProcessOnNode> children) {
        Map<String, List<JSONObject>> robotAsk = new HashMap<>();
        for (ProcessOnNode node: children) {
            if(node.getTitle().startsWith("核需话术")){
                List<JSONObject> replyList = new ArrayList<>();
                sortForProcessOn(node);
                node.getChildren().forEach(subNode->{
                    JSONObject reply = new JSONObject();
                    reply.put("delayTime", subNode.getTitle().split("=")[1]);
                    String content = subNode.getChildren().get(0).getTitle().replace("话术内容：", "");
                    reply.put("content", content);
                    replyList.add(reply);
                });
                robotAsk.put("replyList", replyList);
            } else if(node.getTitle().startsWith("超时话术")){
                List<JSONObject> noResponseList = new ArrayList<>();
                sortForProcessOn(node);
                node.getChildren().forEach(subNode->{
                    JSONObject reply = new JSONObject();
                    reply.put("delayTime", subNode.getTitle().split("=")[1]);
                    if(subNode.getChildren().get(0).getTitle().startsWith("话术内容")){
                        String content = subNode.getChildren().get(0).getTitle()
                                .replace("话术内容：", "");
                        reply.put("content", content);
                        noResponseList.add(reply);
                    } else if(subNode.getChildren().get(0).getTitle().startsWith("素材ID")){
                        String content = subNode.getChildren().get(0).getTitle()
                                .replace("素材ID：", "");
                        reply.put("content", content);
                        noResponseList.add(reply);
                    } else if(subNode.getChildren().get(0).getTitle().startsWith("子槽位")){
                        String subCheckTypeCode = subNode.getChildren().get(0).getTitle()
                                .replace("子槽位：", "");
                        reply.put("subCheckTypeCode", subCheckTypeCode);
                        String content = subNode.getChildren().get(0)
                                .getChildren().get(0).getTitle().replace("话术内容：", "");
                        reply.put("content", content);
                        noResponseList.add(reply);
                    }

                });
                robotAsk.put("noResponseList", noResponseList);
            } else{
            }

            if (!robotAsk.isEmpty()){
                robotAskList.put(nextAskSlot, robotAsk);
            }
        }
    }
}
