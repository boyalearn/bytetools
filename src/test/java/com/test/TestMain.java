package com.test;

import com.test.service.Cat;

public class TestMain {
    public static void main(String[] args) {
        start(System.currentTimeMillis());
        start(System.currentTimeMillis());
        end();
    }
    private static void start(long time){
        System.out.println("start:"+ time);
        try {
            new Cat().eat();
        }catch (Exception e){
            System.out.println("dellException");
            dellException(e);
        }
    }

    private static void end(){
        new Cat().work();
        new Cat().eat();
        System.out.println("end");
    }

    private static void dellException(Exception e){
        System.out.println(e.getMessage());
    }
}
