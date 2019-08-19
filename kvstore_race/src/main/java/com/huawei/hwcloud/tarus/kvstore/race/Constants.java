package com.huawei.hwcloud.tarus.kvstore.race;

import sun.misc.Unsafe;

public class Constants {

    public static final int KEY_SIZE = 8;

    public static final int VALUE_SIZE = 4 * 1024;

    /**
     * 1 << 17 = 128KB
     * 1 << 16 = 64KB
     */
    public static final int PAGE_SIZE_BIT = 17;


    public static final int PAGE_SIZE = 1 << PAGE_SIZE_BIT;


    public static final int PAGE_SIZE_MOD_MASK = PAGE_SIZE - 1;

    public static final int PAGE_SIZE_DIV_MASK = ~(PAGE_SIZE - 1);

    public static final int MAX_CACHE_SIZE = 1;

    public static final int TOTAL_SIZE = 4000000;

    public static final Unsafe UNSAFE = Tools.unsafe();
}
