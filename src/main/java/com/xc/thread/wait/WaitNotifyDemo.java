package com.xc.thread.wait;

/**
 * 通过对象锁的唤醒、等待来控制线程的执行顺序
 * Created by xuec on 2019/3/6.
 */
public class WaitNotifyDemo {

    static class PrintThread implements Runnable {

        private String name;
        private final Object prev;
        private final Object self;

        PrintThread(String name, Object prev, Object self) {
            this.name = name;
            this.prev = prev;
            this.self = self;
        }

        @Override
        public void run() {

            int count = 10;
            while (count > 0) {
                //先锁住prev
                synchronized (prev) {
                    //再锁self
                    synchronized (self) {
                        System.out.println(name);
                        count--;

                        //唤醒等待self的线程
                        self.notify();
                    }
                    try {
                        //当前线程阻塞，交出prev对象的控制
                        prev.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Object a = new Object();
        Object b = new Object();
        Object c = new Object();

        PrintThread t1 = new PrintThread("A", c, a);    //线程A持有c和a的对象锁
        PrintThread t2 = new PrintThread("B", a, b);    //线程B持有a和b的对象锁
        PrintThread t3 = new PrintThread("C", b, c);    //线程C持有b和c的对象锁

        //线程A开始执行，先持有c,a的对象锁，打印完成后先释放a，唤醒了等待a锁的线程B；然后再释放了c，自身进入休眠状态。
        new Thread(t1).start();
        Thread.sleep(100);//确保线程按顺序执行
        //线程B被唤醒后，这时a,b锁都没被占用，开始执行，并持有a、b的对象锁，打印操作完成了再释放了b，唤醒了等待b锁的c;然后再释放a，进入休眠状态
        new Thread(t2).start();
        Thread.sleep(100);
        //线程C被唤醒，申请了b,c的锁，打印完成后释放了c，又唤醒了等待c锁的线程A
        new Thread(t3).start();
        Thread.sleep(100);

    }
}
