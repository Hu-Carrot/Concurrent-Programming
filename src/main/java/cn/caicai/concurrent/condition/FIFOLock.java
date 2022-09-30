package cn.caicai.concurrent.condition;


import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

public class FIFOLock {

    private final AtomicBoolean locked = new AtomicBoolean(false);

    private final Queue<Thread> waiters = new ConcurrentLinkedQueue<>();

    class CV {

        private final Queue<Thread> queue = new ConcurrentLinkedQueue<>();

        public void await() {
            Thread currentThread = Thread.currentThread();

            unlock();
            queue.add(currentThread);
            LockSupport.park(this);
            lock();
        }

        public void signal() {
            LockSupport.unpark(queue.poll());
        }

        //TODO not test
        public void signalAll() {
            while (queue.isEmpty()) {
                LockSupport.unpark(queue.poll());
            }
        }
    }

    final CV createCV() {
        return new CV();
    }

    public void lock() {
        boolean wasInterrupted = false;
        Thread current = Thread.currentThread();
        waiters.add(current);
        // Block while not first in queue or cannot acquire lock
        while (waiters.peek() != current ||
                !locked.compareAndSet(false, true)) {
            LockSupport.park(this);
            if (Thread.interrupted()) // ignore interrupts while waiting
                wasInterrupted = true;
        }
        waiters.remove();
        if (wasInterrupted)          // reassert interrupt status on exit
            current.interrupt();
    }

    public void unlock() {
        locked.set(false);
        LockSupport.unpark(waiters.peek());
    }

}
