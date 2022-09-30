package cn.caicai.concurrent.queue;


import cn.caicai.concurrent.spinlock.SpinLock;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.Queue;

/**
 * @Auther: jinbo.hu
 * @Date: 2022/6/14 16:05
 * @Description:
 */
public class LKQueue<E> extends AbstractQueue<E> implements Queue<E> {

    private Node head;
    private Node tail;

    public LKQueue() {
        Node node = new Node();
        head = node;
        tail = node;
    }

    class Node {
        Node next;
        E data;
    }

    private SpinLock spinLock = new SpinLock();

    private volatile int size;

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean offer(E e) {
        spinLock.lock();
        //do something
        spinLock.unlock();
        return false;
    }

    /**
     * 队列是在两端操作，锁的颗粒度可放小一点
     *
     * */

    @Override
    public E poll() {
        spinLock.lock();
        //do something
        spinLock.unlock();
        return null;
    }

    @Override
    public E peek() {
        spinLock.lock();
        //do something
        spinLock.unlock();
        return null;
    }
}
