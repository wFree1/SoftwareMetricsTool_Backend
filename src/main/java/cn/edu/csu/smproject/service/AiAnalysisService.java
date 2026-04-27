package cn.edu.csu.smproject.service;

import cn.edu.csu.smproject.domain.ai.AiRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AiAnalysisService {

    @Value("${ai.api.url}")
    private String apiUrl;

    @Value("${ai.api.key}")
    private String apiKey;

    // Spring 内置的 HTTP 客户端
    private final RestTemplate restTemplate = new RestTemplate();

    public String analyzeCodeFile(MultipartFile file, String extraMetrics) throws Exception {
        // 1. 读取文件内容转换为字符串
        String codeContent = new String(file.getBytes(), StandardCharsets.UTF_8);

        // 2. 构造 System Prompt（设定 AI 的人设和任务边界）
        String systemPrompt = "你是一位资深的软件架构师和代码质量专家。" +
                "你的任务是Review源代码，指出代码在圈复杂度、耦合度或坏味道（Code Smell）方面的问题，" +
                "并给出具体的重构建议。请使用 Markdown 格式排版，重点突出，不要说废话。";

        // 3. 构造 User Prompt（拼装文件内容和额外的度量数据）
        StringBuilder userPrompt = new StringBuilder();
        userPrompt.append("请分析以下源代码文件：`").append(file.getOriginalFilename()).append("`\n\n");
        
        if (extraMetrics != null && !extraMetrics.isEmpty()) {
            userPrompt.append("【前端计算出的度量参考指标】：\n").append(extraMetrics).append("\n\n");
        }
        
        userPrompt.append("【源代码内容】：\n```\n").append(codeContent).append("\n```\n\n");
        userPrompt.append("请输出分析报告，包含：1. 核心问题指出；2. 3条具体的代码重构建议。");

        // 4. 发送 HTTP 请求调用大模型
        return callLlmApi(systemPrompt, userPrompt.toString());
    }

    private String callLlmApi(String systemPrompt, String userPrompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey); // 携带 API Key

        AiRequest requestBody = new AiRequest(systemPrompt, userPrompt);
        HttpEntity<AiRequest> entity = new HttpEntity<>(requestBody, headers);

        try {
            // 发起 POST 请求
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);
            Map<String, Object> body = response.getBody();
            
            // 解析 OpenAI 格式的响应体: response.choices[0].message.content
            if (body != null && body.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) body.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
            return "解析模型返回结果失败。";
        } catch (Exception e) {
            e.printStackTrace();
            return "调用 AI 接口异常: " + e.getMessage();
        }
    }
}