package com.huawei.hwcloud.tarus.kvstore.race;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

import static junit.framework.TestCase.assertTrue;

@Ignore
public class FileBenchTest {

    String mappedFileName = "/tmp/mappedfile.data";
    String channelFileName = "/tmp/filechannels.data";

    String dataFileName = "/tmp/datafile.data";

    @Before
    public void before() {
        File file = new File(mappedFileName);
        if(file.exists()) {
            file.delete();
        }

        file = new File(channelFileName);
        if(file.exists()) {
            file.delete();
        }

       file = new File(dataFileName);
        if(file.exists()) {
            file.delete();
        }
    }

    @Test
    public void test_write() throws IOException {
        int count = 1022;
        byte[][] values = new byte[count][];
        for (int i = 0; i < count; i++) {
            values[i] = RandomStringUtils.randomAlphabetic(4 * 1024).getBytes();
        }

        test_mapped_file_write_and_read(count, values);
    }

    private void test_mapped_file_write_and_read(int count, byte[][] values) throws IOException {
//        MappedFile mappedFile = new MappedFile(mappedFileName, Constants.PAGE_SIZE, 5);
//        long pos = 0;
//        for (int i = 0; i < count; i++) {
//            mappedFile.write(values[i]);
//            pos += 4 * 1024;
//        }
//
//
//        byte[] valueRel = new byte[1024 * 4];
//        pos = 0;
//        for (int i = 0; i < count; i++) {
//            mappedFile.read(pos, valueRel);
//            assertTrue(Arrays.equals(values[i], valueRel));
//            pos += 4 * 1024;
//        }
    }

    private void test_mapped_file_write(int count, byte[][] values) throws IOException {
//        MappedFile mappedFile = new MappedFile(mappedFileName, Constants.PAGE_SIZE, 5);
//        for (int i = 0; i < count; i++) {
//            mappedFile.write(values[i]);
//        }
    }


    private void test_file_channel_write_and_read(int count, byte[][] values) throws IOException {
        FileChannel fileChannel = Channels.createFileChannel(new File(channelFileName));
        ByteBuffer valueBuf = ByteBuffer.allocateDirect(4 * 1024);
        long pos = 0;
        for (int i = 0; i < count; i++) {
            valueBuf.clear();
            valueBuf.put(values[i]);
            valueBuf.flip();

            fileChannel.write(valueBuf, pos);
            pos += 4 * 1024;
        }
        pos = 0;


        ByteBuffer valueBuf1 = ByteBuffer.allocate(4 * 1024);
        for (int i = 0; i < count; i++) {
            valueBuf1.clear();
            fileChannel.read(valueBuf1, pos);
            valueBuf1.flip();
            assertTrue(Arrays.equals(values[i], valueBuf1.array()));
            pos += 4 * 1024;
        }
    }



    private void test_file_channel_write(int count, byte[][] values) throws IOException {
        FileChannel fileChannel = Channels.createFileChannel(new File(channelFileName));
        ByteBuffer valueBuf = ByteBuffer.allocateDirect(4 * 1024);
        long pos = 0;
        for (int i = 0; i < count; i++) {
            valueBuf.clear();
            valueBuf.put(values[i]);
            valueBuf.flip();

            fileChannel.write(valueBuf, pos);
            pos += 4 * 1024;
        }
    }

    private void test_data_file_write(int count, byte[][] values) throws IOException {
        DataFile dataFile = new DataFile(dataFileName, Constants.PAGE_SIZE, 5);
        for (int i = 0; i < count; i++) {
            dataFile.write(values[i]);
        }
    }

    @Test
    public void test_bench_mark() throws IOException {
        int count = 102200;
        byte[][] values = new byte[count][];
        for (int i = 0; i < count; i++) {
            values[i] = RandomStringUtils.randomAlphabetic(4 * 1024).getBytes();
        }
        long t1 = System.currentTimeMillis();
        test_mapped_file_write_and_read(count, values);
        long t2 = System.currentTimeMillis();
        test_file_channel_write_and_read(count,values);
        long t3 = System.currentTimeMillis();

        System.out.println(t2 - t1);
        System.out.println(t3 - t2);
    }



    @Test
    public void test_datafile_vs_mappedfile_vs_filechannel_bench_mark() throws IOException {
        int count = Constants.TOTAL_SIZE / 50;
        byte[][] values = new byte[count][];
        for (int i = 0; i < count; i++) {
            values[i] = RandomStringUtils.randomAlphabetic(4 * 1024).getBytes();
        }
        long t1 = System.currentTimeMillis();
        test_mapped_file_write(count, values);
        long t2 = System.currentTimeMillis();
        test_data_file_write(count,values);
        long t3 = System.currentTimeMillis();

        test_file_channel_write(count,values);
        long t4 = System.currentTimeMillis();

        System.out.println((t2 - t1));
        System.out.println((t3 - t2));
        System.out.println((t4 - t3));
    }

    @Test
    public void test_datafile_write() throws IOException {
        int count = 102;
        byte[][] values = new byte[count][];
        for (int i = 0; i < count; i++) {
            values[i] = RandomStringUtils.randomAlphabetic(4 * 1024).getBytes();
        }
        long t1 = System.currentTimeMillis();
        test_data_file_write(count,values);
        long t2 = System.currentTimeMillis();

        System.out.println(t2 - t1);
    }
}
