package com.test.rule;
import org.mvel2.MVEL;
import org.mvel2.ast.ASTNode;
import org.mvel2.compiler.CompiledExpression;
import org.mvel2.compiler.ExpressionCompiler;

import java.util.HashSet;
import java.util.Set;

public class MvelVariableExtractor {

    public static Set<String> extractVariables(String mvelExpression) {
        ExpressionCompiler compiler = new ExpressionCompiler(mvelExpression);
        CompiledExpression compiledExpression = compiler.compile();
        ASTNode rootNode = compiledExpression.getFirstNode();
        return extractVariablesFromNode(rootNode);
    }

    private static Set<String> extractVariablesFromNode(ASTNode node) {
        ASTNode firstNode = node.nextASTNode;
//        while(null != firstNode){
//            firstNode.get
//
//
//            firstNode = node.nextASTNode;
//        }
        Set<String> variables = new HashSet<>();
        if (node != null) {
//            if (node.getFields() != null && !node.getFields().isEmpty()) {
//                variables.addAll(node.getFields().keySet());
//            }
//            if (node.hasChildNodes()) {
//                for (ASTNode childNode : node.getChildNodes()) {
//                    variables.addAll(extractVariablesFromNode(childNode));
//                }
//            }
        }
        return variables;
    }

    public static void main(String[] args) {
        String mvelExpression = "currentAskSlot=='房屋类型' && currentIntention=='QA问答' && HOUSE_TYPE_TIMES==1 && HOUSE_TYPE_VALUE=='' && HOUSE_TYPE_MAKE_DETAIL_INQUIRY_1_TIMES==0";
        Set<String> variables = extractVariables(mvelExpression);
        System.out.println("MVEL 表达式中的变量名：");
        for (String variable : variables) {
            System.out.println(variable);
        }
    }
}
