package com.huawei.hwcloud.tarus.kvstore.race;

import com.huawei.hwcloud.tarus.kvstore.common.KVStoreRace;
import com.huawei.hwcloud.tarus.kvstore.common.Ref;
import com.huawei.hwcloud.tarus.kvstore.exception.KVSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class EngineKVStoreRace implements KVStoreRace {
    private static final Logger LOG = LoggerFactory.getLogger(EngineKVStoreRace.class);

    private int threadNum;
    private String dir;

    private IndexFile indexFile;

    private DataFile dataFile;

    private final byte[] valueContainer = new byte[Constants.VALUE_SIZE];

    private DeltaLongIntMap indexMap = new DeltaLongIntMap(Constants.TOTAL_SIZE);

    private boolean inited = false;

    @Override
    public boolean init(final String dir, final int threadNum) throws KVSException {
        this.threadNum = threadNum;
        this.dir = dir;
        return true;
    }

//    private long initedTime = 0;
//
//    private long writeIndexTime = 0;
//
//    private long writeDataTime = 0;

    private boolean doInit() {
        try {
//            long s = System.currentTimeMillis();
            File dirFile = new File(this.dir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }

            String indexFilePath = this.dir + "/" + this.threadNum + ".key";
            String dataFilePath = this.dir + "/" + this.threadNum + ".value";
            String cacheFilePath = this.dir + "/" + this.threadNum + ".cache";

            this.indexFile = new IndexFile(indexFilePath, this.indexMap);
            int valueCount = this.indexFile.getCount();

            this.dataFile = new DataFile(dataFilePath, cacheFilePath);
            this.dataFile.recovery(valueCount);
            this.inited = true;
//            this.initedTime = System.currentTimeMillis() - s;
            return true;
        } catch (Exception e) {
            throw new KVSException(e.getMessage(), e);
        }

    }

    @Override
    public long set(final String key, final byte[] val) throws KVSException {
        if (!this.inited) {
            doInit();
        }
        try {
//            long t1 = System.currentTimeMillis();
            this.dataFile.write(val);
//            long t2 = System.currentTimeMillis();
            long longValue = keyToLong(key);
            this.indexFile.writeLong(longValue);
//            long t3 = System.currentTimeMillis();
//            this.writeDataTime += (t2 - t1);
//            this.writeIndexTime += (t3 - t2);
            return 1;
        } catch (IOException e) {
            throw new KVSException(e.getMessage(), e);
        }
    }

    @Override
    public long get(final String key, final Ref<byte[]> val) throws KVSException {
        if (!this.inited) {
            doInit();
        }
        try {
            int offset = this.indexFile.getOffset(keyToLong(key));
            if (offset <= 0) {
                LOG.info("key:{} not found", key);
                val.setValue(null);
                return -1;
            }
            int len = this.dataFile.read(((long) (offset - 1)) * Constants.VALUE_SIZE, valueContainer);
            if (len <= 0) {
                val.setValue(null);
                return -1;
            }
            val.setValue(valueContainer);
            return 1;
        } catch (IOException e) {
            throw new KVSException(e.getMessage(), e);
        }
    }

    @Override
    public void close() {
        if (!this.inited) {
            return;
        }
//        LOG.info("inited time: {},write index time,{},write data time:{}", initedTime, writeIndexTime, writeDataTime);
        try {
            this.indexFile.close();
        } catch (IOException e) {
            LOG.error("close file channel error", e);
        }
        try {
            this.dataFile.close();
        } catch (IOException e) {
            LOG.error("close file channel error", e);
        }
    }

    @Override
    public void flush() {
        if (!this.inited) {
            return;
        }
        try {
            this.indexFile.flush();
        } catch (IOException e) {
            LOG.error("close file channel error", e);
        }
        try {
            this.dataFile.flush();
        } catch (IOException e) {
            LOG.error("close file channel error", e);
        }
    }

    public static long keyToLong(String key) {
        long value = key.charAt(0) - '0';
        for (int i = 1, len = key.length(); i < len; i++) {
            value = (value << 3) + (value << 1) + (key.charAt(i) - '0');
        }
        return value;
    }
}
