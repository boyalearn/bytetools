package com.bytetools.filter;

import com.bytetools.agent.TransformerAgent;

import java.util.LinkedList;
import java.util.List;

public class AgentFilter {

    private static List<TransformerAgent> agentList = new LinkedList<TransformerAgent>();

    public static void addTransformerAgent(TransformerAgent agent) {
        agentList.add(agent);
    }

    public static List<TransformerAgent> getTransformerAgent() {
        return agentList;
    }
}
