package com.zlikun.jee.java.util.concurrent.locks;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.junit.Assert.assertEquals;

/**
 * 读写锁用于读多写少的场景下
 *
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018/8/9 19:04
 */
public class ReentrantReadWriteLockTest {

    @Test
    public void test() {

        Cache cache = new Cache();

        // 写
        cache.put("name", "zlikun");
        // 读
        assertEquals("zlikun", cache.get("name"));


    }

    static class Cache {

        Map<String, Object> map = new HashMap<>();
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        Lock r = lock.readLock();
        Lock w = lock.writeLock();

        public Object get(String key) {
            r.lock();
            try {
                return this.map.get(key);
            } finally {
                r.unlock();
            }
        }

        public void put(String key, Object value) {
            w.lock();
            try {
                this.map.put(key, value);
            } finally {
                w.unlock();
            }
        }

        public void clear() {
            w.lock();
            try {
                map.clear();
            } finally {
                w.unlock();
            }
        }

    }

}
