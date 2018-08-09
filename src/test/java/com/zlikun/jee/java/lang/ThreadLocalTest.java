package com.zlikun.jee.java.lang;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018/8/9 18:22
 */
public class ThreadLocalTest {

    private ThreadLocal<String> local = new ThreadLocal<>();

    @Test
    public void test() {

        ExecutorService executor = Executors.newFixedThreadPool(4);

        // 只会输出4个数字，因为线程池里的线程对象是复用的，而ThreadLocal里存的值是与线程绑定的，所以只会有4个输出
        for (int i = 0; i < 1 << 3; i++) {
            executor.execute(() -> {
                if (local.get() != null) {
                    System.out.println(local.get());
                } else {
                    local.set(Long.toString(new Random().nextLong() & 255));
                }
            });
        }

        executor.shutdown();
        while (!executor.isTerminated()) ;

    }

}
