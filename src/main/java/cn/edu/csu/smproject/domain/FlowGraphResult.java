package cn.edu.csu.smproject.domain;

public class FlowGraphResult {
    private String fileName;
    private int nodeCount;
    private int edgeCount;
    private int cyclomaticComplexity;
    private int predicateNodeCount;
    private int branchCount;
    private String complexityLevel;
    private String suggestion;

    // 省略 Getter 和 Setter 方法，请在 IDE 中自动生成
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public int getNodeCount() { return nodeCount; }
    public void setNodeCount(int nodeCount) { this.nodeCount = nodeCount; }
    public int getEdgeCount() { return edgeCount; }
    public void setEdgeCount(int edgeCount) { this.edgeCount = edgeCount; }
    public int getCyclomaticComplexity() { return cyclomaticComplexity; }
    public void setCyclomaticComplexity(int cyclomaticComplexity) { this.cyclomaticComplexity = cyclomaticComplexity; }
    public int getPredicateNodeCount() { return predicateNodeCount; }
    public void setPredicateNodeCount(int predicateNodeCount) { this.predicateNodeCount = predicateNodeCount; }
    public int getBranchCount() { return branchCount; }
    public void setBranchCount(int branchCount) { this.branchCount = branchCount; }
    public String getComplexityLevel() { return complexityLevel; }
    public void setComplexityLevel(String complexityLevel) { this.complexityLevel = complexityLevel; }
    public String getSuggestion() { return suggestion; }
    public void setSuggestion(String suggestion) { this.suggestion = suggestion; }
}