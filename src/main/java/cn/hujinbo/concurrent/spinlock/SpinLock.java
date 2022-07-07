package cn.hujinbo.concurrent.spinlock;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Auther: jinbo.hu
 * @Date: 2022/6/10 09:13
 * @Description:
 */
public class SpinLock {

    private static final Unsafe unsafe;
    private static final long stateOffset;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);

            stateOffset = unsafe.objectFieldOffset
                    (SpinLock.class.getDeclaredField("state"));
        } catch (Exception ex) { throw new Error(ex); }
    }

    private volatile int state = 0;

    public void lock() {
        while (!cas(0,1)) {
            //TODO yield 优化自旋(指数回退法)
        }
    }

    public void unlock() {
        state = 0;
    }

    private void setSate(int val) {
        this.state = val;
    }

    private boolean cas(int expect, int update) {
       return unsafe.compareAndSwapInt(this,stateOffset,expect,update);
    }

    public static void main(String[] args) {
        LockSupport.unpark(Thread.currentThread());

        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        lock.unlock();
    }
}
