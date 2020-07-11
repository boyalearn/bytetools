package com.bytecode.method;

import com.bytecode.utils.MonitorPrinter;

import java.util.Stack;

public class TimeMonitorUtils {

    private static long stackDeep;
    private static final Stack<TimeMethodInfo> methodStack = new Stack<TimeMethodInfo>();

    public static void markMethodStart(String methodName) {
        methodStack.push(new TimeMethodInfo(methodName, System.currentTimeMillis()));
    }

    public static void doMethodEnd(String methodName) {
        int deep = methodStack.size();
        StringBuffer sb = new StringBuffer("");
        for (int i = 2; i < deep; i++) {
            sb.append("│ ");
        }
        if (1 < deep) {
            sb.append("├─");
        }
        TimeMethodInfo methodInfo = methodStack.pop();
        long spend = System.currentTimeMillis() - methodInfo.getMarkTime();
        MonitorPrinter.println(sb.toString() + methodName + String.format(" [%6d ms]",spend));
    }


}
