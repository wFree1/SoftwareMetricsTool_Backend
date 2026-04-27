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
}