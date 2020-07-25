package com.honji.order;

public class Singleton {
    private Singleton() {}
    //启动就创建不延迟
    private static Singleton singleton = new Singleton();

    //获取单例
    public static  Singleton getInstance() {
        System.out.println("获取单例");
        return singleton;
    }

    //销毁单例
    public static void delInstance() {
        System.out.println("销毁单例");
        singleton = null;
    }

    public static void main(String[] args)throws Exception {
        getInstance();
        delInstance();
    }
}
