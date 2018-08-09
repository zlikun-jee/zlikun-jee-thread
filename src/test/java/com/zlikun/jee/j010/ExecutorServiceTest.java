package com.zlikun.jee.j010;

import org.junit.Test;

import java.util.concurrent.*;

/**
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018/8/9 17:17
 */
public class ExecutorServiceTest {

    @Test
    public void executors() {
        // Executors生成ExecutorService工厂方法
        ExecutorService executorService = null;
        // 生成一个单线程的线程池，核心线程数为1
        executorService = Executors.newSingleThreadExecutor();
        // 生成一个固定大小的线程池，核心线程数为指定值
        executorService = Executors.newFixedThreadPool(4);
        // 生成一个可缓存的线程池，任务队列由SynchronousQueue类实现，其特点是缓存重复线程（如果所有线程都是新线程对象，则线程不可控）
        executorService = Executors.newCachedThreadPool();
        // 生成一个并行 的线程池，JDK1.8增加的方法（注意这里说的是并行，非并发）
        executorService = Executors.newWorkStealingPool(4);

        // Executors生成ScheduledExecutorService工厂方法
        ScheduledExecutorService scheduledExecutorService = null;
        // 生成一个单线程的任务调度线程池
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 生成一个固定大小的任务调度线程池
        scheduledExecutorService = Executors.newScheduledThreadPool(4);
    }

    /**
     * 线程池的拒绝机制
     */
    @Test
    public void rejection() {

        // 使用工厂方法构造线程池类
        // ExecutorService executor = Executors.newFixedThreadPool(4);
        // 等效如下代码
        // return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

        RejectedExecutionHandler handler = null;
        // AbortPolicy 直接抛出异常，拒绝新任务
        // handler = new ThreadPoolExecutor.AbortPolicy();
        // 丢弃下一个将执行的任务，优先执行新任务
        // handler = new ThreadPoolExecutor.DiscardOldestPolicy();
        // 丢弃新添加的任务，但不抛出异常（不做任何处理）
        handler = new ThreadPoolExecutor.DiscardPolicy();
        // 不丢弃任务，也不加入线程池任务队列，而是直接执行任务
        // handler = new ThreadPoolExecutor.CallerRunsPolicy();

        // 这里仍使用手工构造方法，灵活控制线程池
        ExecutorService executor = new ThreadPoolExecutor(
                4,
                16,
                30,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(32),
                handler);

        // 连续添加128个比较耗时的任务
        // 此时将造成任务堆积，会触发RejectedExecutionHandler，默认：AbortPolicy，直接抛出一个异常
        for (int i = 0; i < 128; i++) {
            // 假设执行的任务比较耗时
            try {
                executor.execute(() -> {
                    try {
                        Thread.sleep(3000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Hello !");
                });
            } catch (RejectedExecutionException e) {
                System.err.println(e.getMessage());
            }
        }

        // 任务执行完成后，关闭线程池
        executor.shutdown();
        // 循环检查线程池是否已关闭
        while (!executor.isTerminated()) ;

    }

}
