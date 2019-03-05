package com.xc.thread.join;

/**
 * 当主线程需要等待子线程执行完成之后才结束时，使用子线程join()方法
 * Created by xuec on 2019/3/5.
 */
public class ThreadJoinDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Main Thread start!");
        Thread1 thread1 = new Thread1("A");
        Thread thread2 = new Thread1("B");
        thread1.start();
        thread2.start();
        //此处不添加join方法，则主线程会先结束，然后A、B线程结束
        //添加join方法后，主线程会在A、B线程结束后再结束
        thread1.join();
        thread2.join();
        System.out.println("Main Thread end!");
    }

    static class Thread1 extends Thread {

        private String name;

        public Thread1(String name) {
            super(name);
            this.name = name;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " start");
            for (int i = 0; i < 5; i ++) {
                System.out.println("子线程" + name + "run:" + i);
                try {
                    sleep((long) (Math.random() * 10));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + " end");
        }
    }
}
