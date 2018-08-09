package com.zlikun.jee.j004;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 线程间通知机制：wait/notify，这两个方法必须使用在锁代码块中，即其允许被执行的先决条件是得到锁(synchronized)
 *
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018/8/9 14:50
 */
public class NotificationTest {

    @Test
    public void test() throws InterruptedException {

        // 启动生产者
        Producer producer = new Producer();
        producer.start();
        // 启动消费者
        Stream.of(new Consumer("A", producer),
                new Consumer("B", producer),
                new Consumer("C", producer))
                .forEach(Thread::start);

        Thread.currentThread().join(5000L);

    }

    static class Producer extends Thread {
        final List<String> list = new ArrayList();

        @Override
        public void run() {
            while (true) {
                // 每隔200毫秒生产一条消息
                try {
                    Thread.sleep(200L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 生产者持续生产消息，但消费者的数量远大于生产者，所以当消息被消费完时会wait，如果有生产新的消息则需要notify
                synchronized (list) {
                    list.add("A message .");
                    list.notify();
                }
            }
        }

        public String pop() {
            synchronized (list) {
                if (list.isEmpty()) {
                    try {
                        list.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return list.remove(0);
            }
        }
    }

    static class Consumer extends Thread {
        private Producer producer;

        public Consumer(String name, Producer producer) {
            super(name);
            this.producer = producer;
        }

        @Override
        public void run() {
            while (true) {
                // 消费消息
                System.out.printf("%s - %s%n", this.getName(), this.producer.pop());
            }
        }
    }

}
