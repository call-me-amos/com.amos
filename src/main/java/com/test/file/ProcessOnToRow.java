package com.test.file;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import com.test.excel.ForRuleConditionMain;
import com.test.file.data.ProcessOnNode;
import com.test.smart.CheckTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
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
     * pos文件解析规则成excel行格式的规则
     */
    public static final List<LinkedHashMap<Integer, String>> EXCEL_MODEL_FROM_PROCESS_ON = new ArrayList<>();

    /**
     * 根据pos文件解析到的话术对象
     * key：核需编码
     * key：replyList和noResponseList
     */
    public static final Map<String, Map<String, List<JSONObject>>> ROBOT_ASK_LIST = new HashMap<>();

    private static final AtomicInteger RULE_INDEX = new AtomicInteger();
    /**
     * 跳过槽位，类似于根节点
     */
    private static String skipSlot = "";

    public static void main(String[] args) {
//        initExcelModelFromProcessOn();
//        System.out.println(JSONObject.toJSONString(EXCEL_MODEL_FROM_PROCESS_ON));
//        System.out.println(JSONObject.toJSONString(ROBOT_ASK_LIST));
    }

    public static final LinkedHashMap<String, String> SECOND_NODE_TITLE_MAPPING = new LinkedHashMap<>();

    // AI 外呼
