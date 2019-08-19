package com.huawei.hwcloud.tarus.kvstore.race;

import org.junit.Ignore;
import org.junit.Test;

import static org.apache.commons.lang.math.RandomUtils.nextLong;
import static org.junit.Assert.assertEquals;

@Ignore
public class ModBenchTest {



    @Test
    public void test_div_correctly() {
        for (int i = 0; i < Constants.TOTAL_SIZE; i++) {
            long offset = ((long) i) * Constants.VALUE_SIZE;
            long pos = (offset / Constants.PAGE_SIZE * Constants.PAGE_SIZE);
            long pos1 = (offset & Constants.PAGE_SIZE_DIV_MASK);
            assertEquals(pos, pos1);
        }
    }


    @Test
    public void test_div_correctly2() {
        for (int i = 0; i < Constants.TOTAL_SIZE; i++) {
            long offset = nextLong();
            long pos = (offset / Constants.PAGE_SIZE * Constants.PAGE_SIZE);
            long pos1 = (offset & Constants.PAGE_SIZE_DIV_MASK);
            assertEquals(pos, pos1);
        }
    }


    @Test
    public void test_div_benchmark() {
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < Constants.TOTAL_SIZE; i++) {
            long offset = ((long) i) * Constants.VALUE_SIZE;
            long pos1 = (offset & Constants.PAGE_SIZE_DIV_MASK);
        }
        long t2 = System.currentTimeMillis();
        for (int i = 0; i < Constants.TOTAL_SIZE; i++) {
            long offset = ((long) i) * Constants.VALUE_SIZE;
            long pos = (offset / Constants.PAGE_SIZE * Constants.PAGE_SIZE);
        }
        long t3 = System.currentTimeMillis();
        System.out.println(t2 - t1);
        System.out.println(t3 - t2);
    }


    @Test
    public void test_mod_correctly() {
        for (int i = 0; i < Constants.TOTAL_SIZE; i++) {
            long offset = ((long) i) * Constants.VALUE_SIZE;
            int mod1 = (int) (offset % Constants.PAGE_SIZE);
            int mod2 = (int) (offset & Constants.PAGE_SIZE_MOD_MASK);
            assertEquals(mod1, mod2);
        }
    }

    @Test
    public void test_mod_benchmark() {
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < Constants.TOTAL_SIZE; i++) {
            long offset = ((long) i) * Constants.VALUE_SIZE;
            long pagNum = offset / Constants.PAGE_SIZE * Constants.PAGE_SIZE;
            int mod2 = (int) (offset & Constants.PAGE_SIZE_MOD_MASK);
        }
        long t2 = System.currentTimeMillis();
        for (int i = 0; i < Constants.TOTAL_SIZE; i++) {
            long offset = ((long) i) * Constants.VALUE_SIZE;
            long pagNum = offset / Constants.PAGE_SIZE * Constants.PAGE_SIZE;
            int mod1 = (int) (offset % Constants.PAGE_SIZE);
        }
        long t3 = System.currentTimeMillis();
        System.out.println(t2 - t1);
        System.out.println(t3 - t2);
    }
}
