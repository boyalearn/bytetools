package com.test.service;

public class Cat {
    public void eat() throws Exception{
        System.out.println("cat eat");
        int k=20/0;
        System.out.println(k);
    }
}
