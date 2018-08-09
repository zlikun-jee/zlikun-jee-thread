package com.zlikun.jee.j010;

import org.junit.Test;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018/8/9 16:50
 */
public class ScheduledThreadPoolExecutorTest {

    @Test
    public void schedule() throws InterruptedException {

        // 使用 DelayedWorkQueue() 代替了直接使用BlockingQueue
        // 由于该类继承了ThreadPoolExecutor类，所以大部分设定及功能都沿用了该类
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(4);
        // 下面是一些其特有有方法，主要是schedule系列方法，实现任务的调度执行
        System.out.println("1 - " + System.currentTimeMillis());
        // 提交任务消耗了一些时间（某一次输出为49毫秒）
        // 该调度程序只会执行一次
        executor.schedule(() -> {
            System.out.println("2 - " + System.currentTimeMillis());
        }, 1500, TimeUnit.MILLISECONDS);
        // 从输出时间戳来看，任务被延迟了1500毫秒左右才执行
        System.out.println("3 - " + System.currentTimeMillis());


        // 初始1000毫秒，间隔2000毫秒执行
        System.out.println("4 - " + System.currentTimeMillis());
        // 该调度程序会间隔2000毫秒不间断执行
        // initialDelay参数用于控制第一次执行时机，本例为任务提交1000毫秒后（没有那么精准）
        // period参数则用于控制从第二次开始后续所有任务的执行间隔
        executor.scheduleAtFixedRate(() -> {
            System.out.println("5 - " + System.currentTimeMillis());
        }, 1000, 2000, TimeUnit.MILLISECONDS);
        System.out.println("6 - " + System.currentTimeMillis());

        TimeUnit.SECONDS.sleep(30L);

    }

    /**
     * 以固定间隔执行任务
     * 如果上一任务在本次任务开始时间仍未执行完成，那等其完成才开始执行本次任务，中间不再等待
     * 如果上一任务占用了不此一次任务的时间，被占用的任务会被跳过
     *
     * @throws InterruptedException
     */
    @Test
    public void scheduleAtFixedRate() throws InterruptedException {

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(4);

        final AtomicLong counter = new AtomicLong();

        //
        executor.scheduleAtFixedRate(() -> {
            long number = counter.incrementAndGet();
            System.out.printf("%d - %d - BEGIN%n", number, System.currentTimeMillis());
            // 会造成第二次任务执行时，第一次任务尚未执行完
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("%d - %d - END%n", number, System.currentTimeMillis());
        }, 1000, 2000, TimeUnit.MILLISECONDS);

        TimeUnit.SECONDS.sleep(30L);

    }

    /**
     * 以相对间隔执行任务（下一次执行任务时，如果上一次任务未执行完成，则该任务向后延）
     *
     * @throws InterruptedException
     */
    @Test
    public void scheduleWithFixedDelay() throws InterruptedException {

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(4);

        final AtomicLong counter = new AtomicLong();

        // 第二次执行的任务会与上次任务保持相对固定间隔时间执行
        executor.scheduleWithFixedDelay(() -> {
            long number = counter.incrementAndGet();
            System.out.printf("%d - %d - BEGIN%n", number, System.currentTimeMillis());
            // 会造成第二次任务执行时，第一次任务尚未执行完
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("%d - %d - END%n", number, System.currentTimeMillis());
        }, 1000, 2000, TimeUnit.MILLISECONDS);

        TimeUnit.SECONDS.sleep(30L);

    }

}
