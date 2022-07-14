package cn.caicai.concurrent.rwlock;

/**
 * @Auther: jinbo.hu
 * @Date: 2022/7/6 12:19
 * @Description:
 */
public class ReadWriteLockTest {

    private ReadWriteLock lock = new ReadWriteLock();

    public String text = "bar";

    public void read() {
        lock.rLock();

        System.out.println(Thread.currentThread().getName()+" read:"+text);

        lock.rUnlock();
    }

    public void write() {
        lock.wLock();

        String write = "foo";
        text = text+write;
        System.out.println(Thread.currentThread().getName()+" write:"+write);

        lock.wUnlock();
    }

    public static void main(String[] args) throws InterruptedException {

        ReadWriteLockTest object = new ReadWriteLockTest();

        Thread r1 = new Thread(() -> {
            object.read();
        }, "R1");
        Thread r2 = new Thread(() -> {
            object.read();
        }, "R2");
        Thread r3 = new Thread(() -> {
            object.read();
        }, "R3");

        Thread w1 = new Thread(() -> {
            object.write();
        }, "W1");
        Thread w2 = new Thread(() -> {
            object.write();
        }, "W2");

        r1.start();r2.start();r3.start();
        w1.start();w2.start();

        r1.join();
        r2.join();
        r3.join();

        w1.join();
        w2.join();
    }
}
