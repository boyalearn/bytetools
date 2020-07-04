package com.bytecode;

import com.bytecode.config.ConfigUtils;
import com.bytecode.transformer.CodeByteTransformer;
import com.bytecode.utils.MonitorPrinter;

import java.lang.instrument.Instrumentation;

public class JavaAgentMain {

    private final static CodeByteTransformer codeByteTransformer = new CodeByteTransformer();

    public static void premain(String agentOps, Instrumentation instrumentation) {

        ConfigUtils.loadConfig(agentOps);

        //避免文件打印出现空指针异常需要在加载配置文件后打印JavaAgent的启动文件
        MonitorPrinter.println("Start JavaAgent...");

        instrumentation.addTransformer(codeByteTransformer);
    }

}
