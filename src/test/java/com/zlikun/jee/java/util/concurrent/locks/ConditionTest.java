package com.zlikun.jee.java.util.concurrent.locks;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018/8/9 19:12
 */
public class ConditionTest {

    @Test
    public void test() throws InterruptedException {

        final BoundedQueue queue = new BoundedQueue(4);

        final CountDownLatch latch = new CountDownLatch(2);

        // 生产者线程，速度慢
        new Thread(() -> {
            for (int i = 1; i <= 64; i++) {
                try {
                    queue.add(i);
                    System.out.printf("生产%02d%n", i);
                    Thread.sleep(200L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            latch.countDown();
        }).start();

        // 消费者线程，速度快
        new Thread(() -> {
            for (int i = 0; i < 64; i++) {
                try {
                    System.out.printf("消费%02d%n", queue.remove());
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            latch.countDown();
        }).start();


        latch.await();
    }

    /**
     * 有界队列，底层使用数组实现
     *
     * @param <T>
     */
    static class BoundedQueue<T> {
        private Object[] items;
        private int addIndex, removeIndex, count, size;
        private Lock lock = new ReentrantLock();
        private Condition empty = lock.newCondition();
        private Condition full = lock.newCondition();

        public BoundedQueue(int size) {
            this.size = size;
            items = new Object[size];
        }

        public void add(T t) throws InterruptedException {
            lock.lock();
            try {
                // 添加元素时，如果队列满，则等待
                while (count == size) {
                    full.await();
                }
                items[addIndex] = t;
                count++;
                if (++addIndex == size) {
                    addIndex = 0;
                }
                // 通知在notEmpty上等待的线程
                empty.signal();
            } finally {
                lock.unlock();
            }
        }

        public T remove() throws InterruptedException {
            lock.lock();
            try {
                // 如果总数为0，则notEmpty等待
                while (count == 0) {
                    empty.await();
                }
                Object x = items[removeIndex];
                if (++removeIndex == size)
                    removeIndex = 0;
                count--;
                // 通知在notFull上等待的线程
                full.signal();
                return (T) x;
            } finally {
                lock.unlock();
            }
        }

    }

}
