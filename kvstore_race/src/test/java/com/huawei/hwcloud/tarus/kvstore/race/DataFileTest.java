package com.huawei.hwcloud.tarus.kvstore.race;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@Ignore
public class DataFileTest {



    @Test
    public void test_can_write_and_read_benchmark() throws IOException {
//        String filePath = "/tmp/data_file_b.1";
//        String cacheFile = "/tmp/data_file_b.1.cache";
//        Tools.deleteFile(filePath);
//        Tools.deleteFile(cacheFile);
//
//        String filePath1 = "/tmp/data_file_b.2";
//        String cacheFile1 = "/tmp/data_file_b.2.cache";
//        Tools.deleteFile(filePath1);
//        Tools.deleteFile(cacheFile1);
//
//        int maxItem = 400000;
//        byte[][] values = new byte[maxItem][];
//        for (int i = 0; i < maxItem; i++) {
//            values[i] = RandomStringUtils.randomAlphabetic(Constants.VALUE_SIZE).getBytes();
//        }
//        long t1 = System.currentTimeMillis();
//        DataFile dataFile1 = new DataFile(filePath1, cacheFile1, 512 * 1024, Constants.MAX_CACHE_SIZE);
//        for (int i = 0; i < maxItem; i++) {
//            dataFile1.write(values[i]);
//        }
//
//        long t2 = System.currentTimeMillis();
//        DataFile dataFile = new DataFile(filePath, cacheFile,128 * 1024, Constants.MAX_CACHE_SIZE);
//        for (int i = 0; i < maxItem; i++) {
//            dataFile.write(values[i]);
//        }
//        long t3 = System.currentTimeMillis();
//
//        System.out.println(t2 - t1);
//        System.out.println(t3 - t2);
    }


    @Test
    public void test_can_write_and_read() throws IOException {
        String filePath = "/tmp/data_file.5";
        String cacheFile = "/tmp/data_file.5.cache";
        Tools.deleteFile(filePath);
        Tools.deleteFile(cacheFile);
        DataFile dataFile = new DataFile(filePath, cacheFile);

        int maxItem = 108;
        byte[][] values = new byte[maxItem][];
        for (int i = 0; i < maxItem; i++) {
            values[i] = RandomStringUtils.randomAlphabetic(Constants.VALUE_SIZE).getBytes();
        }

        for (int i = 0; i < maxItem; i++) {
            dataFile.write(values[i]);
        }

        byte[] bytes = new byte[Constants.VALUE_SIZE];
        int pos = 0;
        for (int i = 0; i < maxItem; i++) {
            dataFile.read(pos, bytes);
            pos += bytes.length;
            assertTrue(Arrays.equals(values[i], bytes));
        }
    }

    @Test
    public void test_can_write_and_read_after_flush_when_open() throws IOException {
        String filePath = "/tmp/data_file.5";
        String cacheFile = "/tmp/data_file.5.cache";
        Tools.deleteFile(filePath);
        Tools.deleteFile(cacheFile);
        DataFile dataFile = new DataFile(filePath, cacheFile);

        int maxItem = 32;
        byte[][] values = new byte[maxItem][];
        for (int i = 0; i < maxItem; i++) {
            values[i] = RandomStringUtils.randomAlphabetic(Constants.VALUE_SIZE).getBytes();
        }

        for (int i = 0; i < maxItem; i++) {
            dataFile.write(values[i]);
        }

        byte[] bytes = new byte[Constants.VALUE_SIZE];
        int pos = 0;
        for (int i = 0; i < maxItem; i++) {
            dataFile.read(pos, bytes);
            pos += bytes.length;
            assertTrue(Arrays.equals(values[i], bytes));
        }
        dataFile.close();

        dataFile = new DataFile(filePath, cacheFile);
        dataFile.recovery(32);
        assertEquals(maxItem * Constants.VALUE_SIZE, dataFile.size());
    }


    @Test
    public void test_can_write_and_read_when_reopen() throws IOException {
        String filePath = "/tmp/data_file.6";
        String cacheFile = "/tmp/data_file.6.cache";
        Tools.deleteFile(filePath);
        Tools.deleteFile(cacheFile);
        DataFile dataFile = new DataFile(filePath, cacheFile);

        int maxItem = 108;
        byte[][] values = new byte[maxItem][];
        for (int i = 0; i < maxItem; i++) {
            values[i] = RandomStringUtils.randomAlphabetic(Constants.VALUE_SIZE).getBytes();
        }

        for (int i = 0; i < maxItem; i++) {
            dataFile.write(values[i]);
        }

        byte[] bytes = new byte[Constants.VALUE_SIZE];
        int pos = 0;
        for (int i = 0; i < maxItem; i++) {
            dataFile.read(pos, bytes);
            pos += bytes.length;
            assertTrue(Arrays.equals(values[i], bytes));
        }

        dataFile.close();

        dataFile =  new DataFile(filePath, Constants.PAGE_SIZE, Constants.MAX_CACHE_SIZE);
        dataFile.recovery(maxItem);
        assertEquals(maxItem * Constants.VALUE_SIZE, dataFile.size());

        pos = 0;
        for (int i = 0; i < maxItem; i++) {
            dataFile.read(pos, bytes);
            pos += bytes.length;
            assertTrue(Arrays.equals(values[i], bytes));
        }
    }

