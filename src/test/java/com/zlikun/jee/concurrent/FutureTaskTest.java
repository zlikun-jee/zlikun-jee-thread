package com.zlikun.jee.concurrent;

import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;

/**
 * Future异步调用测试
 *
 * @author zlikun
 * @date 2018-09-07 9:29
 */
public class FutureTaskTest {

    /**
     * 异步执行任务，但回调无法放到与任务不同的线程中执行
     *
     * @throws InterruptedException
     */
    @Test
    public void testFutureTask() throws InterruptedException {

        ExecutorService exec = Executors.newSingleThreadExecutor();

        // 使用线程池提交异步任务，返回一个Future实例（底层实现为FutureTask类）
        Future<String> future = exec.submit(() -> {
            System.out.println("开始执行");
            // 模拟一个耗时操作
            Thread.sleep(2000L);
            System.out.println("执行结束，返回数据");
            return "Done";
        });
        exec.shutdown();

        // 在同一线程中执行其它逻辑
        System.out.println("==Begin==");
//        // 打印一个九九乘法表
//        for (int i = 1; i <= 9; i++) {
//            for (int j = 1; j <= i; j++) {
//                System.out.printf("%d × %d = %d\t", j, i, i * j);
//            }
//            System.out.println();
//        }
        // 简单打印一组数字，每200毫秒打印一次
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
            Thread.sleep(200L);
        }
        System.out.println("==End==");

        // 获取异步调用的结果
        try {
            String result = future.get();
            assertEquals("Done", result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

}
