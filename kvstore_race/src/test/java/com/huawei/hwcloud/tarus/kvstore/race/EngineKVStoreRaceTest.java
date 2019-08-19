package com.huawei.hwcloud.tarus.kvstore.race;

import com.huawei.hwcloud.tarus.kvstore.common.Ref;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@Ignore
public class EngineKVStoreRaceTest {


    private void cleanFile(String dir, int threadNum) {
        Tools.deleteFile(dir + "/" + threadNum + ".key");
        Tools.deleteFile(dir + "/" + threadNum + ".value");
        Tools.deleteFile(dir + "/" + threadNum + ".cache");

    }


    @Test
    public void test_engine_set_and_get_when_key_start_0() {
        String dir = "/tmp/huaweirace";
        int threadNum = 0;
        cleanFile(dir, threadNum);
        EngineKVStoreRace engineKVStoreRace = new EngineKVStoreRace();
        engineKVStoreRace.init(dir, threadNum);
        int count = 1;
        String[] keys = new String[count];
        byte[][] values = new byte[count][];
        for (int i = 0; i < count; i++) {
            keys[i] = String.valueOf(i);
            values[i] = RandomStringUtils.randomAlphabetic(4 * 1024).getBytes();
        }
        for (int i = 0; i < count; i++) {
            engineKVStoreRace.set(keys[i], values[i]);
        }
        engineKVStoreRace.close();

        engineKVStoreRace = new EngineKVStoreRace();
        engineKVStoreRace.init(dir, threadNum);
        Ref<byte[]> value = Ref.of(null);
        for (int i = 0; i < count; i++) {
            long code = engineKVStoreRace.get(keys[i], value);
            assertEquals(1, code);
            assertTrue(Arrays.equals(values[i], value.getValue()));
        }
    }


    @Test
    public void test_engine_set_and_get() {
        String dir = "/tmp/huaweirace";
        int threadNum = 0;
        cleanFile(dir, threadNum);
        EngineKVStoreRace engineKVStoreRace = new EngineKVStoreRace();
        engineKVStoreRace.init(dir, 0);
        int count = 1028;
        String[] keys = new String[count];
        byte[][] values = new byte[count][];
        for (int i = 0; i < count; i++) {
            keys[i] = String.valueOf(i + 100);
            values[i] = RandomStringUtils.randomAlphabetic(4 * 1024).getBytes();
        }
        for (int i = 0; i < count; i++) {
            engineKVStoreRace.set(keys[i], values[i]);
        }
        engineKVStoreRace.close();

        engineKVStoreRace = new EngineKVStoreRace();
        engineKVStoreRace.init(dir, 0);
        Ref<byte[]> value = Ref.of(null);
        for (int i = 0; i < 100; i++) {
            long code = engineKVStoreRace.get(keys[i], value);
            assertEquals(1, code);
            assertTrue(Arrays.equals(values[i], value.getValue()));
        }

        for (int i = 0; i < count; i++) {
            keys[i] = String.valueOf(i + 1000);
            values[i] = RandomStringUtils.randomAlphabetic(4 * 1024).getBytes();
        }
        for (int i = 0; i < count; i++) {
            engineKVStoreRace.set(keys[i], values[i]);
        }


        for (int i = 0; i < count; i++) {
            long code = engineKVStoreRace.get(keys[i], value);
            assertEquals(1, code);
            assertTrue(Arrays.equals(values[i], value.getValue()));
        }
    }

    @Test
    public void test_engine_set_and_get_from_cache_page() {
        EngineKVStoreRace engineKVStoreRace = new EngineKVStoreRace();
        String dir = "/tmp/huaweirace";
        int threadNum = 0;
        cleanFile(dir, threadNum);
        engineKVStoreRace.init(dir, threadNum);
        int count = 36;
        String[] keys = new String[count];
        byte[][] values = new byte[count][];
        for (int i = 0; i < count; i++) {
            keys[i] = String.valueOf(i + 100);
            values[i] = RandomStringUtils.randomAlphabetic(4 * 1024).getBytes();
        }
        for (int i = 0; i < count; i++) {
            engineKVStoreRace.set(keys[i], values[i]);
        }
        Ref<byte[]> value = Ref.of(null);
        for (int i = 0; i < count; i++) {
            long code = engineKVStoreRace.get(keys[i], value);
            assertEquals(1, code);
            assertTrue(Arrays.equals(values[i], value.getValue()));
        }
    }
}
