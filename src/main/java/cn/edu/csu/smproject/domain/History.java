package cn.edu.csu.smproject.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class History {
    private String projectName;
    private String metricType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;
    private Object data;

    public History() {
    }

    public History(String projectName, String metricType, Date time, Object data) {
        this.projectName = projectName;
        this.metricType = metricType;
        this.time = time;
        this.data = data;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getMetricType() {
        return metricType;
    }

    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}