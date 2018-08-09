package com.zlikun.jee.j003;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 公平锁与非公平锁
 *
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018/8/9 14:38
 */
public class FairLockTest {

    @Test
    public void test() throws InterruptedException {

        // 定义一个公平锁，安保证按得到锁的顺序执行
        // 当定义为公平锁时，会固定按：生火-做饭-熄火 输出
        // 当定义为非公平锁时，则会任意输出
        final ReentrantLock lock = new ReentrantLock(true);

        final CountDownLatch latch = new CountDownLatch(3);

        // 三个线程，分别做三件事，但要求按固定顺序执行
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    System.out.println("生火");
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
            latch.countDown();
        }).start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    System.out.println("做饭");
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
            latch.countDown();
        }).start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    System.out.println("熄火");
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
            latch.countDown();
        }).start();

        // 等待子线程执行完成退出
        latch.await();

    }

}
