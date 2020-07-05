package com.bytecode.method;

public class TimeMethodInfo {
    private String methodName;

    private Long markTime;

    public TimeMethodInfo(String methodName, Long markTime) {
        this.methodName = methodName;
        this.markTime = markTime;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Long getMarkTime() {
        return markTime;
    }

    public void setMarkTime(Long markTime) {
        this.markTime = markTime;
    }
}
