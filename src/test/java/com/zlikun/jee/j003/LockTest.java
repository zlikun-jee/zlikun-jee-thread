package com.zlikun.jee.j003;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * 使用Lock对象实现多线程对数字自增
 *
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018/8/9 14:15
 */
public class LockTest {

    private final Lock lock = new ReentrantLock();
    private Integer number;

    @Test
    public void test() throws InterruptedException {

        final int loop = 1_000_000;

        // 为了方便测试，我们使用一个线程池来模拟多线程场景
        ExecutorService exec = Executors.newFixedThreadPool(20);

        // 测试一：不加锁
        reset();
        final CountDownLatch latch0 = new CountDownLatch(loop);
        for (int i = 0; i < loop; i++) {
            exec.execute(() -> {
                m0();
                latch0.countDown();
            });
        }
        latch0.await();
        assertNotEquals(Integer.valueOf(loop), this.number);

        // 测试二：全局加锁
        reset();
        final CountDownLatch latch2 = new CountDownLatch(loop);
        for (int i = 0; i < loop; i++) {
            exec.execute(() -> {
                m2();
                latch2.countDown();
            });
        }
        latch2.await();
        assertEquals(Integer.valueOf(loop), this.number);

        // 退出线程池
        exec.shutdown();

    }

    /**
     * 不加锁，自增
     */
    private void m0() {
        number++;
    }

    /**
     * 加锁自增
     */
    private void m2() {
        try {
            lock.lock();
            number++;
        } finally {
            lock.unlock();
        }
    }

    private void reset() {
        number = 0;
    }

}
