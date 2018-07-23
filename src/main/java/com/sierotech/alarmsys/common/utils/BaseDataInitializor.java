package com.sierotech.alarmsys.common.utils;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.sierotech.alarmsys.common.BusinessException;

import redis.clients.jedis.Jedis;



/**
 * @author liweiming
 *
 */
@Component
public class BaseDataInitializor{
	static final Logger log = LoggerFactory.getLogger(BaseDataInitializor.class);
	@Autowired
	private JdbcTemplate springJdbcDao;
	
	public  void run() {
		String redisHost = ConfigFactory.getPropertyConfig("alarm.properties").getString("redisHost");
		String redisPort = ConfigFactory.getPropertyConfig("alarm.properties").getString("redisPort");
		int port = 6379;
		try {
			port = Integer.valueOf(redisPort);
		}catch(Exception e) {
			//			
		}
		JedisUtil.poolInit(redisHost, port);
		Jedis jedis = JedisUtil.getJedis();
		if(jedis == null) {
			log.info("缓存数据失败，未能连接Redis[host{}:port{}].", redisHost, redisPort);
			return;
		}
		String connStatus = jedis.ping();
		if("PONG".equals(connStatus)) {
			String isInitBaseData = jedis.get("isInitBaseData");
			if("Y".equals(isInitBaseData)) {
				//已经初始化基础数据,不再初始化
			}else {
				//初始化基础数据
				String getProcessorSql = ConfigSQLUtil.getCacheSql("alarm-cache-processor");
				String getDetectorPreSql = ConfigSQLUtil.getCacheSql("alarm-cache-detector");
				Map<String,String> params = new HashMap<String, String>();
				
				//处理器
				List<Map<String, Object>> alProcessors = null;
				try {
					alProcessors = springJdbcDao.queryForList(getProcessorSql);
				} catch (DataAccessException dae) {
					log.info("初始化缓存，获取处理器信息出错.");
				}
				if(alProcessors != null && alProcessors.size() >0) {
					long detectors = 0;
					jedis.set("processorNum", "" + alProcessors.size());
					
					for(Map<String, Object> processor : alProcessors) {
						String processorId = processor.get("id").toString();
						String nfcNumber = processor.get("nfc_number").toString();
						String longitude = processor.get("longitude").toString();
						String latitude = processor.get("latitude").toString();
						String posDesc = processor.get("pos_desc").toString();
						String posInfo = "["+ longitude + "," + latitude + "]-" + posDesc;
						jedis.set(nfcNumber, posInfo);
						
						//探测器
						List<Map<String, Object>> alDetectors = null;
						params.put("processorId", processorId);
						String getDetectorSql = ConfigSQLUtil.preProcessSQL(getDetectorPreSql, params);
						
						try {
							alDetectors = springJdbcDao.queryForList(getDetectorSql);
						} catch (DataAccessException dae) {
							log.info("初始化缓存，获取探测器信息出错.");
						}
						if(alDetectors != null && alDetectors.size() >0) {
							detectors = detectors + alDetectors.size();
							//for(Map<String, Object> detector : alDetectors) {
							for(int i=0; i< alDetectors.size(); i++) {
								Map<String, Object> detector  = alDetectors.get(i);
								String dProcessorId = detector.get("processor_id").toString();
								//String orderNum = detector.get("order_num").toString();
								int orderNum = i + 1;
								String dLongitude = detector.get("longitude").toString();
								String dLatitude = detector.get("latitude").toString();
								String dPosDesc = detector.get("pos_desc").toString();
								String dKey = dProcessorId + "_" + orderNum;
								String dPosInfo = "["+ dLongitude + "," + dLatitude + "]-" + dPosDesc;
								jedis.set(dKey, dPosInfo);
							}
							alDetectors = null;
						}
					}
					log.info("初始化缓存，完成处理器、探测器的加载.");
					jedis.set("detectorNum", "" + detectors);
					alProcessors = null;
					jedis.set("isInitBaseData", "Y");
				}
			}			
			JedisUtil.releaseJedis(jedis);
		}
	}
	
}
