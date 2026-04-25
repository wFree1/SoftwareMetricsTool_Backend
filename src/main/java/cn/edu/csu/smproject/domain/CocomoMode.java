package cn.edu.csu.smproject.domain;

public enum CocomoMode {
    ORGANIC("organic", 2.4, 1.05, 2.5, 0.38),
    SEMI_DETACHED("semi-detached", 3.0, 1.12, 2.5, 0.35),
    EMBEDDED("embedded", 3.6, 1.20, 2.5, 0.32);

    private final String modeName;
    private final double a;
    private final double b;
    private final double c;
    private final double d;

    CocomoMode(String modeName, double a, double b, double c, double d) {
        this.modeName = modeName;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public String getModeName() { return modeName; }
    public double getA() { return a; }
    public double getB() { return b; }
    public double getC() { return c; }
    public double getD() { return d; }

    public static CocomoMode fromString(String text) {
        for (CocomoMode mode : CocomoMode.values()) {
            if (mode.modeName.equalsIgnoreCase(text)) {
                return mode;
            }
        }
        return ORGANIC; // 默认返回有机型
    }
}