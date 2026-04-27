package cn.edu.csu.smproject.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api/clone")
public class CloneDetectionController {

    /**
     * 多文件代码重复率检测接口
     * 采用 N-Gram 滑动窗口指纹算法 (N=3)
     */
    @PostMapping("/detect")
    public ResponseEntity<Map<String, Object>> detectClones(@RequestParam("files") MultipartFile[] files) {
        Map<String, Object> response = new HashMap<>();

        if (files == null || files.length < 2) {
            response.put("code", 400);
            response.put("message", "请至少上传 2 个及以上的代码文件进行查重");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            int windowSize = 3; // 滑动窗口大小：连续 3 行代码作为一个比对指纹
            int totalNgrams = 0; // 总指纹数

            // 【改动点 1】：把 Set 改成 List，这样不仅能跨文件查，还能查出同一个文件里多次复制粘贴的代码
            Map<String, List<String>> fingerprintMap = new HashMap<>();

            // 1. 遍历并解析所有上传的文件
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();
                String content = new String(file.getBytes(), StandardCharsets.UTF_8);

                // 【核心升级】：使用正则做工业级降噪
                // 1. 抹除所有的多行注释 (/* ... */)
                content = content.replaceAll("(?s)/\\*.*?\\*/", "");
                // 2. 抹除所有的单行注释 (// ...)
                content = content.replaceAll("//.*", "");

                // 3. 按行分割并去除所有空白字符
                String[] lines = content.split("\n");
                List<String> cleanLines = new ArrayList<>();
                for (String line : lines) {
                    String cleanLine = line.replaceAll("\\s+", ""); // 彻底去空格
                    // 过滤掉空行，以及只有单个大括号 } 的无意义行（减少误判噪音）
                    if (cleanLine.length() > 2) {
                        cleanLines.add(cleanLine);
                    }
                }

                // 2. 生成 N-Gram 指纹并存入哈希表
                for (int i = 0; i <= cleanLines.size() - windowSize; i++) {
                    StringBuilder block = new StringBuilder();
                    for (int j = 0; j < windowSize; j++) {
                        block.append(cleanLines.get(i + j));
                    }
                    String fingerprint = block.toString();

                    fingerprintMap.putIfAbsent(fingerprint, new ArrayList<>());
                    fingerprintMap.get(fingerprint).add(fileName); // 记录出现过的文件名
                    totalNgrams++;
                }
            }

            // 3. 【改动点 2】：重写统计算法，采用工业级标准
            int duplicateInstances = 0; // 参与抄袭的代码块总频次
            int duplicateBlockCount = 0; // 发现的重复代码段数量
            Set<String> filesWithClones = new HashSet<>();

            for (List<String> fileNames : fingerprintMap.values()) {
                if (fileNames.size() > 1) {
                    // 如果这个代码块出现了 2 次以上，说明这几处全是抄的
                    duplicateInstances += fileNames.size();
                    duplicateBlockCount++;
                    filesWithClones.addAll(fileNames);
                }
            }

            // 新公式：参与抄袭的代码量 / 项目总代码量
            double cloneRate = 0.0;
            if (totalNgrams > 0) {
                cloneRate = ((double) duplicateInstances / totalNgrams) * 100.0;
            }

            // 4. 封装返回数据
            Map<String, Object> data = new HashMap<>();
            // 如果重复率超过 100%（极端同文件疯狂复制情况），最高限制显示为 100%
            data.put("cloneRate", Math.min(100.0, Double.parseDouble(String.format("%.2f", cloneRate))));
            data.put("totalLinesProcessed", totalNgrams + windowSize);
            data.put("duplicateBlockCount", duplicateBlockCount);
            data.put("filesWithClones", filesWithClones);

            response.put("code", 200);
            response.put("message", "success");
            response.put("data", data);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            e.printStackTrace();
            response.put("code", 500);
            response.put("message", "文件读取异常");
            return ResponseEntity.status(500).body(response);
        }
    }
}