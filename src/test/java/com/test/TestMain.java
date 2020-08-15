package com.test;

import com.test.service.Cat;

public class TestMain {
    public static void main(String[] args) {
        start(System.currentTimeMillis());
    }
    private static void start(long time){
        try {
            new Cat().eat();
        }catch (Exception e){
            dellException(e);
        }
    }

    private static void end(){
        new Cat().work();
        System.out.println("end");
    }

    private static void dellException(Exception e){
        System.out.println(e.getMessage());
    }
}
