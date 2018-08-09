package com.zlikun.jee.java.util.concurrent.locks;

import org.junit.Test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018/8/9 18:39
 */
public class LockTest {

    @Test
    public void test() {
        // 《Java并发编程的艺术》
        // 不要将获取锁的过程写在try块中，因为如果在获取锁（自定义锁的实现）时发生了异常，
        // 异常抛出的同时，也会导致锁无故释放。
        Lock lock = new ReentrantLock();
        lock.lock();
        try {
            System.out.println("这是一段并发执行的业务逻辑");
        } finally {
            lock.unlock();
        }
    }

}
