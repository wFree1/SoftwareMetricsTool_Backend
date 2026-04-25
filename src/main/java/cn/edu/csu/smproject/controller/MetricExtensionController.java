package cn.edu.csu.smproject.controller;

import cn.edu.csu.smproject.domain.CocomoRequest;
import cn.edu.csu.smproject.domain.CocomoResult;
import cn.edu.csu.smproject.domain.FlowGraphResult;
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
}