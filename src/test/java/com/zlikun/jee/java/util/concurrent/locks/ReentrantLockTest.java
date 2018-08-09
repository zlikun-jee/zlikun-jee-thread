package com.zlikun.jee.java.util.concurrent.locks;

import org.junit.Test;

import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.*;

/**
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018/8/9 18:48
 */
public class ReentrantLockTest {

    @Test
    public void test() {

        ReentrantLock lock = new ReentrantLock();

        lock.lock();
        lock.lock();
        lock.lock();

        // 当前计数器为3
        assertEquals(3, lock.getHoldCount());



        lock.unlock();
        lock.unlock();

        // 释放两次锁，计数器为1
        assertEquals(1, lock.getHoldCount());

        // 可以通过isLocked()判断是否加锁
        assertTrue(lock.isLocked());

        lock.unlock();

        assertFalse(lock.isLocked());

        // 最后一次也释放后，计数器归0
        assertEquals(0, lock.getHoldCount());

        // 锁被完全释放后，再次释放会抛出异常
        if (lock.isLocked()) {
            lock.unlock();
        }
    }

}
