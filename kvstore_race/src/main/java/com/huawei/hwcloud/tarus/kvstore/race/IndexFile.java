package com.huawei.hwcloud.tarus.kvstore.race;

import sun.misc.Unsafe;
import sun.nio.ch.DirectBuffer;

import java.io.File;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class IndexFile {
    public static final Unsafe UNSAFE = Tools.unsafe();

    private FileChannel fileChannel;

    private MappedByteBuffer buffer;

    private int count;

    private int writePos;

    private DeltaLongIntMap indexMap;

    private final long address;

    public IndexFile(String filePath, DeltaLongIntMap indexMap) throws IOException {
        File file = new File(filePath);
        boolean fileExist = file.exists();
        this.fileChannel = Channels.createFileChannel(filePath);
        int mapSize = Constants.TOTAL_SIZE * Constants.KEY_SIZE + Integer.BYTES;
        this.buffer = this.fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, mapSize);
        this.address = ((DirectBuffer) this.buffer).address();
        this.count = UNSAFE.getInt(this.address);
        this.writePos += 4;
        this.indexMap = indexMap;
        if (fileExist) {
            loadIndex();
        }
    }

    private void loadIndex() {
        int num = 0;
        while (num < count) {
            long value = UNSAFE.getLong(this.address + this.writePos);
            this.writePos += Long.BYTES;
            this.indexMap.put(value, ++num);
        }
    }

    public void writeLong(long value) {
        UNSAFE.putLong(address + this.writePos, value);
        this.writePos += Long.BYTES;
        count++;
        UNSAFE.putInt(address, count);
        this.indexMap.put(value, count);
    }


    public void readReady() {
        this.writePos = Integer.BYTES;
    }

    public long readLong() {
        long value = UNSAFE.getLong(this.address + this.writePos);
        this.writePos += Long.BYTES;
        return value;
    }

    public int getCount() {
        return this.count;
    }

    public void flush() throws IOException {
//        this.buffer.force();
    }

    public void close() throws IOException {
//        this.fileChannel.close();
    }

    public int getOffset(long key) {
        return this.indexMap.get(key);
    }
}
