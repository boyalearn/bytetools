package com.javaagent.agentmain;

public class TestDemo {

    public void init() throws InterruptedException {
        for (; ; ) {
            Thread.sleep(2 * 1000);
            sayHello();
        }
    }

    public void sayHello() {
        System.out.println("hello world");
    }
}