    @Test
    public void test_can_read_from_cache() throws IOException {
        String filePath = "/tmp/data_file.1";
        String cacheFile = "/tmp/data_file.1.cache";
        Tools.deleteFile(filePath);
        Tools.deleteFile(cacheFile);
        DataFile dataFile = new DataFile(filePath, cacheFile);

        int maxItem = 16;
        byte[][] values = new byte[maxItem][];
        for (int i = 0; i < maxItem; i++) {
            values[i] = RandomStringUtils.randomAlphabetic(Constants.VALUE_SIZE).getBytes();
        }

        for (int i = 0; i < maxItem; i++) {
            dataFile.write(values[i]);
        }

        byte[] bytes = new byte[Constants.VALUE_SIZE];
        int pos = 0;
        for (int i = 0; i < maxItem; i++) {
            dataFile.read(pos, bytes);
            pos += bytes.length;
            assertTrue(Arrays.equals(values[i], bytes));
        }
    }

    @Test
    public void test_can_read_from_both_file_and_cache() throws IOException {
        String filePath = "/tmp/data_file.2";
        String cacheFile = "/tmp/data_file.2.cache";
        Tools.deleteFile(filePath);
        Tools.deleteFile(cacheFile);
        DataFile dataFile = new DataFile(filePath, cacheFile);

        int maxItem = 38;
        byte[][] values = new byte[maxItem][];
        for (int i = 0; i < maxItem; i++) {
            values[i] = RandomStringUtils.randomAlphabetic(Constants.VALUE_SIZE).getBytes();
        }

        for (int i = 0; i < maxItem; i++) {
            dataFile.write(values[i]);
        }

        byte[] bytes = new byte[Constants.VALUE_SIZE];
        int pos = 0;
        for (int i = 0; i < maxItem; i++) {
            dataFile.read(pos, bytes);
            pos += bytes.length;
            assertTrue(Arrays.equals(values[i], bytes));
        }
    }


    @Test
    public void test_can_get_file_size() throws IOException {
        String filePath = "/tmp/data_file.3";
        String cacheFile = "/tmp/data_file.3.cache";
        Tools.deleteFile(filePath);
        Tools.deleteFile(cacheFile);
        DataFile dataFile = new DataFile(filePath, cacheFile);

        int maxItem = 38;
        byte[][] values = new byte[maxItem][];
        for (int i = 0; i < maxItem; i++) {
            values[i] = RandomStringUtils.randomAlphabetic(Constants.VALUE_SIZE).getBytes();
        }

        for (int i = 0; i < maxItem; i++) {
            dataFile.write(values[i]);
        }
        assertEquals(38 * Constants.VALUE_SIZE, dataFile.size());
    }


    @Test
    public void test_can_get_file_size_when_reopen_file() throws IOException {
        String filePath = "/tmp/data_file.4";
        String cacheFile = "/tmp/data_file.4.cache";
        Tools.deleteFile(filePath);
        Tools.deleteFile(cacheFile);
        DataFile dataFile = new DataFile(filePath, cacheFile);

        int maxItem = 38;
        byte[][] values = new byte[maxItem][];
        for (int i = 0; i < maxItem; i++) {
            values[i] = RandomStringUtils.randomAlphabetic(Constants.VALUE_SIZE).getBytes();
        }

        for (int i = 0; i < maxItem; i++) {
            dataFile.write(values[i]);
        }
        assertEquals(38 * Constants.VALUE_SIZE, dataFile.size());

        dataFile.close();

        dataFile =  new DataFile(filePath, Constants.PAGE_SIZE, Constants.MAX_CACHE_SIZE);
        dataFile.recovery(38);
        assertEquals(38 * Constants.VALUE_SIZE, dataFile.size());
    }

    @Test
    public void test_can_recovery_when_reopen_file() throws IOException {
        String filePath = "/tmp/data_file.6";
        String cacheFile = "/tmp/data_file.6.cache";
        Tools.deleteFile(filePath);
        Tools.deleteFile(cacheFile);
        DataFile dataFile = new DataFile(filePath, cacheFile);
        long writePosition = 1897775104L;
        dataFile.setWritePosition(writePosition);
        dataFile.recovery(463327);
        assertEquals(31,dataFile.getWriteCacheNum());
    }
}
