package cn.caicai.concurrent.spinlock;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Auther: jinbo.hu
 * @Date: 2022/6/10 09:13
 * @Description: 实现可重入的 spinlock
 * 参考：
 * http://www.java2s.com/example/java/thread/use-atomicreference-to-implement-reentrant-lock.html
 *
 */
public class ReentrantSpinLock {

    private static final Unsafe unsafe;
    private static final long stateOffset;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);

            stateOffset = unsafe.objectFieldOffset
                    (ReentrantSpinLock.class.getDeclaredField("state"));
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    private volatile int state = 0;
    private volatile Thread owner;

    public void lock() {
        final Thread current = Thread.currentThread();
        if (current == owner) {
            state = state + 1;
            return;
        }
        while (!cas(0, 1)) {
        }
        owner = current;
    }

    public void unlock() {
        Thread current = Thread.currentThread();
        if (current == owner) {
            int c = state - 1;
            if (c == 0) {
                state = c;
                owner = null;
            }
        }
    }

    private boolean cas(int expect, int update) {
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }

    public static void main(String[] args) {
        LockSupport.unpark(Thread.currentThread());

        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        lock.unlock();

        AtomicReference<Thread> owner = new AtomicReference<Thread>();
        owner.get();
    }
}
