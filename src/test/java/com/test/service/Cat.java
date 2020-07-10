package com.test.service;

import java.sql.Time;

public class Cat {
    public void eat() {
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
