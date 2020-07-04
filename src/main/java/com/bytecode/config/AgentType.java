package com.bytecode.config;

/**
 * 代理类型，可以是方法执行时间。异常打印等等方法
 */
public enum AgentType {
    EXCEPTION(0), TIME(1);

    private int type;

    AgentType(int type) {
        this.type = type;
    }

    public static AgentType getAgentType(String type) {
        int pos = type.lastIndexOf(".");
        String enumName = type.substring(pos + 1);
        for (AgentType agentType : AgentType.values()) {
            if (agentType.name().equals(enumName)) return agentType;
        }
        return null;
    }
}
