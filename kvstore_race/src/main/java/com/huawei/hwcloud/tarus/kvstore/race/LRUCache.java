package com.huawei.hwcloud.tarus.kvstore.race;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LRUCache extends LinkedHashMap<Long, ByteBuffer> {

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private final int maxCapacity;

    public LRUCache(int maxCapacity) {
        super(maxCapacity, DEFAULT_LOAD_FACTOR, true);
        this.maxCapacity = maxCapacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<Long, ByteBuffer> eldest) {
        return size() > maxCapacity;
    }
}
