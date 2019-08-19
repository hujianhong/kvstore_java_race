package com.huawei.hwcloud.tarus.kvstore.race;

public class DeltaLongIntMap {

    private long firstValue;

    private IntIntMap underlying;

    public DeltaLongIntMap(int size) {
        this.firstValue = -1;
        this.underlying = new IntIntMap(size, 0.95f);
    }


    public int get(final long key) {
        if (this.firstValue == -1) {
            return 0;
        }
        long delta = key - this.firstValue + 1;
        if (delta > Integer.MAX_VALUE) {
            throw new IllegalStateException(key + " " + this.firstValue + " delta " + delta + " greater than int max value");
        }
        return this.underlying.get((int) delta);
    }

    public int put(final long key, final int value) {
        if (this.firstValue == -1) {
            this.firstValue = key;
        }
        long delta = key - this.firstValue + 1;
        if (delta > Integer.MAX_VALUE) {
            throw new IllegalStateException(key + " " + this.firstValue + " delta " + delta + " greater than int max value");
        }
        return this.underlying.put((int) delta, value);
    }

}
