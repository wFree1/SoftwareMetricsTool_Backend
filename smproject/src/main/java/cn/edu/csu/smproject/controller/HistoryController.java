package cn.edu.csu.smproject.controller;

import cn.edu.csu.smproject.Service.HistoryService;
import cn.edu.csu.smproject.domain.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 历史记录控制器，处理与历史记录相关的HTTP请求
 * 提供保存历史记录、获取历史记录列表和获取项目名称等功能
 */
@RestController
@RequestMapping("/history")
@CrossOrigin
public class HistoryController {

    /**
     * 自动注入历史记录服务，用于处理业务逻辑
     */
    @Autowired
    private HistoryService historyService;

    /**
     * 保存历史记录的接口
     * @param request 包含projectName、metricType和data的请求体
     * @return ResponseEntity 包含操作结果和消息的响应实体
     */
    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveHistory(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 从请求中获取参数
            String projectName = (String) request.get("projectName");
            String metricType = (String) request.get("metricType");
            Object data = request.get("data");

            // 验证必需参数是否存在
            if (projectName == null || metricType == null || data == null) {
                response.put("success", false);
                response.put("message", "Missing required parameters: projectName, metricType, or data");
                return ResponseEntity.badRequest().body(response);
            }

            // 调用服务层保存历史记录
            historyService.saveHistory(projectName, metricType, data);
            response.put("success", true);
            response.put("message", "History saved successfully");
        } catch (IOException e) {
            // 处理异常情况
            response.put("success", false);
            response.put("message", "Failed to save history: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
        return ResponseEntity.ok(response);
    }

    /**
     * 获取历史记录列表的接口
     * @param metricType 指标类型参数
     * @param projectName 项目名称参数，可选
     * @return ResponseEntity 包含操作结果和历史记录列表的响应实体
     */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getHistories(
            @RequestParam String metricType,
            @RequestParam(required = false) String projectName) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<History> histories = historyService.getHistories(metricType, projectName);
            response.put("success", true);
            response.put("data", histories);
            response.put("count", histories.size());
        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "Failed to get histories: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
        return ResponseEntity.ok(response);
    }

    /**
     * 获取项目名称的接口
     * @param metricType 指标类型参数
     * @return ResponseEntity 包含操作结果和项目名称列表的响应实体
     */
    @GetMapping("/projects")
    public ResponseEntity<Map<String, Object>> getProjectNames(@RequestParam String metricType) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<String> projectNames = historyService.getProjectNames(metricType);
            response.put("success", true);
            response.put("data", projectNames);
        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "Failed to get project names: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
        return ResponseEntity.ok(response);
    }
}