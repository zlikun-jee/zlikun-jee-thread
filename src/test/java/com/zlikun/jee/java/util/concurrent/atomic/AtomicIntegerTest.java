package com.zlikun.jee.java.util.concurrent.atomic;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

/**
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018/8/10 9:57
 */
public class AtomicIntegerTest {

    @Test
    public void test() {

        AtomicInteger ai = new AtomicInteger();

        assertEquals(0, ai.get());

        // 下面两个方法用于设置计数器的值（覆盖式）
        // 区别在于第一个方法立即生效，第二个方法则会有一定的延迟，
        // 其最终保证值为新值，但在一段时间内，其它线程读到的仍是旧值
        ai.set(16);
        ai.lazySet(64);

        // 无锁自增、自减，基于CAS（compare-and-set）原理
        // 下面是自增和自减的用法，区别在于先增还是先读，但其都保证操作的原子性
        ai.incrementAndGet();
        ai.getAndIncrement();
        ai.decrementAndGet();
        ai.getAndDecrement();

    }

}
