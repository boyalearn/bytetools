package com.bytecode.method;

import com.bytecode.utils.MonitorPrinter;

import java.util.Stack;

public class TimeMonitorUtils {

    private static final Stack<TimeMethodInfo> methodStack = new Stack<TimeMethodInfo>();

    private static final Stack<TimeMethodInfo> printStack = new Stack<TimeMethodInfo>();

    public static void markMethodStart(String methodName) {
        methodStack.push(new TimeMethodInfo(methodName, System.currentTimeMillis()));
    }

    public static void doMethodEnd(String methodName) {
        int deep = methodStack.size();
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < deep; i++) {
            sb.append("--");
        }
        sb.append(">  ");
        TimeMethodInfo methodInfo = methodStack.pop();
        long spend = System.currentTimeMillis() - methodInfo.getMarkTime();
        printStack.push(new TimeMethodInfo(sb.toString() + methodName, spend));
        if (methodStack.isEmpty()) {
            printSpendTime();
        }

    }

    private static void printSpendTime() {
        while (!printStack.isEmpty()) {
            TimeMethodInfo methodInfo = printStack.pop();
            MonitorPrinter.println(methodInfo.getMethodName() + " [" + methodInfo.getMarkTime() + "ms]");
        }
    }

}
