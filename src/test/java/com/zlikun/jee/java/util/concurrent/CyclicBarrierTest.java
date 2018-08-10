package com.zlikun.jee.java.util.concurrent;

import org.junit.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 同步屏障类，目的是让所有线程在一个屏障前等待（await），直到最后一个线程到达屏障，所有屏障都会放开，程序继续执行
 * 可以用于模拟一个瞬时并发，因为线程启动的后线程未必会在同一时间运行，如果线程执过快则无法构成并发，所以可以使用该类来实现
 *
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018/8/10 10:17
 */
public class CyclicBarrierTest {

    @Test
    public void test() {

        // 对JVM而言不存在主线程、子线程，平等对待，所以主线程与子线程的执行顺序是不定的
        // 两者任意一个都可能先执行，但肯定都会执行
        // 这里有两个线程，屏障计数为2，当后一个线程到达时，两者都会开始执行
        // 如果屏障计数改为大于2的值，那么所有线程都会被阻塞（等待其它屏障生效）
        CyclicBarrier barrier = new CyclicBarrier(2);

        new Thread(() -> {
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println("执行子线程任务");
        }).start();

        // 当前为主线程，所以也可以使用CyclicBarrier作为屏障
        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println("执行主线程任务");

        // CyclicBarrier类与CountDownLatch计数器的区别在于后者只能用一次，
        // 而后者可以通过reset()方法重置以重复使用
        barrier.reset();

    }

    @Test
    public void test2() {

        // 第二个参数是一个Runnable对象，用于当屏障开放时优先执行，可以做一些准备或初始化工作
        // 其它屏障前的线程无论顺序怎样，都会在该Runnable对象后执行
        CyclicBarrier barrier = new CyclicBarrier(2, () -> System.out.println("前置工作"));

        new Thread(() -> {
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println("执行子线程任务");
        }).start();

        // 当前为主线程，所以也可以使用CyclicBarrier作为屏障
        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println("执行主线程任务");
    }

}
