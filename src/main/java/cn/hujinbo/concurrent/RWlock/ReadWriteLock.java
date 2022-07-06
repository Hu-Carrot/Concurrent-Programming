package cn.hujinbo.concurrent.RWlock;


import cn.hujinbo.concurrent.SpinLock.SpinLock;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: jinbo.hu
 * @Date: 2022/7/6 11:30
 * @Description:
 */
public class ReadWriteLock {

    private SpinLock S = new SpinLock();

    private AtomicInteger count = new AtomicInteger(0);

    private Object mutex = new Object();

    public void rLock() {
        if (count.compareAndSet(0,0)) S.lock();
        count.incrementAndGet();
    }

    public void rUnlock() {
        count.decrementAndGet();
        if (count.compareAndSet(0,0)) S.unlock();

    }

    public void wLock(){
        S.lock();
    }

    public void wUnlock() {
        S.unlock();
    }
}
