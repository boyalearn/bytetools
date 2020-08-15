package com.bytecode.adapter;

import com.bytecode.agent.TransformerAgent;

public interface AgentAdapter {
    TransformerAgent adapter(String className, ClassLoader loader) throws Exception;
}
