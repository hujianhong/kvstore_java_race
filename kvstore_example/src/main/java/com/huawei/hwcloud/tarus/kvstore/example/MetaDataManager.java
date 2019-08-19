package com.huawei.hwcloud.tarus.kvstore.example;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.hwcloud.tarus.kvstore.common.Ref;

public class MetaDataManager {
	
	private static final Logger log = LoggerFactory.getLogger(MetaDataManager.class);
	
	private String path;
	
	private final DataFile dataFile = new DataFile();
	
	private final Map<String, Long> keys_map = new HashMap<>();
	
	public final boolean init(final String dir) throws IOException{
		
		log.info("init meta data file=[{}]", dir);
		
		keys_map.clear();

		path = DataFile.buildPath(dir, "kv_store.key");

		dataFile.init(path);

	    restoreMeta();
	    return true;
	}

	public final void set(final String key, final long pos) throws IOException{
		keys_map.put(key, pos);
		dataFile.append(key, String.valueOf(pos).getBytes());
//		dataFile.flush();
	}

	public final long get(final String key){
		if(keys_map.containsKey(key)){
			return keys_map.get(key);
		}else{
			return 0;
		}
	}

	public final int restoreMeta() throws NumberFormatException, IOException{
		
		final Ref<String> key = Ref.of(String.class);
		final Ref<byte[]> val = Ref.of(byte[].class);
	    int pos = 0;
	    final int int_size = Integer.BYTES;
	    int readSize=0;
	    
	    while((readSize=dataFile.readKV(pos, key, val)) > int_size) {
	    	keys_map.put(key.getValue(), Long.parseLong(new String(val.getValue())));
	        pos += readSize;
	    }
	    return keys_map.size();
	}
	
	public final void close(){
		dataFile.close();
	}

	public void flush() {
		dataFile.flush();
	}
}
