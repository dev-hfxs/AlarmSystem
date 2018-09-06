/**
* <p>版权所有:(C)2018-2022 天津航峰希萨科技有限公司 </p>
* @创建人: lwm
* @创建日期: 2018年7月26日
* @修改人: 
* @修改日期：
* @描述: 
 */
package com.sierotech.alarmsys.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sierotech.alarmsys.common.Constants;
import com.sierotech.alarmsys.common.utils.ConfigFactory;
import com.sierotech.alarmsys.common.utils.JedisUtil;
import com.sierotech.alarmsys.common.utils.JsonUtil;
import com.sierotech.alarmsys.monitor.ProcessorMonitor;

import redis.clients.jedis.Jedis;

/**
* @JDK版本: 1.7
* @创建人: lwm
* @创建日期：2018年7月26日
* @功能描述: 系统相关上下文信息处理
 */
public class ProcessContext {
	static final Logger log = LoggerFactory.getLogger(ProcessContext.class);
	
	private static Map<String, String> stringCacheMap = new HashMap<String, String>();
	
	private static List<String> alarmMessageList = new ArrayList<String>();
	
	private static List<String> processorList = new ArrayList<String>();
	
	private static String  lastReadAlarmNewDate = "";
	
	private static List<Map<String, Object>> hadReadAlarms = new ArrayList<Map<String, Object>>();
	
	private static Map<String, ProcessorMonitor> processorMonitorMap = new HashMap<String, ProcessorMonitor>();
	
	private static Map<String, Map<String,Object>> cameraDeployMaps = new HashMap<String, Map<String,Object>>();
	
	public static void clearBaseDataCache() {
		stringCacheMap.clear();
		processorList.clear();
		cameraDeployMaps.clear();
	}
	
	public static boolean initRedis() {
		String redisHost = ConfigFactory.getPropertyConfig("alarm.properties").getString("redisHost");
		String redisPort = ConfigFactory.getPropertyConfig("alarm.properties").getString("redisPort");
		int port = 6379;
		try {
			port = Integer.valueOf(redisPort);
		}catch(Exception e) {
			log.info(" Redis:{} 端口错误", redisPort);
			return false;
		}
		JedisUtil.poolInit(redisHost, port);
		Jedis jedis = JedisUtil.getJedis();
		if(jedis == null) {
			log.info("未能连接Redis[host{}:port{}].", redisHost, redisPort);
			return false;
		}
		
		return true;
	}
	
	public static boolean isInitBaseData() {
		Jedis jedis = JedisUtil.getJedis();
		if(jedis == null) {
			return false;
		}
		boolean result = false;
		String initBaseDataStatus = jedis.get("isInitBaseData");
		if("Y".equals(initBaseDataStatus)) {
			return true;
		}
		JedisUtil.releaseJedis(jedis);
		return result;
	}
	
	public static void registerStringValue (String key, String value){
		Jedis jedis = JedisUtil.getJedis();
		if(jedis == null) {
			stringCacheMap.put(key, value);
		}else {
			jedis.set(key, value);
			JedisUtil.releaseJedis(jedis);
		}		
	}
	
	public static String getStringValue (String key){
		Jedis jedis = JedisUtil.getJedis();
		String value = null;
		if(jedis == null) {
			value = stringCacheMap.get(key);	
		}else {
			value = jedis.get(key);
			JedisUtil.releaseJedis(jedis);
		}
		return value;
	}
	
	public static void registerProcessorNum (int processorNum){
		Jedis jedis = JedisUtil.getJedis();
		if(jedis == null) {
			stringCacheMap.put("processorNum", "" + processorNum);
		}else {
			jedis.set("processorNum", "" + processorNum);
			JedisUtil.releaseJedis(jedis);
		}
	}
	
	public static void registerDetectorNum (long detectorNum){
		Jedis jedis = JedisUtil.getJedis();
		if(jedis == null) {
			stringCacheMap.put("detectorNum", "" + detectorNum);
		}else {
			jedis.set("detectorNum", "" + detectorNum);
			JedisUtil.releaseJedis(jedis);
		}		
	}
	