//    static {
//        SECOND_NODE_TITLE_MAPPING.put("开场问候", "开场问候");
//        SECOND_NODE_TITLE_MAPPING.put("开场白-澄清槽位", "开场白-澄清槽位");
//        SECOND_NODE_TITLE_MAPPING.put("仅加微-是否同意加微", "仅加微-是否同意加微");
//        SECOND_NODE_TITLE_MAPPING.put("仅加微-是否找到服务通知", "仅加微-是否找到服务通知");
//        SECOND_NODE_TITLE_MAPPING.put("仅加微-是否服务通知加微成功", "仅加微-是否服务通知加微成功");
//        SECOND_NODE_TITLE_MAPPING.put("仅加微-是否收到短信", "仅加微-是否收到短信");
//        SECOND_NODE_TITLE_MAPPING.put("仅加微-是否短信加微成功", "仅加微-是否短信加微成功");
//        SECOND_NODE_TITLE_MAPPING.put("仅加微-结束语", "仅加微-结束语");
//        SECOND_NODE_TITLE_MAPPING.put("公共策略", "公共策略");
//    }
    // 自定义规则
    static {
        SECOND_NODE_TITLE_MAPPING.put("公共策略", "");
        SECOND_NODE_TITLE_MAPPING.put("开场白", "开场白-澄清槽位");
        SECOND_NODE_TITLE_MAPPING.put("装修时间-初轮", "装修时间-初轮");
        SECOND_NODE_TITLE_MAPPING.put("房屋类型", "房屋类型");
        // SECOND_NODE_TITLE_MAPPING.put("需求类型-装修/定制", "需求类型-装修/定制");
        SECOND_NODE_TITLE_MAPPING.put("工程量", "工程量");
        SECOND_NODE_TITLE_MAPPING.put("装修用途", "装修用途");
        SECOND_NODE_TITLE_MAPPING.put("城市", "城市");
        SECOND_NODE_TITLE_MAPPING.put("是否交房", "是否交房");
        SECOND_NODE_TITLE_MAPPING.put("交房时间", "交房时间");
        SECOND_NODE_TITLE_MAPPING.put("房子交房前能否提前进去看", "房子交房前能否提前进去看");
        SECOND_NODE_TITLE_MAPPING.put("意向量房时间", "意向量房时间");
        SECOND_NODE_TITLE_MAPPING.put("小区名称", "小区名称");
        SECOND_NODE_TITLE_MAPPING.put("房屋面积", "房屋面积");
        SECOND_NODE_TITLE_MAPPING.put("装修时间-引导", "装修时间-引导");
        // SECOND_NODE_TITLE_MAPPING.put("电话", "电话");
        SECOND_NODE_TITLE_MAPPING.put("姓氏", "姓氏");
    }

    public static void initExcelModelFromProcessOn() {
        JSONObject jsonObject = getJsonFromFile();
        JSONArray childrenArray = jsonObject.getJSONObject("diagram").getJSONObject("elements").getJSONArray("children");
        childrenArray.forEach(o -> {
            ProcessOnNode secondNode = JSONObject.parseObject(String.valueOf(o), ProcessOnNode.class);
            String title = secondNode.getTitle()
                    .replace("&nbsp;", "")
                    .replace("<br>", "").trim();
            skipSlot = SECOND_NODE_TITLE_MAPPING.get(title);
            if (null == skipSlot) {
                //log.info("其他槽位，暂时不处理, secondNode={}", JSONObject.toJSONString(secondNode));
                return;
            }

            if (CollectionUtils.isNotEmpty(secondNode.getChildren())) {
                sortForConditionProcessOn(secondNode);
                secondNode.getChildren().forEach(children -> {
                    StringBuilder finalFatherRule1 = new StringBuilder();
                    parseNode(children, finalFatherRule1);
                });
            }

        });

//        excelModelFromProcessOn.forEach(e->{
//            log.info("{}", JSONObject.toJSONString(e));
//        });


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
            return jsonObject;
        } catch (IOException e) {
            log.error("e={}", Throwables.getStackTraceAsString(e));
            return null;
        }
    }

    public static void parseNode(ProcessOnNode node, StringBuilder fatherRule) {
        String title = node.getTitle();
        title = title.replaceAll("（", "(");
        title = title.replaceAll("）", ")");
        title = title.replace("&nbsp;", "");
        title = title.replace("<br>", "");
        title = title.trim();
        if (title.startsWith("备注") || title.startsWith("回复")) {
            if (CollectionUtils.isNotEmpty(node.getChildren())) {
                sortForConditionProcessOn(node);
                node.getChildren().forEach(children -> {
                    StringBuilder finalFatherRule1 = new StringBuilder(fatherRule);
                    parseNode(children, finalFatherRule1);
                });
            }
        } else if (title.startsWith("跳转策略：")) {
            String nextStrategyType = title.replace("跳转策略：", "").trim();
            nextStrategyType = nextStrategyType
                    .replaceAll("；", ";").trim()
                    .replace("跳转策略：", "")
                    .replace("跳转策略:", "")
                    .replace("肯定;", "").replace("肯定；", "")
                    .replace("回复QA;", "").replace("回复QA；", "");

            List<String> nextStrategyTypeList = Lists.newArrayList();
            List<String> nextAskSlotList = Lists.newArrayList();
            for (String nextStrategy: nextStrategyType.split(";")) {
                String nextAskSlot = null;
                if ("下一槽位".equals(nextStrategy)
                        || "转人工".equals(nextStrategy)
                        || "闭环".equals(nextStrategy)
                        || "/".equals(nextStrategy)) {
                    nextAskSlot = "/";
                } else {
                    nextAskSlot = nextStrategy;
                    nextStrategy = "指定跳转";
                }

                nextStrategyTypeList.add(nextStrategy);
                nextAskSlotList.add(nextAskSlot);
            }

            String ruleCondition = StringUtils.isEmpty(skipSlot) ? fatherRule.toString() : "(跳过槽位 <> " + skipSlot + ") and " + fatherRule;
            // 新增一个叶子结点：规则名称	规则条件	跳转策略	跳转槽位    核需话术    超时话术
            LinkedHashMap<Integer, String> leafRule = new LinkedHashMap<>();
            int index = RULE_INDEX.incrementAndGet();
            //规则名称 暂时用id替代
            leafRule.put(0, index + "-" + node.getId());
            //规则条件
            leafRule.put(1, ruleCondition);
            //跳转策略
            leafRule.put(2, JSONObject.toJSONString(nextStrategyTypeList));
            //跳转槽位
            leafRule.put(3, JSONObject.toJSONString(nextAskSlotList));
            EXCEL_MODEL_FROM_PROCESS_ON.add(leafRule);

            if (CollectionUtils.isNotEmpty(node.getChildren())) {
                String nextAskSlot = CollectionUtils.isEmpty(nextAskSlotList)? "": nextAskSlotList.get(0);
                parseForRobotAskList(nextAskSlot, node.getChildren());
                sortForConditionProcessOn(node);
                node.getChildren().forEach(childrenNode -> {
                    StringBuilder finalFatherRule = new StringBuilder();
                    parseNode(childrenNode, finalFatherRule);
                });
            }
        } else if (title.startsWith("条件")) {
            if (StringUtils.isNotEmpty(fatherRule.toString())) {
                fatherRule.append(" and ");
            }
            String subExp = "";
            try {
                String sub = title.replace(":", "：");
                subExp = sub.substring(sub.indexOf("：") + 1);
            } catch (Exception e) {
                subExp = title;
            }
            fatherRule.append(" (" + subExp + ")");
            if (CollectionUtils.isNotEmpty(node.getChildren())) {
                sortForConditionProcessOn(node);
                node.getChildren().forEach(children -> {
                    StringBuilder finalFatherRule = new StringBuilder(fatherRule);
                    parseNode(children, finalFatherRule);
                });
            } else {
                log.error("条件后面没有节点 node={}", JSONObject.toJSONString(node));
            }
        } else {
            //log.info("非标准节点，不处理 node={}", JSONObject.toJSONString(node));
        }
    }

    /**
     * 核需话术和超时话术排序
     */
    private static void sortForRobotAskProcessOn(ProcessOnNode node) {
        node.getChildren().sort((o1, o2) -> {
            int sort1 = 0;
            if (o1.getTitle().startsWith("延迟时间")) {
                try {
                    sort1 = Integer.parseInt(o1.getTitle().split("=")[1]);
                } catch (Exception e) {
                    log.info("配置错误{}", o1.getTitle());
                }
            }

            int sort2 = 0;
            if (o2.getTitle().startsWith("延迟时间")) {
                try {
                    sort2 = Integer.parseInt(o2.getTitle().split("=")[1]);
                } catch (Exception e) {
                    log.info("配置错误{}", o2.getTitle());
                }
            }
            return sort1 - sort2;
        });
    }

    /**
     * 条件排序
     */
    private static void sortForConditionProcessOn(ProcessOnNode node) {
        node.getChildren().sort((o1, o2) -> {
            int sort1 = 0;
            if (o1.getTitle().startsWith("条件")) {
                try {
                    String sub = o1.getTitle().replace(":", "：");
                    sort1 = Integer.parseInt(sub.substring(2, sub.indexOf("：")));
                } catch (Exception e) {
                    log.info("配置错误{}", o1.getTitle());
                }
            }

            int sort2 = 0;
            if (o2.getTitle().startsWith("条件")) {
                try {
                    String sub = o2.getTitle().replace(":", "：");
                    sort2 = Integer.parseInt(sub.substring(2, sub.indexOf("：")));
                } catch (Exception e) {
                    log.info("配置错误{}", o2.getTitle());
                }
            }
            return sort1 - sort2;
        });
    }

    /**
     * 解析跳转策略后面的话术
     */
    private static void parseForRobotAskList(String nextAskSlot, List<ProcessOnNode> children) {
        Map<String, List<JSONObject>> robotAsk = new HashMap<>();
        for (ProcessOnNode node : children) {
            if (node.getTitle().startsWith("核需话术")) {
                List<JSONObject> replyList = new ArrayList<>();
                sortForRobotAskProcessOn(node);
                node.getChildren().forEach(subNode -> {
                    JSONObject reply = new JSONObject();
                    String delayTime = subNode.getTitle().split("=")[1].replace("<br>", "");
                    reply.put("delayTime", delayTime);
                    if (subNode.getChildren().get(0).getTitle().startsWith("话术内容")) {
                        String content = subNode.getChildren().get(0).getTitle()
                                .replace("话术内容：", "")
                                .replaceAll("&nbsp;", "").trim()
                                .replace("<br>", "").trim();

                        if (content.contains("现场量房先帮您延期预约到")) {
                            System.out.println("=====");
                        }
                        reply.put("content", content);
                        reply.put("type", 0);
                        replyList.add(reply);
                    } else if (subNode.getChildren().get(0).getTitle().startsWith("素材ID")) {
                        String content = subNode.getChildren().get(0).getTitle()
                                .replace("素材ID：", "")
                                .replaceAll("&nbsp;", "").trim()
                                .replace("<br>", "").trim();
                        reply.put("content", content);
                        reply.put("type", 3);
                        replyList.add(reply);
                    }
                });
                robotAsk.put("replyList", replyList);
            } else if (node.getTitle().startsWith("超时话术")) {
                List<JSONObject> noResponseList = new ArrayList<>();
                sortForRobotAskProcessOn(node);
                node.getChildren().forEach(subNode -> {
                    JSONObject reply = new JSONObject();
                    String delayTime = subNode.getTitle().split("=")[1].replace("<br>", "").trim();
                    reply.put("delayTime", delayTime);
                    if (subNode.getChildren().get(0).getTitle().startsWith("话术内容")) {
                        String content = subNode.getChildren().get(0).getTitle()
                                .replace("话术内容：", "")
                                .replaceAll("&nbsp;", "").trim()
                                .replace("<br>", "").trim();
                        reply.put("content", content);
                        reply.put("type", 0);
                        noResponseList.add(reply);
                    } else if (subNode.getChildren().get(0).getTitle().startsWith("素材ID")) {
                        String content = subNode.getChildren().get(0).getTitle()
                                .replace("素材ID：", "")
                                .replaceAll("&nbsp;", "").trim()
                                .replace("<br>", "").trim();
                        reply.put("content", content);
                        reply.put("type", 3);
                        noResponseList.add(reply);
                    } else if (subNode.getChildren().get(0).getTitle().startsWith("子槽位")) {
                        String subCheckTypeCode = subNode.getChildren().get(0).getTitle()
                                .replace("子槽位：", "")
                                .replaceAll("&nbsp;", "").trim()
                                .replace("<br>", "").trim();
                        reply.put("subCheckTypeCode", CheckTypeEnum.getByName(subCheckTypeCode).getCode());
                        String content = subNode.getChildren().get(0)
                                .getChildren().get(0).getTitle()
                                .replace("话术内容：", "")
                                .replaceAll("&nbsp;", "").trim()
                                .replace("<br>", "").trim();
                        reply.put("content", content);
                        reply.put("type", 5);
                        noResponseList.add(reply);
                    }

                });
                robotAsk.put("noResponseList", noResponseList);
            } else if (node.getTitle().startsWith("槽位赋值")) {
                // TODO  赋值的逻辑先简单写死实现，后面再考虑支持条件
                List<JSONObject> defaultReplyList = new ArrayList<>();
                String str1 = node.getChildren().get(0).getTitle()
                        .replace("<br>", "").trim();
                if (str1.contains("肯定回答")) {
                    String str2 = node.getChildren().get(0).getChildren().get(0).getTitle();
                    String value = str2.replace("赋值：", "").trim()
                            .replace("赋值:", "").trim()
                            .replaceAll("&nbsp;", "").trim()
                            .replace("<br>", "").trim();
                    JSONObject defaultReply = new JSONObject();
                    defaultReply.put("affNegIntentionName", "肯定回答");
                    defaultReply.put("content", value.trim());
                    defaultReply.put("type", 2);
                    defaultReplyList.add(defaultReply);
                }
                robotAsk.put("defaultReplyList", defaultReplyList);
            } else {
            }

            if (!robotAsk.isEmpty()) {
                nextAskSlot = nextAskSlot.split(";|；")[0];
                ROBOT_ASK_LIST.put(nextAskSlot, robotAsk);
            }
        }
    }
}
