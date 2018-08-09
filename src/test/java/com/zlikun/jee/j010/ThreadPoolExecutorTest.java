package com.zlikun.jee.j010;

import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

/**
 * 线程池测试
 *
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018/8/9 16:24
 */
public class ThreadPoolExecutorTest {

    @Test
    public void test() {

        // 线程池使用的阻塞式队列，用于任务排队
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(32);

        // 线程工厂（非必要），允许针对线程做一些设置
        // 默认：Executors.defaultThreadFactory()
        ThreadFactory factory = new ThreadFactory() {
            private AtomicInteger counter = new AtomicInteger();

            // 这里只是给线程指定命名
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, String.format("thread-%02d", counter.incrementAndGet()));
            }
        };

        // 拒绝执行Handler
        // 默认：final RejectedExecutionHandler defaultHandler = new ThreadPoolExecutor.AbortPolicy();
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

        // 构建一个线程池
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                4,
                16,
                30,
                TimeUnit.SECONDS,
                queue,
                factory,
                handler);

        // 直接执行
        System.out.println("1 - " + System.currentTimeMillis());
        executor.execute(() -> {
            System.out.println("2 - " + System.currentTimeMillis());
            System.out.println("Hello Thread Pool .");
        });
        // 提交任务，返回Future实例
        System.out.println("3 - " + System.currentTimeMillis());
        Future<String> future = executor.submit(() -> {
            System.out.println("4 - " + System.currentTimeMillis());
            // 模拟执行过程
            Thread.sleep(1000L);
            // 返回结果
            return "Hello Thread Pool .";
        });

        // 异步获取任务结果（会发生阻塞，等待任务执行完成）
        System.out.println("5 - " + System.currentTimeMillis());
        try {
            assertEquals("Hello Thread Pool .", future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("6 - " + System.currentTimeMillis());

        // 关闭线程池
        executor.shutdown();
        System.out.println("7 - " + System.currentTimeMillis());

    }

}
