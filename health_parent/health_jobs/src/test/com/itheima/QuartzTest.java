package com.itheima;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class QuartzTest {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("application-jobs.xml");
    }
}
