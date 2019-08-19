package com.huawei.hwcloud.tarus.kvstore.example;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.hwcloud.tarus.kvstore.common.KVStoreRace;
import com.huawei.hwcloud.tarus.kvstore.common.Ref;
import com.huawei.hwcloud.tarus.kvstore.exception.KVSErrorCode;
import com.huawei.hwcloud.tarus.kvstore.exception.KVSException;

public class EngineKVStoreRaceExample implements KVStoreRace {
	
	private static final Logger log = LoggerFactory.getLogger(EngineKVStoreRaceExample.class);
	
	public final static int MAX_FILE_SIZE = (1 << (10 * 2 + 8)); //default size: 256MB per file
	
	private static final int COMMIT_TIMES = 500;
	
	private long writeTimes=0;
	
	private String dir;
	private int file_size;
	private int thread_num;
    
    private MetaDataManager metaDataManager = new MetaDataManager();
    
    private DataManager dataManager = new DataManager();

	@Override
	public boolean init(final String dir, final int thread_num) throws KVSException {
		this.dir = dir;
		this.thread_num = thread_num;
		this.file_size = MAX_FILE_SIZE;
		Validate.isTrue(StringUtils.isNotEmpty(this.dir), "empty dir=[" + this.dir + "]");
		Validate.isTrue(this.file_size > 0, "file_size must large than zero!");
		try {
			dataManager.init(dir, this.file_size);
			metaDataManager.init(dir);
		} catch (IOException e) {
			throw new KVSException(KVSErrorCode.INIT_RACE_ERROR, KVSErrorCode.INIT_RACE_ERROR.getDescription(), e);
		}
	    return true;
	}

	@Override
	public long set(final String key, final byte[] val) throws KVSException {
		long pos = -1;
		try {
			pos = dataManager.append(key, val);
			metaDataManager.set(key, pos);
			writeTimes++;
			if(writeTimes%COMMIT_TIMES == 0){
	    		flush();
	    	}
			return (pos >> 32);
		} catch (IOException e) {
			throw new KVSException(KVSErrorCode.SET_RACE_ERROR, KVSErrorCode.SET_RACE_ERROR.getDescription(), e);
		}
	}

	@Override
	public long get(final String key, final Ref<byte[]> val) throws KVSException {
		
		try {
			long pos = metaDataManager.get(key);
			final Ref<String> key_ref = Ref.of(String.class);
			key_ref.setValue(key);
			dataManager.get(pos, key_ref, val);
			return (pos >> 32);
		} catch (IOException e) {
			throw new KVSException(KVSErrorCode.GET_RACE_ERROR, KVSErrorCode.GET_RACE_ERROR.getDescription(), e);
		}
	}

	@Override
	public void close() {
		dataManager.close();
		metaDataManager.close();
	}

	@Override
	public void flush() {
		dataManager.flush();
		metaDataManager.flush();
	}
}
