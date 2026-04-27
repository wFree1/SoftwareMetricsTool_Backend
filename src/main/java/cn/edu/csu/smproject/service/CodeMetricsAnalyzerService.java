package cn.edu.csu.smproject.service;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class CodeMetricsAnalyzerService {

    /**
     * 动态分析 Java 代码字符串的各项指标
     */
    public Map<String, Integer> analyzeCodeString(String codeText) {
        Map<String, Integer> metrics = new HashMap<>();
        
        // 1. 计算代码行数 (LOC)
        int loc = codeText.split("\r\n|\r|\n").length;
        metrics.put("loc", loc);

        try {
            // 将纯文本代码解析为抽象语法树 (AST)
            CompilationUnit cu = StaticJavaParser.parse(codeText);

            // 2. 计算圈复杂度 (WMC - McCabe Cyclomatic Complexity)
            // 基础复杂度为 1，每次遇到分支节点 +1
            int[] wmc = {1};
            cu.findAll(IfStmt.class).forEach(n -> wmc[0]++);
            cu.findAll(ForStmt.class).forEach(n -> wmc[0]++);
            cu.findAll(ForEachStmt.class).forEach(n -> wmc[0]++);
            cu.findAll(WhileStmt.class).forEach(n -> wmc[0]++);
            cu.findAll(DoStmt.class).forEach(n -> wmc[0]++);
            cu.findAll(SwitchEntry.class).forEach(n -> {
                if (n.getLabels().isNonEmpty()) wmc[0]++; // case语句
            });
            cu.findAll(CatchClause.class).forEach(n -> wmc[0]++);
            cu.findAll(ConditionalExpr.class).forEach(n -> wmc[0]++); // 三元运算符 ?:
            
            metrics.put("wmc", wmc[0]);

            // 3. 计算耦合度 (CBO - Coupling Between Objects)
            // 统计代码中引用的所有不同的类名（剔除基本数据类型）
            Set<String> referencedTypes = new HashSet<>();
            cu.findAll(ClassOrInterfaceType.class).forEach(type -> {
                referencedTypes.add(type.getNameAsString());
            });
            metrics.put("cbo", referencedTypes.size());

        } catch (Exception e) {
            // 如果代码语法有严重错误导致无法解析，赋予默认降级值
            System.err.println("代码语法树解析失败: " + e.getMessage());
            metrics.put("wmc", -1); // -1 表示解析失败
            metrics.put("cbo", -1);
        }

        return metrics;
    }
}