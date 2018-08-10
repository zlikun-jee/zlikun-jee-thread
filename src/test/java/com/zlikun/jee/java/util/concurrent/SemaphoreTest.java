package com.zlikun.jee.java.util.concurrent;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018/8/10 10:42
 */
public class SemaphoreTest {

    @Test
    public void test() throws InterruptedException {

        // 线程池有10个线程
        ExecutorService executor = Executors.newFixedThreadPool(10);

        // 信号量只允许同时运行5个
        Semaphore semaphore = new Semaphore(5);

        for (int i = 0; i < 32; i++) {
            // 模拟写入数据库操作，最大连接数：5
            executor.execute(() -> {
                try {
                    // 取得许可证
                    semaphore.acquire();
                    // 并发逻辑
                    System.out.println(System.currentTimeMillis() + "：插入数据库，最大连接数为5");
                    Thread.sleep(500L);
                    // 释放许可证
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            // 模拟其它业务逻辑：不限制并发数，由线程池控制
            executor.execute(() -> {
                System.out.println(System.currentTimeMillis() + "：其它业务逻辑不限制");
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            Thread.sleep(100L);
        }

        System.out.println("==End==");

    }

}
