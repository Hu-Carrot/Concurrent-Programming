package cn.hujinbo.concurrent.FIFOMutex;

/**
 * @Auther: paratera
 * @Date: 2022/6/10 17:04
 * @Description:
 */
public class FIFOMutexTest {
    public int nums = 0;

    int size = 10;

    private FIFOMutex lock = new FIFOMutex();

    public void add1() {
        for (int i = 0; i < size; i++) {
            lock.lock();
            if (Thread.currentThread().isInterrupted()) {
                System.out.println(Thread.currentThread().getName() + " 退出执行");
                lock.unlock();
                break;
            }
            System.out.println(Thread.currentThread().getName() + " stop");
            Thread.currentThread().stop();
            nums++;
            lock.unlock();
        }
    }

    public void add2() {
        for (int i = 0; i < size; i++) {
            lock.lock();
            nums++;
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        FIFOMutexTest spinlockTest = new FIFOMutexTest();

        Thread t1 = new Thread(spinlockTest::add1, "Tom add1");
        Thread t2 = new Thread(spinlockTest::add1, "Jack add1");
        Thread t3 = new Thread(spinlockTest::add1, "Lee add1");

        t1.start();
        t2.start();
        t3.start();

        t1.join();t2.join();t3.join();
        System.out.println(spinlockTest.nums);
    }
}
