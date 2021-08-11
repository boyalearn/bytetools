package com.bytetool.agentmain;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
        System.out.println("main start");
        for (VirtualMachineDescriptor descriptor : VirtualMachine.list()) {
            if (descriptor.displayName().equals("com.javaagent.agentmain.Application")) {
                VirtualMachine virtualMachine = VirtualMachine.attach(descriptor.id());
                virtualMachine.loadAgent("D:\\IdeaWorkSpace\\MyGitHub\\bytetools\\bytetool-agentmain\\target\\bytetools.jar",
                        "com.javaagent.agentmain.Application"); // 传入agent的jar包路径，test.Task是一个String agentArgs，就像main方法的String[] args，使用户传入的，test.Task表示要热修改test.Task类
                virtualMachine.detach();
            }
        }
    }
}
