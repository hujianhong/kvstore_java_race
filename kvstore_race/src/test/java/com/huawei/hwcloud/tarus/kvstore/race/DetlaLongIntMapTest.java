package com.huawei.hwcloud.tarus.kvstore.race;

import com.carrotsearch.hppc.LongIntOpenHashMap;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;

@Ignore
public class DetlaLongIntMapTest {


    @Test
    public void test_init_index_map() {
        long t1 = System.currentTimeMillis();
        for(int i = 0;i < 16;i ++) {
            DeltaLongIntMap intMap = new DeltaLongIntMap(Constants.TOTAL_SIZE);
        }
        long t2 = System.currentTimeMillis();
        System.out.println(t2 - t1);
    }


    @Test
    public void test_put_and_get() {

        int step = 4000000;
        DeltaLongIntMap deltaLongIntMap = new DeltaLongIntMap(step);
        LongIntOpenHashMap openHashMap = new LongIntOpenHashMap(step);
        HashMap<Long,Integer> hashMap = new HashMap<>(step);

        long start = Integer.MAX_VALUE * 2;
        long end = start + 4000000;


        long t1 = System.currentTimeMillis();
        long key = start;
        int i = 1;
        while (key < end) {
            deltaLongIntMap.put(key, i);
            key++;
            i++;
        }

        key = start;
        i = 1;
        while (key < end) {
            assertEquals(i, deltaLongIntMap.get(key));
            key++;
            i++;
        }

        long t2 = System.currentTimeMillis();



        key = start;
        i = 1;
        while (key < end) {
            openHashMap.put(key, i);
            key++;
            i++;
        }

        key = start;
        i = 1;
        while (key < end) {
            assertEquals(i, openHashMap.get(key));
            key++;
            i++;
        }

        long t3 = System.currentTimeMillis();


        key = start;
        i = 1;
        while (key < end) {
            hashMap.put(key, i);
            key++;
            i++;
        }

        key = start;
        i = 1;
        while (key < end) {
            assertEquals(i, (int)hashMap.get(key));
            key++;
            i++;
        }

        long t4 = System.currentTimeMillis();



        System.out.println(t2 - t1);
        System.out.println(t3 - t2);
        System.out.println(t4 - t3);
    }
}
