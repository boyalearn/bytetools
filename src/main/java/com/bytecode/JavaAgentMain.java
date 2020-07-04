package com.bytecode;

import com.bytecode.config.ConfigUtils;
import com.bytecode.transformer.CodeByteTransformer;

import java.lang.instrument.Instrumentation;

public class JavaAgentMain {

    private final static CodeByteTransformer codeByteTransformer = new CodeByteTransformer();

    public static void premain(String agentOps, Instrumentation instrumentation) {

        ConfigUtils.loadConfig(agentOps);


        instrumentation.addTransformer(codeByteTransformer);
    }

}
