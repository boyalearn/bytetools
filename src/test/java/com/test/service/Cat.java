package com.test.service;

public class Cat {
    public void eat() throws Exception {
        int i=1/0;
        System.out.println("cat eat");
    }

    public void work(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
