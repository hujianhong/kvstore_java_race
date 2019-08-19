package com.huawei.hwcloud.tarus.kvstore.common;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.huawei.hwcloud.tarus.kvstore.exception.KVSErrorCode;
import com.huawei.hwcloud.tarus.kvstore.exception.KVSException;

public class ResourceManager {
	
	private static final Properties config = new Properties();
	
	private static String home="";
	
	private static int threadNum=0;
	
	private static int kvNumPerThread=0;
	
	private static String executeMode= "";
	
	private static String checkModeParam= "";
	
	private static CheckMode checkMode = null;
	
	public static final void init(String[] params) {
		initConfig(params);
		parseParams(params);
		validateParams();
		loadCheckMode();
		loadExecuteMode();		
	}

	private static final void initConfig(String[] params) {

		Configuration[] configurations = Configuration.values();
		for(int i=0; i<configurations.length; i++){
			Configuration configuration= configurations[i];
			String value= ConfigManager.getConfigByDefalt(configuration);
			config.put(configuration.getParamName(), value);
		}
		
		System.out.println("system config=[" + config + "]");
	}
	
	private static final void parseParams(String[] params) {
		parseHome(params);
		parseThreadNum();
		parseKVNumPerThread();
		parseExecuteMode();
		parseCheckMode();
	}
	
	private static final void validateParams() {
		if (StringUtils.isEmpty(executeMode) && StringUtils.isEmpty(checkModeParam)) {
			throw new KVSException(KVSErrorCode.CONFIG_PARAM_ERROR, 
					String.format("param:[%s && %s] are both null!", 
							Configuration.KVS_CONFIG__KVS_EXECUTE_MODE.getParamName(), 
							Configuration.KVS_CONFIG__KVS_CHECK_MODE.getParamName()));
		}
	}

	private static final void parseHome(String[] params) {
		
		String tmpKVHome = System.getProperty(Configuration.KVS_CONFIG_HOME.getParamName());
		
		String userdir = config.getProperty(Configuration.KVS_CONFIG_USER_DIR.getParamName());
		
		if (StringUtils.isEmpty(tmpKVHome)) {
			tmpKVHome = userdir;
			File userdirfile = new File(userdir);
			if (userdir.lastIndexOf("/bin/") != -1
					|| userdir.lastIndexOf("/bin") != -1) {
				userdirfile = userdirfile.getParentFile();
				tmpKVHome = userdirfile.getAbsolutePath();
			}
		} else {
			try {
				tmpKVHome = new File(tmpKVHome).getCanonicalPath();
			} catch (IOException e) {
				throw new KVSException(KVSErrorCode.CONFIG_PARAM_ERROR, 
						printParamNull(Configuration.KVS_CONFIG_USER_DIR),
						e);
			}
		}
		home= tmpKVHome;
		System.out.println("Home=[" + home + "]");
	}
	
	private static final String printParamNull(Configuration configuration){
		return String.format("param:[%s] is null!", configuration.getParamName());
	}
	

	private static final void parseThreadNum() {
		String threadNumStr = config.getProperty(Configuration.KVS_CONFIG__KVS_THREAD_NUM.getParamName());
		if (StringUtils.isNotEmpty(threadNumStr)) {
			threadNum= Integer.parseInt(threadNumStr);
		}
	}
	
	private static final void parseKVNumPerThread() {
		String kvNumPerThreadStr = config.getProperty(Configuration.KVS_CONFIG__KVS_PER_THREAD_KV_NUM.getParamName());
		if (StringUtils.isNotEmpty(kvNumPerThreadStr)) {
			kvNumPerThread= Integer.parseInt(kvNumPerThreadStr);
		}
	}
	
	private static final void parseExecuteMode() {
		executeMode = config.getProperty(Configuration.KVS_CONFIG__KVS_EXECUTE_MODE.getParamName());
	}
	
	private static final void parseCheckMode() {
		checkModeParam = config.getProperty(Configuration.KVS_CONFIG__KVS_CHECK_MODE.getParamName());
	}
	
	/**
	 * get param
	 */
	
	public static final String getHome() {
		return home;
	}

	public static final int getThreadNum() {
		return threadNum;
	}

	public static final int getKvNumPerThread() {
		return kvNumPerThread;
	}

	public static final String getExecuteMode() {
		return executeMode;
	}

	public static final String getCheckModeParam() {
		return checkModeParam;
	}
	
	public static final CheckMode getCheckMode() {
		return checkMode;
	}
	
	private static final void loadExecuteMode() {
		if (StringUtils.isNotEmpty(getExecuteMode())) {
			
			try {
				ExecuteMode mode = ExecuteMode.valueOf(getExecuteMode().toUpperCase());
				KVStoreRace race = (KVStoreRace) Class.forName(mode.getModeClazz()).newInstance();
				RaceManager.instance().registerRacer(race);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				throw new KVSException(KVSErrorCode.UNSUPPORTED_MODE_ERROR, 
						String.format("unsupported execute mode:[%s] for racer!", getExecuteMode()));
			}
		}
	}

	private static final void loadCheckMode() {
		if (StringUtils.isNotEmpty(getCheckModeParam())) {
			
			try {
				checkMode = CheckMode.valueOf(getCheckModeParam().toUpperCase());
				KVStoreCheck check = (KVStoreCheck) Class.forName(getCheckMode().getModeClazz()).newInstance();
				RaceManager.instance().registerChecker(check);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				throw new KVSException(KVSErrorCode.UNSUPPORTED_MODE_ERROR, 
						String.format("unsupported check mode:[%s] for racer!", getCheckMode()));
			}
		}		
	}
	
	public static final KVStoreRace initExecute() {
		KVStoreRace race = null;
		if (StringUtils.isNotEmpty(getExecuteMode())) {
			
			try {
				ExecuteMode mode = ExecuteMode.valueOf(getExecuteMode().toUpperCase());
				race = (KVStoreRace) Class.forName(mode.getModeClazz()).newInstance();				
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				throw new KVSException(KVSErrorCode.UNSUPPORTED_MODE_ERROR, 
						String.format("unsupported execute mode:[%s] for racer!", getExecuteMode()));
			}
		}else{
			throw new KVSException(KVSErrorCode.CONFIG_PARAM_ERROR, 
					String.format("execute mode:[%s] is null!", getExecuteMode()));
		}
		Validate.notNull(race, "failed to init race=[" + getExecuteMode() + "] init!");
		return race;
	}
	
	public static final String buildFullDir(final String dir){
		return new StringBuilder().append(getHome()).append(File.separator).append(dir).toString();
	}
}
