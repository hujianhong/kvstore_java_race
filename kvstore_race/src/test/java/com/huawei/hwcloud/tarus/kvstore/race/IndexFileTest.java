package com.huawei.hwcloud.tarus.kvstore.race;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@Ignore
public class IndexFileTest {

    @Test
    public void test_put_and_get() throws IOException {
        String filePath = "/tmp/indexfile.data";
        Tools.deleteFile(filePath);
        long start = 100000;
        long end = start * 4;

        DeltaLongIntMap deltaLongIntMap = new DeltaLongIntMap(Constants.TOTAL_SIZE);
        IndexFile indexFile = new IndexFile(filePath, deltaLongIntMap);
        for (long i = start; i < end; i++) {
            indexFile.writeLong(i);
        }
        indexFile.readReady();
        for (long i = start; i < end; i++) {
            long result = indexFile.readLong();
            assertEquals(i, result);
        }
    }


    @Test
    public void test_put_and_get_when_reopen() throws IOException {
        String filePath = "/tmp/indexfile.data";
        Tools.deleteFile(filePath);
        long start = 100000;
        long end = start * 4;
        DeltaLongIntMap deltaLongIntMap = new DeltaLongIntMap(Constants.TOTAL_SIZE);
        IndexFile indexFile = new IndexFile(filePath, deltaLongIntMap);
        for (long i = start; i < end; i++) {
            indexFile.writeLong(i);
        }
        indexFile.readReady();
        int count = 0;
        for (long i = start; i < end; i++) {
//            System.out.println(++ count);
            long result = indexFile.readLong();
            assertEquals(result, i);
        }
        indexFile.close();


        indexFile = new IndexFile(filePath, deltaLongIntMap);

        assertEquals((int) (end - start), indexFile.getCount());
    }
}
