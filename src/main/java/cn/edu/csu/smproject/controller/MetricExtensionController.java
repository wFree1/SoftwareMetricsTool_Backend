package cn.edu.csu.smproject.controller;

import cn.edu.csu.smproject.domain.CocomoRequest;
import cn.edu.csu.smproject.domain.CocomoResult;
import cn.edu.csu.smproject.domain.FlowGraphResult;
import cn.edu.csu.smproject.service.AiAnalysisService;
import cn.edu.csu.smproject.service.CocomoService;
import cn.edu.csu.smproject.service.FlowGraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class MetricExtensionController {

    @Autowired
    private CocomoService cocomoService;

    @Autowired
    private FlowGraphService flowGraphService;

    @Autowired
    private AiAnalysisService aiAnalysisService;

    @PostMapping(value = "/cocomo/estimate")
    public ResponseEntity<Map<String, Object>> estimateCocomo(@RequestBody CocomoRequest request) {
        CocomoResult data = cocomoService.estimate(request.getLoc(), request.getMode());
        
        // 构造与前端约定好的通用响应结构
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "success");
        response.put("data", data);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/flowgraph/analyze")
    public ResponseEntity<Map<String, Object>> analyzeFlowGraph(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            FlowGraphResult data = flowGraphService.analyze(file);
            response.put("code", 200);
            response.put("message", "success");
            response.put("data", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("code", 500);
            response.put("message", "解析XML失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * AI 代码诊断接口
     * @param file 用户上传的源代码文件 (.java, .xml 等)
     * @param metrics 可选参数，可以将你们自己算出来的 CK/圈复杂度 传给 AI 作为参考
     */
    @PostMapping(value = "/ai/analyze")
    public ResponseEntity<Map<String, Object>> analyzeWithAI(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "metrics", required = false) String metrics) {

        Map<String, Object> response = new HashMap<>();
        try {
            // 调用服务层，获取 AI 的 Markdown 回答
            String analysisReport = aiAnalysisService.analyzeCodeFile(file, metrics);

            response.put("code", 200);
            response.put("message", "success");
            response.put("data", analysisReport);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("code", 500);
            response.put("message", "AI 诊断失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // 【新增】：专门用来安全接收前端 JSON 数据的实体类
    public static class RefactorReq {
        private String code;
        private String issueType;

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getIssueType() { return issueType; }
        public void setIssueType(String issueType) { this.issueType = issueType; }
    }

    /**
     * AI 自动重构接口
     */
    @PostMapping("/ai/refactor")
    public ResponseEntity<Map<String, Object>> refactorCode(@RequestBody RefactorReq request) {
        Map<String, Object> response = new HashMap<>();
        // 使用 get 方法获取数据，告别 Map 解析崩溃的风险
        String badCode = request.getCode();
        String issueType = request.getIssueType();

        if (badCode == null || badCode.trim().isEmpty()) {
            response.put("code", 400);
            response.put("message", "代码不能为空");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // 调用 Service 中新增的方法
            String refactoredCode = aiAnalysisService.refactorCode(badCode, issueType);

            response.put("code", 200);
            response.put("message", "success");

            // 容错处理：去除 Markdown 标记
            if (refactoredCode.startsWith("```java")) {
                refactoredCode = refactoredCode.replaceFirst("```java\n?", "");
            } else if (refactoredCode.startsWith("```")) {
                refactoredCode = refactoredCode.replaceFirst("```\n?", "");
            }
            if (refactoredCode.endsWith("```")) {
                refactoredCode = refactoredCode.substring(0, refactoredCode.lastIndexOf("```"));
            }

            response.put("data", refactoredCode.trim());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace(); // 如果 AI 接口崩了，这里一定会打印红字
            response.put("code", 500);
            response.put("message", "AI 核心服务调用失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}