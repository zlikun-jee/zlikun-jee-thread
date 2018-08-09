package com.zlikun.jee.j005;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * 构建单例时使用双重检查真的能保证只有一个实例么？
 *
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018/8/9 15:34
 */
public class DoubleCheckTest {

    static class Foo {
        public Foo() {
            // 随机休眠[0, 255]毫秒，模拟构造过程
            try {
                Thread.sleep(new Random().nextLong() & 255);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("--init--");
        }

        public void invoke() {
            // System.out.println("--invoke--");
        }
    }

    private Foo foo;

    @Test
    public void test() throws InterruptedException {
        final int loop = 1024;

        CountDownLatch latch = new CountDownLatch(loop);

        // 使用大量线程来调用该方法，测试构造方法是否只被执行过一次
        for (int i = 0; i < loop; i++) {
            new Thread(() -> {
                invoke();
                latch.countDown();
            }).start();
        }

        latch.await();

    }

    /**
     * 使用双重检查保证只有一个例（针对并发场景下）
     * 这里不返回实例，而是直接调用实例方法，检查是否会抛出空指针异常
     */
    private void invoke() {
        if (foo == null) {
            synchronized (this) {
                if (foo == null) {
                    foo = new Foo();
                }
            }
        }
        foo.invoke();
    }

}
