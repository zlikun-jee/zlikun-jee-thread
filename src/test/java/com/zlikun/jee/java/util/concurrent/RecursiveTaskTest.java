package com.zlikun.jee.java.util.concurrent;

import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * 使用Fork/Join实现累加求和（以递归方式实现）
 *
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018/8/9 19:42
 */
public class RecursiveTaskTest {

    @Test
    public void test() {

        // 构造一个ForkJoinPool
        ForkJoinPool pool = new ForkJoinPool();
        // 构造Fork/Join任务
        CountTask task = new CountTask(1, 10);
        // 使用ForkJoinPool提交该任务，返回一个Future，后续异步取得计算结果
        // 计算过程以递归方式实现
        Future<Integer> future = pool.submit(task);
        try {
            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
            // 任务执行期间可能会抛出异常，但没办法在主线程中捕获，
            // 所以ForkJoinTask提供了isCompletedAbnormally()方法来检查任务是否抛出异常或已被取消了
            if (task.isCompletedAbnormally()) {
                System.out.println("执行异常>>>" + task.getException());
            }
            if (task.isCancelled()) {
                System.out.println("任务取消>>>" + task.getException());
            }
        }

    }

    static class CountTask extends RecursiveTask<Integer> {

        private static final int THRESHOLD = 2;
        private int start;
        private int end;

        public CountTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            // 模拟异常情况
            if (start == 6) {
                // throw new RuntimeException("没有实际意义，仅用于模拟Fork/Join异常情形");
            }

            // 计算逻辑
            int sum = 0;
            if (end - start <= THRESHOLD) {
                // 拆分到足够小规模时，直接计算
                for (int i = start; i <= end; i++) {
                    sum += i;
                }
            } else {
                // 计算区间大于阈值，将任务分裂为两个子任务
                int middle = (end + start) / 2;
                CountTask leftTask = new CountTask(start, middle);
                CountTask rightTask = new CountTask(middle + 1, end);
                // 执行子任务
                leftTask.fork();
                rightTask.fork();
                // 等待子任务完成，取得其结果
                int leftResult = leftTask.join();
                int rightResult = rightTask.join();
                // 合并子任务
                sum = leftResult + rightResult;
            }
            return sum;
        }


    }

}
