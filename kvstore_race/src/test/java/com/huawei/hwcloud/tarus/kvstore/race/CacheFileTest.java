package com.huawei.hwcloud.tarus.kvstore.race;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@Ignore
public class CacheFileTest {


    String filePath = "/tmp/cachefile.data";

    @Before
    public void before() {
        File file = new File(filePath);
        if(file.exists()) {
            file.delete();
        }
    }

    @Test
    public void test_put_and_read_use_view() throws IOException {
        int maxItem = Constants.PAGE_SIZE / Constants.VALUE_SIZE;
        CacheFile cacheFile = new CacheFile(filePath, maxItem);
        byte[][] values = new byte[maxItem][];
        for (int i = 0; i < maxItem; i++) {
            values[i] = RandomStringUtils.randomAlphabetic(Constants.VALUE_SIZE).getBytes();
        }

        for (int i = 0; i < maxItem; i++) {
            cacheFile.write(values[i]);
        }

        ByteBuffer buffer = cacheFile.view();
        byte[] bytes = new byte[Constants.VALUE_SIZE];
        for (int i = 0; i < maxItem; i++) {
           buffer.get(bytes);
           assertTrue(Arrays.equals(values[i], bytes));
        }
    }


    @Test
    public void test_can_get_count() throws IOException {
        int maxItem = Constants.PAGE_SIZE / Constants.VALUE_SIZE;
        String filePath = "/tmp/cacheFile.1";
        Tools.deleteFile(filePath);
        CacheFile cacheFile = new CacheFile(filePath, maxItem);
        byte[][] values = new byte[maxItem][];
        for (int i = 0; i < maxItem; i++) {
            values[i] = RandomStringUtils.randomAlphabetic(Constants.VALUE_SIZE).getBytes();
        }
        cacheFile.write(values[0]);
        assertEquals(1, cacheFile.getCount());

        cacheFile.write(values[1]);

        assertEquals(2, cacheFile.getCount());
    }


    @Test
    public void test_can_reopen_to_recovery_data() throws IOException {
        int maxItem = Constants.PAGE_SIZE / Constants.VALUE_SIZE;
        String filePath = "/tmp/cacheFile.2";
        Tools.deleteFile(filePath);
        CacheFile cacheFile = new CacheFile(filePath, maxItem);
        byte[][] values = new byte[maxItem][];
        for (int i = 0; i < maxItem; i++) {
            values[i] = RandomStringUtils.randomAlphabetic(Constants.VALUE_SIZE).getBytes();
        }
        cacheFile.write(values[0]);
        assertEquals(1, cacheFile.getCount());
        cacheFile.write(values[1]);
        assertEquals(2, cacheFile.getCount());

        cacheFile.close();

        cacheFile = new CacheFile(filePath, maxItem);
        cacheFile.reset(2);
        assertEquals(2, cacheFile.getCount());
        ByteBuffer buffer = cacheFile.view();
        byte[] bytes = new byte[Constants.VALUE_SIZE];
        for(int i = 0;i < 2;i ++) {
            buffer.get(bytes);
            assertTrue(Arrays.equals(values[i], bytes));
        }
    }

    @Test
    public void test_can_write_data_after_open() throws IOException {
        String filePath = "/tmp/cacheFile.3";
        Tools.deleteFile(filePath);
        int maxItem = 16;
        CacheFile cacheFile = new CacheFile(filePath, maxItem);
        byte[][] values = new byte[maxItem][];
        for (int i = 0; i < maxItem; i++) {
            values[i] = RandomStringUtils.randomAlphabetic(Constants.VALUE_SIZE).getBytes();
        }
        cacheFile.write(values[0]);
        assertEquals(1, cacheFile.getCount());
        cacheFile.write(values[1]);
        assertEquals(2, cacheFile.getCount());

        cacheFile.close();

        cacheFile = new CacheFile(filePath, maxItem);
        cacheFile.reset(2);
        assertEquals(2, cacheFile.getCount());

        for(int i = 0;i < 5; i ++) {
            cacheFile.write(values[2 + i]);
        }

        assertEquals(7, cacheFile.getCount());

        ByteBuffer buffer = cacheFile.view();
        byte[] bytes = new byte[Constants.VALUE_SIZE];
        for(int i = 0;i < 7;i ++) {
            buffer.get(bytes);
            assertTrue(Arrays.equals(values[i], bytes));
        }
    }
}
