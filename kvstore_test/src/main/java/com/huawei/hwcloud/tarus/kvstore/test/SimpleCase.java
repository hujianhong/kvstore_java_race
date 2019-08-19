package com.huawei.hwcloud.tarus.kvstore.test;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.hwcloud.tarus.kvstore.common.KVStoreRace;
import com.huawei.hwcloud.tarus.kvstore.common.RaceManager;
import com.huawei.hwcloud.tarus.kvstore.common.Ref;
import com.huawei.hwcloud.tarus.kvstore.util.BufferUtil;

public class SimpleCase {

	private static final Logger log = LoggerFactory.getLogger(EngineKVStoreTester.class);
	
	private KVStoreRace racer;
	
	public boolean init() {
		racer = RaceManager.instance().getRacer();
		return false;
	}

	public double test(final String dir, final int times,  final Ref<Integer> err) {

		racer.init(dir, 0);

	    double write_time = write(times, err);

	    racer.close();
	    racer.init(dir, 0);
	    
	    double read_time = read(times, err);
	    racer.close();
	    return write_time + read_time;
	}
	
	public void uninit() {
		if(racer != null){
			racer.close();
			racer = null;
		}
	}

	public double write(final int times, final Ref<Integer> err) {
		
		long begin = System.currentTimeMillis();
	    
	    for (int i = 0; i < times; i ++) {
	        String key = buildKey(i+1);
	        String val = buildVal(i+1);
	        racer.set(key, BufferUtil.stringToBytes(val));
	    }

	    for (int i = 0; i < times; i ++) {
	        String key = buildKey(i+1);
	        String val = buildVal(i+1);
	        Ref<byte[]> val_ref = Ref.of(byte[].class);
	        racer.get(key, val_ref);
	        
	        if (!(Arrays.equals(val.getBytes(), val_ref.getValue())) ) {
	            err.setValue(err.getValue() + 1);
	            log.error("get key=[{}] error, val size=[{}]", i, val.length());
	            break;
	        }
	    }
	    
	    long end = System.currentTimeMillis();
	    
	    return (end - begin);
	}
	
	public double read(final int times, final Ref<Integer> err) {
		long begin = System.currentTimeMillis();
		
	    for (int i = 0; i < times; i ++) {
	    	String key = buildKey(i+1);
	        String val = buildVal(i+1);
	        Ref<byte[]> val_ref = Ref.of(byte[].class);
	        racer.get(key, val_ref);
	        if (!(Arrays.equals(val.getBytes(), val_ref.getValue())) ) {
	            err.setValue(err.getValue() + 1);
	            log.error("get key=[{}] error, val size=[{}]", i, val.length());
	            break;
	        }
	    }

	    long end = System.currentTimeMillis();
	    
	    return (end - begin);
	}
	
	private final String buildKey(final int i) {
		return String.format("t%d", i);
	}
	
	private final String buildVal(final int i) {
		return String.format("hello_%d", i);
	}
}
