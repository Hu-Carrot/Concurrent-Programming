package cn.caicai.concurrent.spinlock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Auther: jinbo.hu
 * @Date: 2022/6/10 10:26
 * @Description:
 */
public class ReentrantSpinlockTest {

    public int nums = 0;

    int size = 10000;

    //private ReentrantFIFOMutex spinLock = new ReentrantFIFOMutex();
    private ReentrantLock spinLock = new ReentrantLock();

    //private SpinLock spinLock = new SpinLock();

    public void add1(String name) {
        spinLock.lock();
        System.out.println(name);
        add2();
        spinLock.unlock();
    }

    public void add2() {
        for (int i = 0; i < size; i++) {
            spinLock.lock();
            nums++;
            spinLock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        ReentrantSpinlockTest spinlockTest = new ReentrantSpinlockTest();

        Thread t1 = new Thread(()->{ spinlockTest.add1("t1"); });
        Thread t2 = new Thread(()->{ spinlockTest.add1("t2"); });
        Thread t3 = new Thread(()->{ spinlockTest.add1("t3"); });
        Thread t4 = new Thread(()->{ spinlockTest.add1("t4"); });

        t1.start();t2.start();t3.start();t4.start();
        t1.join();t2.join();t3.join();t4.join();

        System.out.println(spinlockTest.nums);
    }
}
