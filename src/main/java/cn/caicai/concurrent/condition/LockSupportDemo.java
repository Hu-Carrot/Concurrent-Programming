package cn.caicai.concurrent.condition;


import cn.caicai.concurrent.spinlock.SpinLock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class LockSupportDemo {


    private SpinLock spinLock = new SpinLock();
    volatile Thread producer;
    volatile Thread consumer;

    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    int count;
    StringBuilder stringBuilder = new StringBuilder();


    void product() {
        if (producer == null) {
            producer = Thread.currentThread();
        }

        spinLock.lock();
        while (count == 4) {
            spinLock.unlock();
            LockSupport.park();
            spinLock.lock();
        }
        count++;
        System.out.print("(");
        stringBuilder.append("(");
        LockSupport.unpark(consumer);
        spinLock.unlock();
    }

    void consume() {
        if (consumer == null) {
            consumer = Thread.currentThread();
        }

        spinLock.lock();
        while (count == 0) {
            spinLock.unlock();
            LockSupport.park();
            spinLock.lock();
        }
        count--;
        System.out.print(")");
        stringBuilder.append(")");
        LockSupport.unpark(producer);
        spinLock.unlock();

    }

    public static void main(String[] args) throws InterruptedException {
        final LockSupportDemo demo = new LockSupportDemo();
        Thread producer = new Thread(() -> {
            for (int i = 0; i < 500; i++) {
                demo.product();
            }
        }, "Producer");

        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 500; i++) {
                demo.consume();
            }
        }, "Consumer");

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

        //===============================================================
        // verify that the result is correct
        //===============================================================

        StringBuilder stringBuilder = demo.stringBuilder;
        String result = stringBuilder.toString();
        int count = 0;
        int n = 4;
        for (int i = 0; i < result.length();i++) {
            if (result.charAt(i) == '(') count++;
            if (result.charAt(i) == ')') count--;

            if (count > n || count < 0) {
                System.out.println("\ncount:"+count);
                throw new RuntimeException("bug");
            }
        }
        System.out.println("\nOK");

    }
}

