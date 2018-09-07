package com.zlikun.jee.concurrent;

import org.junit.Test;

import java.util.concurrent.*;

import static org.junit.Assert.*;

/**
 * JDK8开始提供CompletableFuture类，提供更强大的异步执行能力
 *
 * @author zlikun
 * @date 2018-09-07 10:00
 */
public class CompletableFutureTest {

    /**
     * 参考：https://blog.csdn.net/moakun/article/details/80153901
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Test
    public void testCompletableFuture() throws InterruptedException, ExecutionException {

        // 使用一个预定义的结果创建一个完成的CompletableFuture，通常我们会在计算的开始阶段使用它
        CompletableFuture<String> future = CompletableFuture.completedFuture("Hello");
        assertTrue(future.isDone());
        // Returns the result value (or throws any encountered exception) if completed, else returns the given valueIfAbsent
        assertEquals("Hello", future.getNow(null));
        // Waits if necessary for this future to complete, and then returns its result
        assertEquals("Hello", future.get());

        // 使用 ForkJoinPool 实现异步执行，从命名上（*Async）可以看出是否是异步执行方法
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            // 异步执行的线程是一个守护线程
            assertTrue(Thread.currentThread().isDaemon());
            // 模拟执行耗时
            sleep(200L);
            // 执行结束
            System.out.println("任务执行完成");
        });
        // 等待足够的时间后，任务执行结束
        assertFalse(future2.isDone());
        sleep(250L);
        assertTrue(future2.isDone());

        // 在前一个阶段上应用函数（同步），CompletableFuture实现了CompletionStage，可以一个阶段一个阶段的执行
        CompletableFuture<String> future3 = CompletableFuture.completedFuture("Hello").thenApply(String::toUpperCase);
        assertEquals("HELLO", future3.getNow(null));

        // 在前一个阶段上异步应用函数
        CompletableFuture<String> future4 = CompletableFuture.completedFuture("Hello").thenApplyAsync(msg -> {
            sleep(100L);
            return msg.toUpperCase();
        });
        assertNull(future4.getNow(null));
        // 下面两个方法代码实现上看起来基本是一致的，但具体有什么区别呢？
        assertEquals("HELLO", future4.get());
        assertEquals("HELLO", future4.join());

        // 使用固定的线程池完成异步执行动作
        ExecutorService exec = Executors.newFixedThreadPool(3, r -> new Thread(r, "future-exec-" + (System.currentTimeMillis() & 0xff)));
        CompletableFuture<String> future5 = CompletableFuture.completedFuture("Hello").thenApplyAsync(msg -> {
            assertTrue(Thread.currentThread().getName().startsWith("future-exec-"));
            sleep(200L);
            return msg.toUpperCase();
        }, exec);
        assertNull(future5.getNow(null));
        assertEquals("HELLO", future5.join());

    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
