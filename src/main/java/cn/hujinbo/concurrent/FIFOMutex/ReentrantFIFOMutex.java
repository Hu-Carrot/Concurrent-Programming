package cn.hujinbo.concurrent.FIFOMutex;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * @Auther: jinbo.hu
 * @Date: 2022/6/10 17:04
 * @Description:
 */
public class ReentrantFIFOMutex {
    private final AtomicBoolean locked = new AtomicBoolean(false);
    private final Queue<Thread> waiters
            = new ConcurrentLinkedQueue<Thread>();

    private volatile int count = 0;
    private volatile Thread owner;
    public void lock() {
        Thread current = Thread.currentThread();
        if (current == owner) {
            count++;
            return;
        }
        waiters.add(current);
        // Block while not first in queue or cannot acquire lock
        while (waiters.peek() != current ||
                !locked.compareAndSet(false, true)) {
            LockSupport.park(this);
        }
        count++;
        owner = current;
        waiters.remove();
    }

    public void unlock() {
        count --;
        System.out.println(count);
        if (count == 0) {
            locked.set(false);
            owner = null;
            LockSupport.unpark(waiters.peek());
        }

    }
}