	public static void registerAlarmMessage (Map<String,String> msg){
		Jedis jedis = JedisUtil.getJedis();
		String jsonMsg = JsonUtil.mapToJson(msg);
		
		if(jedis == null) {
			alarmMessageList.add(jsonMsg);
		}else {
			jedis.rpush(Constants.ALARM_MESSAGE_QUEUE_NAME, jsonMsg);
			JedisUtil.releaseJedis(jedis);
		}		
	}
	
	public static void registerProcessor (Map<String,String> dataObj){
		//		Jedis jedis = JedisUtil.getJedis();
				String jsonObj = JsonUtil.mapToJson(dataObj);
		//		
		//		if(jedis == null) {
					processorList.add(jsonObj);
		//		}else {
		//			jedis.lpush("allProcessors", jsonObj);
		//			JedisUtil.releaseJedis(jedis);
		//		}		
	}
	
	public static List<String> getAllProcessor (){
		//		Jedis jedis = JedisUtil.getJedis();
		//		
				List<String>  result = null;
		//		if(jedis == null) {
					result = processorList;
		//		}else {
		//			result = jedis.lrange("allProcessors", 0, jedis.llen("allProcessors"));
		//			JedisUtil.releaseJedis(jedis);
		//		}
		return result;
	}
	
	public static String popAlarmMessage (){
		String msg = null;
		Jedis jedis = JedisUtil.getJedis();
		if(jedis == null) {
			if(alarmMessageList.size() > 0 ) {
				msg = alarmMessageList.remove(0);
			}
		}else {
			if(jedis.llen(Constants.ALARM_MESSAGE_QUEUE_NAME) > 0) {
				msg = jedis.lpop(Constants.ALARM_MESSAGE_QUEUE_NAME);
			}
			JedisUtil.releaseJedis(jedis);
		}
		return msg;
	}
	
	public static void registerHadReadAlarmMsg (List<Map<String, Object>> alarms){		
		hadReadAlarms.addAll(hadReadAlarms.size(), alarms);
	}
	
	public static List<Map<String, Object>> getHadReadAlarmMsg (){
		return hadReadAlarms;
	}
	
	public static void updateHadReadAlarmMsg (String alarmId, String status){
		for(Map<String,Object> alarm : hadReadAlarms) {
			if(alarmId.equals(alarm.get("id"))) {
				alarm.put("status", status);
				break;
			}
		}
	}
	
	public static void removeHadReadAlarmMsg (String alarmId){
		//for(Map<String,Object> alarm : hadReadAlarms) {
		for(int i=0; i< hadReadAlarms.size();i++) {
			Map<String,Object> alarm = hadReadAlarms.get(i);
			if(alarmId.equals(alarm.get("id"))) {
				hadReadAlarms.remove(i);
				break;
			}
		}
	}
	
	public static void registerLastReadAlarmDate (String alarmDate){
		Jedis jedis = JedisUtil.getJedis();
		if(jedis == null) {
			lastReadAlarmNewDate = alarmDate;		
		}else {
			jedis.set("lastReadAlarmNewDate", alarmDate);
			JedisUtil.releaseJedis(jedis);
		}
	}
	
	public static String getLastReadAlarmDate (){
		Jedis jedis = JedisUtil.getJedis();
		if(jedis == null) {
			return lastReadAlarmNewDate;
		}else {
			String alarmDate = jedis.get("lastReadAlarmNewDate");
			JedisUtil.releaseJedis(jedis);
			return alarmDate;
		}
	}
	
	public static void registerProcessorMonitor(String nfcNum, ProcessorMonitor pm) {
		processorMonitorMap.put(nfcNum, pm);
	}
	
	public static void unRegisterProcessorMonitor(String nfcNum) {
		 ProcessorMonitor pm = processorMonitorMap.remove(nfcNum);
		 pm.quit();
	}
	
	public static void registerCameraDeploy(String nfcNumber, Map<String, Object> cameraDeploy) {
		if(nfcNumber != null) {
			cameraDeployMaps.put(nfcNumber, cameraDeploy);
		}		
	}
	
	public static Map<String, Object> getCameraDeploy(String nfcNumber) {
		return cameraDeployMaps.get(nfcNumber);
	}
}
