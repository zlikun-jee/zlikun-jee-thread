package com.zlikun.jee.j005;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

/**
 * 使用Atomic类型计数实现多线程计数自增（避免加锁）
 *
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018/8/9 14:15
 */
public class AtomicTypeTest {

    private AtomicInteger number = new AtomicInteger();

    @Test
    public void test() throws InterruptedException {

        final int loop = 1_000_000;

        // 为了方便测试，我们使用一个线程池来模拟多线程场景
        ExecutorService exec = Executors.newFixedThreadPool(20);

        final CountDownLatch latch = new CountDownLatch(loop);
        for (int i = 0; i < loop; i++) {
            exec.execute(() -> {
                number.incrementAndGet();
                latch.countDown();
            });
        }
        latch.await();
        assertEquals(loop, this.number.get());

        // 退出线程池
        exec.shutdown();

    }

}
