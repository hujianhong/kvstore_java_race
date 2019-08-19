package com.huawei.hwcloud.tarus.kvstore.race;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Ignore
public class FileChannelTest {
    String filePath = "/Users/jianhong/work/filechannel.test";


    @Test
    public void test() throws IOException {
        FileChannel writeChannel = Channels.createFileChannel(new File(filePath));

//        MappedByteBuffer buffer = writeChannel.map(FileChannel.MapMode.READ_WRITE,0,16 * 4 * 1024);
//        buffer.put("aaabbb".getBytes());

//        buffer.force();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(100).put("aaaaaa".getBytes());
        byteBuffer.flip();
        writeChannel.write(byteBuffer);
//
//        System.out.println("------");

//        ByteBuffer byteBuffer = ByteBuffer.allocate(6);
//
//
//        writeChannel.read(byteBuffer, 0);
//        System.out.println("------");
//        System.out.println(new String(byteBuffer.array()));

    }


    @Test
    public void test_benchmark() {
        int count = 1028;
        byte[][] values = new byte[count][];
        for (int i = 0; i < count; i++) {
            values[i] = RandomStringUtils.randomAlphabetic(4 * 1024).getBytes();
        }

    }
}
