package com.huawei.hwcloud.tarus.kvstore.common;

public enum CheckMode {
	
	TEST("test", "com.huawei.hwcloud.tarus.kvstore.test.EngineKVStoreTester"),
	
	;
    
	 private final String modeName;
	 private final String modeClazz;
	 
	 private CheckMode(final String modeName, final String modeClazz){
	   this.modeName = modeName;
	   this.modeClazz = modeClazz;
	 }
	 
	 public final String getModeName(){
	   return this.modeName;
	 }
	 
	 public final String getModeClazz(){
	   return this.modeClazz;
	 }
}
