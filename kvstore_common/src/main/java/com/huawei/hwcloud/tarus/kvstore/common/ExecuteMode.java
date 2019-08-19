package com.huawei.hwcloud.tarus.kvstore.common;

public enum ExecuteMode {
	
	EXAMPLE("example", "com.huawei.hwcloud.tarus.kvstore.example.EngineKVStoreRaceExample"),
	RACE("race", "com.huawei.hwcloud.tarus.kvstore.race.EngineKVStoreRace"),
	
	;
    
    private final String modeName;
    private final String modeClazz;
        
    private ExecuteMode(final String modeName, final String modeClazz){
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
