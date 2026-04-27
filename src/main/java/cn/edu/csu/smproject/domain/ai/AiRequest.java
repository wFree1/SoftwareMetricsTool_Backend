package cn.edu.csu.smproject.domain.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AiRequest {
    private String model;
    private List<Map<String, String>> messages;
    private double temperature;

    public AiRequest(String systemPrompt, String userPrompt) {
        // 使用 DeepSeek 的代码模型
        this.model = "deepseek-coder"; 
        this.temperature = 0.3; // 温度设低一点，让回答更严谨
        
        this.messages = new ArrayList<>();
        Map<String, String> systemMsg = new HashMap<>();
        systemMsg.put("role", "system");
        systemMsg.put("content", systemPrompt);
        
        Map<String, String> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", userPrompt);
        
        this.messages.add(systemMsg);
        this.messages.add(userMsg);
    }

    public String getModel() { return model; }
    public List<Map<String, String>> getMessages() { return messages; }
    public double getTemperature() { return temperature; }
}