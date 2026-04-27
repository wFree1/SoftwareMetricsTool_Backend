package cn.edu.csu.smproject.service;

import cn.edu.csu.smproject.domain.FlowGraphResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

@Service
public class FlowGraphService {

    public FlowGraphResult analyze(MultipartFile file) throws Exception {
        InputStream is = file.getInputStream();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // 忽略 DTD 验证以加速解析并防止外部实体注入报错
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(is);

        NodeList cells = doc.getElementsByTagName("mxCell");
        int nodeCount = 0;
        int edgeCount = 0;
        int predicateNodeCount = 0;

        for (int i = 0; i < cells.getLength(); i++) {
            Element cell = (Element) cells.item(i);
            
            String isVertex = cell.getAttribute("vertex");
            String isEdge = cell.getAttribute("edge");
            String style = cell.getAttribute("style");

            // 统计节点
            if ("1".equals(isVertex)) {
                nodeCount++;
                // 判定节点通常是菱形 (rhombus)
                if (style != null && (style.contains("rhombus") || style.contains("diamond"))) {
                    predicateNodeCount++;
                }
            }
            // 统计边
            else if ("1".equals(isEdge)) {
                edgeCount++;
            }
        }

        // 计算圈复杂度: V(G) = E - N + 2
        int cyclomaticComplexity = edgeCount - nodeCount + 2;
        // 估算分支数：通常判定节点会产生至少两个分支
        int branchCount = predicateNodeCount * 2; 

        FlowGraphResult result = new FlowGraphResult();
        result.setFileName(file.getOriginalFilename());
        result.setNodeCount(nodeCount);
        result.setEdgeCount(edgeCount);
        result.setPredicateNodeCount(predicateNodeCount);
        result.setBranchCount(branchCount);
        result.setCyclomaticComplexity(cyclomaticComplexity);

        // 评级与建议
        if (cyclomaticComplexity <= 5) {
            result.setComplexityLevel("低");
            result.setSuggestion("流程结构清晰，代码可维护性高。");
        } else if (cyclomaticComplexity <= 10) {
            result.setComplexityLevel("中等");
            result.setSuggestion("复杂度在合理范围内，注意后续迭代不要过度增加分支。");
        } else if (cyclomaticComplexity <= 15) {
            result.setComplexityLevel("高");
            result.setSuggestion("结构较为复杂，建议对判定逻辑进行提取和重构。");
        } else {
            result.setComplexityLevel("极高");
            result.setSuggestion("风险较高，强烈建议拆分系统模块或重新设计流程图！");
        }

        return result;
    }
}