package com.zlikun.jee.java.util.concurrent.atomic;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2018/8/10 10:05
 */
public class AtomicReferenceTest {

    static class Data {
        private Integer id;
        private String name;

        public Data(Integer id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Test
    public void test() {

        // 原子引用对象
        AtomicReference<Data> ar = new AtomicReference<>(new Data(1, "Peter"));

        // 原子更新整个对象（替换对象，返回替换前的对象）
        Data data2 = new Data(2, "Ashe");
        Data oldData = ar.getAndSet(data2);
        assertEquals("Peter", oldData.name);

        // 原子更新对象（更新对象，返回更新后的对象）
        Data newData = ar.getAndUpdate(data -> {
            data.name = "Jane";
            return data;
        });
        assertEquals(Integer.valueOf(2), newData.id);
        assertEquals("Jane", newData.name);
        assertTrue(data2 == newData);

    }

}
