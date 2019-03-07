package com.xc.thread.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 通过lock和condition实现线程间通信，使线程A打印3次后打印1次线程B
 * Created by xuec on 2019/3/7.
 */
public class LockDemo {

    private static Lock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();

    static class ThreadOne extends Thread {

        private String name;

        ThreadOne(String name) {
            super(name);
            this.name = name;
        }

        @Override
        public void run() {
            for (int i = 1; i < 50; i++) {
                try {
                    //线程A先执行，获得对象锁，这时线程B无法执行
                    lock.lock();
                    while (ThreadToGo.value == 2) {
                        //当value为2时，线程A开始等待
                        condition.await();
                    }
                    System.out.println(i + "---" + name);
                    if (i % 3 == 0) {
                        //线程A执行3次后，值改为2，下次执行时，会这是当前线程进入等待状态
                        ThreadToGo.value = 2;
                    }
                    //每次输出结束都会唤醒等待的线程
                    condition.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    static class ThreadTwo extends Thread {

        private String name;

        ThreadTwo(String name) {
            super(name);
            this.name = name;
        }

        @Override
        public void run() {
            for (int i = 1; i < 50; i++) {
                try {
                    lock.lock();
                    while (ThreadToGo.value == 1) {
                        condition.await();
                    }
                    System.out.println(i + "---" + name);
                    if (i % 3 != 0) {
                        ThreadToGo.value = 1;
                    }
                    condition.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    static class ThreadToGo {
        static int value = 1;
    }

    public static void main(String[] args) {
        ThreadOne t1 = new ThreadOne("A");
        ThreadTwo t2 = new ThreadTwo("B");
        t1.start();
        t2.start();
    }
}
