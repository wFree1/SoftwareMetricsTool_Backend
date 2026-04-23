package cn.edu.csu.smproject.Service;

import cn.edu.csu.smproject.domain.History;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HistoryService {

    private static final String HISTORY_DIR = "src/history/";
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    private String getFilePath(String metricType) {
        String fileName;
        switch (metricType.toUpperCase()) {
            case "CK":
                fileName = "ck-history.json";
                break;
            case "LK":
                fileName = "lk-history.json";
                break;
            case "LOC":
                fileName = "loc-history.json";
                break;
            case "UCP":
                fileName = "ucp-history.json";
                break;
            case "VG":
                fileName = "vg-history.json";
                break;
            case "COCOMO":
                fileName = "cocomo-history.json";
                break;
            case "FP":
                fileName = "fp-history.json";
                break;
            default:
                fileName = metricType.toLowerCase() + "-history.json";
        }
        return HISTORY_DIR + fileName;
    }

    /**
     * 保存历史记录的接口
     * @param projectName 项目名称参数
     * @param metricType 指标类型参数
     * @param data 指标数据参数
     * @throws IOException 如果文件操作失败
     */
    public void saveHistory(String projectName, String metricType, Object data) throws IOException {
        Path historyDir = Paths.get(HISTORY_DIR);
        if (!Files.exists(historyDir)) {
            Files.createDirectories(historyDir);
        }

        String filePath = getFilePath(metricType);
        File file = new File(filePath);

        List<History> histories = new ArrayList<>();
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                if (content.length() > 0) {
                    histories = parseHistoryList(content.toString());
                }
            }
        }

        History history = new History(projectName, metricType.toUpperCase(), new Date(), data);
        histories.add(history);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(objectMapper.writeValueAsString(histories));
        }
    }
        
    /**
     * 获取历史记录列表的接口
     * @param metricType 指标类型参数
     * @param projectName 项目名称参数，可选
     * @return List<History> 包含历史记录的列表
     * @throws IOException 如果文件操作失败
     */
    public List<History> getHistories(String metricType, String projectName) throws IOException {
        String filePath = getFilePath(metricType);
        File file = new File(filePath);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

            if (content.length() == 0) {
                return new ArrayList<>();
            }

            List<History> histories = parseHistoryList(content.toString());

            if (StringUtils.hasText(projectName)) {
                histories.removeIf(h -> !h.getProjectName().equals(projectName));
            }

            return histories;
        }
    }

    /**
     * 获取项目名称的接口
     * @param metricType 指标类型参数
     * @return List<String> 包含项目名称的列表
     * @throws IOException 如果文件操作失败
     */
    public List<String> getProjectNames(String metricType) throws IOException {
        List<History> histories = getHistories(metricType, null);
        List<String> projectNames = new ArrayList<>();
        for (History h : histories) {
            if (!projectNames.contains(h.getProjectName())) {
                projectNames.add(h.getProjectName());
            }
        }
        return projectNames;
    }

    /**
     * 解析历史记录列表的接口
     * @param json JSON字符串参数
     * @return List<History> 包含历史记录的列表
     */
    private List<History> parseHistoryList(String json) {
        try {
            return objectMapper.readValue(json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, History.class));
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}