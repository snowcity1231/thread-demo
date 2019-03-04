package com.xc.threadlocal.demo;

/**
 * 通过ThreadLocal保存各自线程中的变量
 * Created by xuec on 2019/3/4.
 */
public class Test {

    //初始化变量
    private ThreadLocal<Long> longLocal = new ThreadLocal<>();
    private ThreadLocal<String> stringLocal = new ThreadLocal<>();

    /**
     * 为变量赋值
     */
    private void set() {
        longLocal.set(Thread.currentThread().getId());
        stringLocal.set(Thread.currentThread().getName());
    }

    /**
     * 获取变量
     * @return Long型变量
     */
    private Long getLong() {
        return longLocal.get();
    }

    /**
     * 获取变量
     * @return String型变量
     */
    private String getString() {
        return stringLocal.get();
    }

    public static void main(String[] args) throws InterruptedException {

        final Test test = new Test();

        //在主线程中赋值
        test.set();

        System.out.println("main-id: " + test.getLong());
        System.out.println("main-name: " + test.getString());

        //新建线程
        final Thread thread1 = new Thread() {
            @Override
            public void run() {
                //在线程1中赋值
                test.set();
                //打印线程1中的值
                System.out.println("Thread1-id: " + test.getLong());
                System.out.println("Thread1-name: " + test.getString());
            }
        };
        thread1.start();
        thread1.join();

        //再次打印变量
        System.out.println("main-id: " + test.getLong());
        System.out.println("main-name: " + test.getString());

    }
}
