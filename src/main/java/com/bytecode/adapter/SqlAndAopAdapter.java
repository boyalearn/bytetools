package com.bytecode.adapter;

import com.bytecode.agent.CglibAopAgent;
import com.bytecode.agent.MybatisAgent;
import com.bytecode.agent.TransformerAgent;
import com.bytecode.config.AgentType;
import com.bytecode.config.ConfigUtils;

public class SqlAndAopAdapter implements AgentAdapter {

    private TransformerAgent cglibAgent = new CglibAopAgent();

    private TransformerAgent mybatisAgent = new MybatisAgent();

    @Override
    public TransformerAgent adapter(String className, ClassLoader loader) {
        if (AgentType.TIME.equals(ConfigUtils.getAgentType()) && className.contains(CglibAopAgent.CLASS_NAME)) {
            return cglibAgent;
        } else if (AgentType.TIME.equals(ConfigUtils.getAgentType()) && className.contains(MybatisAgent.CLASS_NAME)) {
            return mybatisAgent;
        } else {
            return null;
        }
    }


}
