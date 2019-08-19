package com.huawei.hwcloud.tarus.kvstore.exception;

public enum KVSErrorCode {
    
    // comm error 100001~100100
    INTERNAL_SERVER_ERROR(100001, "Internal error"),
    UNSUPPORTED_MODE_ERROR(100002, "unsupported mode error"),
    
    // config error 100101~100200
    CONFIG_PARAM_ERROR(100101, "param error"),
    
    // race error 100201~100300
    INIT_RACE_ERROR(100201, "init race error"),
    SET_RACE_ERROR(100202, "set race error"),
    GET_RACE_ERROR(100203, "get race error"),
    
    // test error 100301~100400
   TEST_EXECUTE_ERROR(100301, "test execute error"),
   TEST_REMOVE_ERROR(100302, "test remove error"),
    
    ;

    private final int errorCode;
    private final String description;
    
    KVSErrorCode(final int errorCode) {
        this(errorCode, "");
    }
    
    KVSErrorCode(final int errorCode, final String description) {
        this.errorCode = errorCode;
        this.description = description;
    }
    
    public final int getErrorCode(){
    	return errorCode;
    }
    
    public final String getDescription(){
    	return description;
    }
}
