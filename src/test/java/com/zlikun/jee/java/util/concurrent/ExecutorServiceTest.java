package com.zlikun.jee.java.util.concurrent;

import org.junit.Test;

import java.util.concurrent.*;

/**
 * 测试线程池里任务执行顺序
 *
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018/8/10 11:01
 */
public class ExecutorServiceTest {

    @Test
    public void test() throws InterruptedException {

        // I、任务队列上限为2，超时为500毫秒，单个任务执行1000毫秒以上，导致队列堆积，新添加的任务按策略被丢弃（new ThreadPoolExecutor.DiscardPolicy()）

        // 只给一个线程，观察任务的执行顺序
        ExecutorService executor = new ThreadPoolExecutor(1, 1,
                500L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(2),
                new ThreadPoolExecutor.DiscardPolicy());

        // 因为一个任务执行需要1秒以上，所以队列中的任务等待会超时
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            executor.execute(() -> {
                // 模拟任务执行过程
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.printf("完成任务 - %d%n", finalI);
            });
        }

        executor.shutdown();
        while (!executor.isTerminated()) Thread.sleep(100L);

    }

}
