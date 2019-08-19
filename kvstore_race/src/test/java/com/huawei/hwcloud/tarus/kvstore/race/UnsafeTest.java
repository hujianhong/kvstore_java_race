package com.huawei.hwcloud.tarus.kvstore.race;

import junit.framework.TestCase;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Ignore;
import org.junit.Test;
import sun.misc.Unsafe;
import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;
import java.util.Arrays;

@Ignore
public class UnsafeTest {

    public static final Unsafe UNSAFE = Tools.unsafe();


    @Test
    public void test_unsafecopy() {
        int maxItem = 5000;
        byte[][] values = new byte[maxItem][];
        for (int i = 0; i < maxItem; i++) {
            values[i] = RandomStringUtils.randomAlphabetic(Constants.VALUE_SIZE).getBytes();
        }
        ByteBuffer directByteBuffer = ByteBuffer.allocateDirect(maxItem * Constants.VALUE_SIZE);
        long adress1 = ((DirectBuffer)directByteBuffer).address();
        ByteBuffer heapByteBuffer = ByteBuffer.allocateDirect(maxItem * Constants.VALUE_SIZE);
//        long adress2 = ((DirectBuffer)heapByteBuffer).address();
        int pos = 0;
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < maxItem; i++) {
            UNSAFE.copyMemory(values[i],16,null,pos + adress1,values.length);
            pos += values.length;
        }
        long t2 = System.currentTimeMillis();
        for (int i = 0; i < maxItem; i++) {
            heapByteBuffer.put(values[i]);
        }
        long t3 = System.currentTimeMillis();

        System.out.println(t2 - t1);
        System.out.println(t3 - t2);
    }

    @Test
    public void test_unsafecopy2() {
        int maxItem = 5000;
        ByteBuffer directByteBuffer = ByteBuffer.allocateDirect(maxItem * Constants.VALUE_SIZE);
        long adress1 = ((DirectBuffer)directByteBuffer).address();
        System.out.println("1111");
        int pos = 0;
        byte[] raw;

        byte[][] values = new byte[maxItem][];
        for (int i = 0; i < maxItem; i++) {
            raw = RandomStringUtils.randomAlphabetic(Constants.VALUE_SIZE).getBytes();
            UNSAFE.copyMemory(raw, 16, null, adress1 + pos , Constants.VALUE_SIZE);
            values[i] = raw;
            pos += Constants.VALUE_SIZE;
        }
        System.out.println("--------------");
        byte[] value = new byte[Constants.VALUE_SIZE];
        pos = 0;
        for (int i = 0; i < maxItem; i++) {
            UNSAFE.copyMemory(null, pos + adress1, value, 16,Constants.VALUE_SIZE);
            pos += Constants.VALUE_SIZE;
            TestCase.assertTrue(Arrays.equals(values[i], value));
        }
    }
}
