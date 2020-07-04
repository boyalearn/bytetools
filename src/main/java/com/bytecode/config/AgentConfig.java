package com.bytecode.config;

import java.util.List;

public class AgentConfig {

    private String logFileName;

    private List<ConfigNode> includes;

    private List<ConfigNode> excludes;

    public String getLogFileName() {
        return logFileName;
    }

    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }

    public List<ConfigNode> getIncludes() {
        return includes;
    }

    public void setIncludes(List<ConfigNode> includes) {
        this.includes = includes;
    }

    public List<ConfigNode> getExcludes() {
        return excludes;
    }

    public void setExcludes(List<ConfigNode> excludes) {
        this.excludes = excludes;
    }
}
