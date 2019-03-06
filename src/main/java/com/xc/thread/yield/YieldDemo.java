package com.xc.thread.yield;

/**
 * yield():让线程回到可执行状态，以允许具有相同优先级的线程具有执行的机会。
 * yield并不一定能达到让步的状态，因为执行yield方法后，该线程仍有可能被线程调度再次选中。
 * Created by xuec on 2019/3/6.
 */
public class YieldDemo {

    static class YieldThread extends Thread {

        YieldThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            for (int i = 0; i < 50; i++) {
                System.out.println(this.getName() + "----" + i);
                if (i == 30) {
                    //当i=30时，该线程把资源让给其他线程执行。
                    yield();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

//        for (int i = 0; i < 10; i++) {
            YieldThread t1 = new YieldThread("a");
            YieldThread t2 = new YieldThread("b");

            t1.start();
            t2.start();

            Thread.sleep(1000);
            System.out.println("============================");
            System.out.println("over");
            System.out.println("============================");

//        }


    }
}
