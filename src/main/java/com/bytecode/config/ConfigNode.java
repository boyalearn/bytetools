package com.bytecode.config;

/**
 * 配置节点
 */
public class ConfigNode {

    private AgentType type;

    private String packageConfig;

    private String classConfig;

    private String methodConfig;

    public AgentType getType() {
        return type;
    }

    public void setType(AgentType type) {
        this.type = type;
    }

    public String getPackageConfig() {
        return packageConfig;
    }

    public void setPackageConfig(String packageConfig) {
        this.packageConfig = packageConfig;
    }

    public String getClassConfig() {
        return classConfig;
    }

    public void setClassConfig(String classConfig) {
        this.classConfig = classConfig;
    }

    public String getMethodConfig() {
        return methodConfig;
    }

    public void setMethodConfig(String methodConfig) {
        this.methodConfig = methodConfig;
    }
}
