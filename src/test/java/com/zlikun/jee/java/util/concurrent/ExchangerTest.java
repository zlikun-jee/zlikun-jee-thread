package com.zlikun.jee.java.util.concurrent;

import org.junit.Test;

import java.util.concurrent.Exchanger;

/**
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018/8/10 10:52
 */
public class ExchangerTest {

    @Test
    public void test() throws InterruptedException {

        // 用于线程间数据交换：线程一执行exchange()方法后会等待第二个线程也执行该方法
        // 第二个线程也执行（双方都到达同步点）exchange()方法时，即可交换数据
        // 数据传递是相互的，所以无所谓哪个线程先执行，哪个线程先得到数据
        final Exchanger<Integer> exchanger = new Exchanger<>();

        // 线程一
        new Thread(() -> {
            // 员工A录入金额：10000
            int amount = 10000;
            System.out.println("员工A录入金额" + amount);
            // 开始交换数据
            try {
                int otherAmount = exchanger.exchange(amount);
                if (otherAmount != amount) {
                    System.out.printf("[A]员工A[%d]与员工B[%d]录入数据不一致%n", otherAmount, amount);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        // 线程二
        new Thread(() -> {
            // 员工B录入金额：1000
            int amount = 1000;
            System.out.println("员工B录入金额" + amount);
            // 开始交换数据
            try {
                int otherAmount = exchanger.exchange(amount);
                if (otherAmount != amount) {
                    System.out.printf("[B]员工A[%d]与员工B[%d]录入数据不一致%n", otherAmount, amount);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        Thread.currentThread().join(500L);

    }

}
