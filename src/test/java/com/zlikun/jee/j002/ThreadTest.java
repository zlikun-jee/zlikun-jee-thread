package com.zlikun.jee.j002;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018/8/9 13:21
 */
public class ThreadTest {

    /**
     * 线程状态：
     * NEW A thread that has not yet started is in this state.
     * RUNNABLE A thread executing in the Java virtual machine is in this state.
     * BLOCKED A thread that is blocked waiting for a monitor lock is in this state.
     * WAITING A thread that is waiting indefinitely for another thread to perform a particular action is in this state.
     * TIMED_WAITING A thread that is waiting for another thread to perform an action for up to a specified waiting time is in this state.
     * TERMINATED A thread that has exited is in this state.
     * @throws InterruptedException
     */
    @Test
    public void test() throws InterruptedException {

        // 构建一个线程，每100毫秒，打印一个数字
        Thread t = new Thread(() -> {
            for (int i = 0; i < 1_000_000; i++) {
                System.out.println(i);
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.setName("t-001");
        t.setDaemon(false);

        // 刚创建的线程处于NEW状态
        assertEquals(Thread.State.NEW, t.getState());

        t.start();

        // 线程开始运行后，处于运行中状态
        assertEquals(Thread.State.RUNNABLE, t.getState());

        System.out.println("线程已经在运行了 ...");

        t.join(1000L);

        // 线程此时处于TIMED_WAITING状态
        assertEquals(Thread.State.TIMED_WAITING, t.getState());

        System.out.println("主线程退出了 ...");

    }

}
