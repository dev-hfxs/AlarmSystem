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

import com.sierotech.alarmsys.context.ProcessContext;
import com.sierotech.alarmsys.monitor.ProcessorMonitor;





/**
 * @author liweiming
 *
 */
@Component
public class ProcessorMonitorInitializor{
	static final Logger log = LoggerFactory.getLogger(ProcessorMonitorInitializor.class);
	@Autowired
	private JdbcTemplate springJdbcDao;
	
	public  void run() {
		int processorPort = 4001;
		
		List<String> allProcessor = ProcessContext.getAllProcessor();
		if(allProcessor == null || allProcessor.size() < 1) {
			//从数据库中获取数据库连接
			String getProcessorSql = ConfigSQLUtil.getCacheSql("alarm-processor-getAll");
			String getDetectorNumPreSql = ConfigSQLUtil.getCacheSql("alarm-detector-getDetectorNumByProcessorId");
			String addProcessorLogPreSql = ConfigSQLUtil.getCacheSql("alarm-processor-addProcessorLog");
			String updateProcessorOnlinePreSql = ConfigSQLUtil.getCacheSql("alarm-processor-updateProcessorOnline");
			
			List<Map<String, Object>> processorList = null;
			Map<String,String> params = new HashMap<String, String>();
			
			try {
				processorList = springJdbcDao.queryForList(getProcessorSql);
			} catch (DataAccessException dae) {
				log.info("获取处理器信息出错.");
			}
			if(processorList != null && processorList.size() > 0) {
				//
				for(Map<String, Object> processor : processorList) {
					String processorId = processor.get("id").toString();
					String nfcNumber = processor.get("nfc_number").toString();
					String longitude = processor.get("longitude").toString();
					String latitude = processor.get("latitude").toString();
					String ip = processor.get("ip").toString();
					String posDesc = processor.get("pos_desc").toString();
					Map<String,String> posMap = new HashMap<String, String>();
					posMap.put("longitude", longitude);
					posMap.put("latitude", latitude);
					posMap.put("posDesc", posDesc);
					
					//String posInfo = "["+ longitude + "," + latitude + "]-" + posDesc;
					String posInfo = JsonUtil.mapToJson(posMap);
					
					//获取探测器数
					params.clear();
					params.put("processorId", processorId);
					String getDetectorNumSql =  ConfigSQLUtil.preProcessSQL(getDetectorNumPreSql, params);
					int detectorNum = 0;
					Map<String, Object> countMap = null; 
					try {
						countMap = springJdbcDao.queryForMap(getDetectorNumSql);
					} catch (DataAccessException dae) {
						log.info("获取处理器探测器信息出错.");
					}
					if(countMap != null) {
						String countNum = countMap.get("countNum").toString();
						try {
							detectorNum = Integer.valueOf(countNum);
						}catch(Exception e) {
							//
						}
					}
					params.clear();
					params.put("id", UUIDGenerator.getUUID());
					params.put("processorId", processorId);
					params.put("operationDate", DateUtils.getNow(DateUtils.FORMAT_LONG));
					
					if(detectorNum < 1) {
						//没有处理器，不启动, 设置处理器状态为启动失败
						params.put("startStatus", "0");
						params.put("errorInfo", "处理器未维护探测器,未启动.");	
					}else {
						ProcessorMonitor pm = new ProcessorMonitor(ip, processorPort, detectorNum, posInfo, nfcNumber,processorId);
						if(pm.connect()) {
							// 连接上处理器, 设置处理器状态为启动成功, 启动监听
							Thread tPM = new Thread(pm);
							tPM.start();
							
							// 设置处理器在线状态
							Map<String,String> paramsMap = new HashMap<String,String>();
							paramsMap.clear();
							paramsMap.put("processorId", processorId);
							paramsMap.put("online", "Y");
							
							String updateProcessorOnlineSql =  ConfigSQLUtil.preProcessSQL(updateProcessorOnlinePreSql, paramsMap);
							try {
								springJdbcDao.update(updateProcessorOnlineSql);
							} catch (DataAccessException dae) {
								//
							}
							
							ProcessContext.registerProcessorMonitor(nfcNumber, pm);
							
							params.put("startStatus", "1");
							params.put("errorInfo", "");
						}else {
							// 设置处理器不在线状态
							Map<String,String> paramsMap = new HashMap<String,String>();
							paramsMap.clear();
							paramsMap.put("processorId", processorId);
							paramsMap.put("online", "N");
							
							String updateProcessorOnlineSql =  ConfigSQLUtil.preProcessSQL(updateProcessorOnlinePreSql, paramsMap);
							try {
								springJdbcDao.update(updateProcessorOnlineSql);
							} catch (DataAccessException dae) {
								//
							}
							
							//不能连接探测器, 设置处理器状态为启动失败
							params.put("startStatus", "2");
							params.put("errorInfo", "不能连接处理器, 未启动.");
						}
					}
					String addProcessorLogSql =  ConfigSQLUtil.preProcessSQL(addProcessorLogPreSql, params);
					try {
						// 记录处理器启动日志
						springJdbcDao.update(addProcessorLogSql);
					} catch (DataAccessException dae) {
						//
					}
				}
			}			
		}else {
			Map<String,String> params = new HashMap<String, String>();
			String getDetectorNumPreSql = ConfigSQLUtil.getCacheSql("alarm-detector-getDetectorNumByProcessorId");
			String addProcessorLogPreSql = ConfigSQLUtil.getCacheSql("alarm-processor-addProcessorLog");
			String updateProcessorOnlinePreSql = ConfigSQLUtil.getCacheSql("alarm-processor-updateProcessorOnline");
			
			for(String jsonObj : allProcessor) {
				Map<String, String> processorMap = JsonUtil.jsonToMap(jsonObj, false);
				String processorId = processorMap.get("id").toString();
				String nfcNumber = processorMap.get("nfc_number").toString();
				String longitude = processorMap.get("longitude").toString();
				String latitude = processorMap.get("latitude").toString();
				String ip = processorMap.get("ip").toString();
				String posDesc = processorMap.get("pos_desc").toString();
				Map<String,String> posMap = new HashMap<String, String>();
				posMap.put("longitude", longitude);
				posMap.put("latitude", latitude);
				posMap.put("posDesc", posDesc);
				
//				String posInfo = "["+ longitude + "," + latitude + "]-" + posDesc;
				String posInfo = JsonUtil.mapToJson(posMap);
				//获取探测器数
				params.clear();
				params.put("processorId", processorId);
				String getDetectorNumSql =  ConfigSQLUtil.preProcessSQL(getDetectorNumPreSql, params);
				int detectorNum = 0;
				Map<String, Object> countMap = null; 
				try {
					countMap = springJdbcDao.queryForMap(getDetectorNumSql);
				} catch (DataAccessException dae) {
					log.info("获取处理器探测器信息出错.");
				}
				if(countMap != null) {
					String countNum = countMap.get("countNum").toString();
					try {
						detectorNum = Integer.valueOf(countNum);
					}catch(Exception e) {
						//
					}
				}
				
				params.clear();
				params.put("id", UUIDGenerator.getUUID());
				params.put("processorId", processorId);
				params.put("operationDate", DateUtils.getNow(DateUtils.FORMAT_LONG));
				
				if(detectorNum < 1) {
					//没有处理器，不启动, 设置处理器状态为启动失败
					params.put("startStatus", "0");
					params.put("errorInfo", "处理器未维护探测器,未启动.");
				}else {
					ProcessorMonitor pm = new ProcessorMonitor(ip, processorPort, detectorNum, posInfo, nfcNumber, processorId);
					if(pm.connect()) {
						// 连接上探测器, 设置处理器状态为启动成功, 启动监听
						Thread tPM = new Thread(pm);
						tPM.start();
						// 设置处理器在线状态
						Map<String,String> paramsMap = new HashMap<String,String>();
						paramsMap.clear();
						paramsMap.put("processorId", processorId);
						paramsMap.put("online", "Y");		
						String updateProcessorOnlineSql =  ConfigSQLUtil.preProcessSQL(updateProcessorOnlinePreSql, paramsMap);
						try {
							springJdbcDao.update(updateProcessorOnlineSql);
						} catch (DataAccessException dae) {
							//
						}
						ProcessContext.registerProcessorMonitor(nfcNumber, pm);
						params.put("startStatus", "1");
						params.put("errorInfo", "");
					}else {
						// 设置处理器不在线状态
						Map<String,String> paramsMap = new HashMap<String,String>();
						paramsMap.clear();
						paramsMap.put("processorId", processorId);
						paramsMap.put("online", "N");		
						String updateProcessorOnlineSql =  ConfigSQLUtil.preProcessSQL(updateProcessorOnlinePreSql, paramsMap);
						try {
							springJdbcDao.update(updateProcessorOnlineSql);
						} catch (DataAccessException dae) {
							//
						}
						//不能连接探测器, 设置处理器状态为启动失败
						params.put("startStatus", "2");
						params.put("errorInfo", "不能连接处理器.");
					}
				}
				String addProcessorLogSql =  ConfigSQLUtil.preProcessSQL(addProcessorLogPreSql, params);
				try {
					// 记录处理器启动日志
					springJdbcDao.update(addProcessorLogSql);
				} catch (DataAccessException dae) {
					//
				}
			}
		}		
	}
	
}
