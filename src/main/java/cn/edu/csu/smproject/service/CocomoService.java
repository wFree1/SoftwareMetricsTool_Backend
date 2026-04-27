package cn.edu.csu.smproject.service;

import cn.edu.csu.smproject.domain.CocomoMode;
import cn.edu.csu.smproject.domain.CocomoResult;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CocomoService {

    public CocomoResult estimate(int loc, String modeString) {
        CocomoMode mode = CocomoMode.fromString(modeString);
        double kloc = loc / 1000.0;

        // 计算工作量 (人月)
        double effort = mode.getA() * Math.pow(kloc, mode.getB());
        
        // 计算开发时间 (月)
        double time = mode.getC() * Math.pow(effort, mode.getD());
        
        // 建议人员数 (向上取整)
        int staff = (int) Math.ceil(effort / time);
        
        // 成本估算 (假设每人月 10000 元)
        double cost = effort * 10000;

        CocomoResult result = new CocomoResult();
        result.setLoc(loc);
        result.setMode(mode.getModeName());
        result.setEffort(formatDouble(effort));
        result.setTime(formatDouble(time));
        result.setStaff(staff);
        result.setCost(formatDouble(cost));

        Map<String, Object> formulaDetail = new HashMap<>();
        formulaDetail.put("a", mode.getA());
        formulaDetail.put("b", mode.getB());
        formulaDetail.put("effortFormula", String.format("%.2f * (%.2f)^%.2f", mode.getA(), kloc, mode.getB()));
        result.setFormulaDetail(formulaDetail);

        return result;
    }

    private double formatDouble(double value) {
        return Math.round(value * 100.0) / 100.0; // 保留两位小数
    }
}