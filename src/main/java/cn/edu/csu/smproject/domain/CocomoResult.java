package cn.edu.csu.smproject.domain;

import java.util.Map;

public class CocomoResult {
    private int loc;
    private double effort;
    private double time;
    private int staff;
    private double cost;
    private String mode;
    private Map<String, Object> formulaDetail;

    // 省略 Getter 和 Setter 方法，请在 IDE 中自动生成
    public int getLoc() { return loc; }
    public void setLoc(int loc) { this.loc = loc; }
    public double getEffort() { return effort; }
    public void setEffort(double effort) { this.effort = effort; }
    public double getTime() { return time; }
    public void setTime(double time) { this.time = time; }
    public int getStaff() { return staff; }
    public void setStaff(int staff) { this.staff = staff; }
    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }
    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
    public Map<String, Object> getFormulaDetail() { return formulaDetail; }
    public void setFormulaDetail(Map<String, Object> formulaDetail) { this.formulaDetail = formulaDetail; }
}