package com.bytetools;

import com.bytetools.agent.ExceptionMonitorAgent;
import com.bytetools.agent.TimeMonitorAgent;
import com.bytetools.config.ConfigUtils;
import com.bytetools.filter.AgentFilter;
import com.bytetools.transformer.CodeByteTransformer;
import com.bytetools.utils.SystemPrintln;

import java.lang.instrument.Instrumentation;

public class JavaAgentMain {

    public static void premain(String agentOps, Instrumentation instrumentation) {

        ConfigUtils.loadConfig(agentOps);
        if (ConfigUtils.timeMonitor.includeFilters.isEmpty() && !ConfigUtils.timeMonitor.excludeFilters.isEmpty()) {
            SystemPrintln.println(
                "no proxy. if TimeMonitor config excludeFilters, you mast config includeFilters to avoid too much class be proxy......");
            return;
        }
        if (ConfigUtils.exceptionMonitor.includeFilters.isEmpty()
            && !ConfigUtils.exceptionMonitor.excludeFilters.isEmpty()) {
            SystemPrintln.println(
                "no proxy. if ExceptionMonitor config excludeFilters, you mast config includeFilters to avoid too much class be proxy......");
            return;
        }
        if (!ConfigUtils.timeMonitor.includeFilters.isEmpty()
            && !ConfigUtils.exceptionMonitor.includeFilters.isEmpty()) {
            SystemPrintln.println("no proxy.you only proxy ExceptionMonitor or TimeMonitor");
            return;
        }
        if (ConfigUtils.timeMonitor.includeFilters.isEmpty()) {
            AgentFilter.addTransformerAgent(new ExceptionMonitorAgent());

        } else {
            AgentFilter.addTransformerAgent(new TimeMonitorAgent());
        }
        SystemPrintln.println("JavaAgentMain start ......");

        instrumentation.addTransformer(new CodeByteTransformer());
    }

}
