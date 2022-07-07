package cn.hujinbo.concurrent.spinlock;

/**
 * @Auther: jinbo.hu
 * @Date: 2022/6/10 10:26
 * @Description:
 */
public class SpinlockTest {

    public int nums = 0;

    int size = 10000;

    private SpinLock spinLock = new SpinLock();

    public void add1() {
        for (int i = 0; i < size; i++) {
            spinLock.lock();
            nums++;
            spinLock.unlock();
        }
    }

    public void add2() {
        for (int i = 0; i < size; i++) {
            spinLock.lock();
            nums++;
            spinLock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        SpinlockTest spinlockTest = new SpinlockTest();

        new Thread(()->{spinlockTest.add1();}).start();
        new Thread(()->{spinlockTest.add2();}).start();

        Thread.sleep(5000);

        System.out.println(spinlockTest.nums);
    }
}
