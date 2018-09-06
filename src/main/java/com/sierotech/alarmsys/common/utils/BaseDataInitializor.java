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
			//清除缓存
			ProcessContext.clearBaseDataCache();
			
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
				ProcessContext.registerProcessorNum(alProcessors.size());
				
				for(Map<String, Object> processor : alProcessors) {
					String processorId = processor.get("id").toString();
					String ip = processor.get("ip").toString();
					String nfcNumber = processor.get("nfc_number").toString();
					String longitude = processor.get("longitude").toString();
					String latitude = processor.get("latitude").toString();
					String posDesc = processor.get("pos_desc").toString();
					Map<String,String> posMap = new HashMap<String, String>();
					posMap.put("longitude", longitude);
					posMap.put("latitude", latitude);
					posMap.put("pos_desc", posDesc);
					String posInfo = JsonUtil.mapToJson(posMap);
					
					Map<String,String> processorMap = new HashMap<String, String>();
					processorMap.put("id", processorId);
					processorMap.put("ip", ip);
					processorMap.put("nfc_number", nfcNumber);
					processorMap.put("longitude", longitude);
					processorMap.put("latitude", latitude);
					processorMap.put("pos_desc", posDesc);
					processorMap.put("pos_info", posInfo);
					processorMap.put("detector_num", "0");
					
					//String keyPos =  "pos_" + nfcNumber ;
					//ProcessContext.registerStringValue(keyPos, posInfo);
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
							String dNfcNumber = detector.get("nfc_number").toString();
							String detectorSeq = detector.get("detector_seq").toString();
							
							//String orderNum = detector.get("order_num").toString();
							int orderNum = i + 1;
							String dLongitude = detector.get("longitude").toString();
							String dLatitude = detector.get("latitude").toString();
							String dPosDesc = detector.get("pos_desc").toString();
							String dKeyNfc =  "nfc_" + nfcNumber + "_" + orderNum;
							String dKeyPos =  "pos_" + nfcNumber + "_" + orderNum;
							String dKeySeq =  "seq_" + nfcNumber + "_" + orderNum;
							
							
							Map<String,String> dPosMap = new HashMap<String, String>();
							dPosMap.put("longitude", dLongitude);
							dPosMap.put("latitude", dLatitude);
							dPosMap.put("pos_desc", dPosDesc);
							String dPosInfo = JsonUtil.mapToJson(dPosMap);
							
							ProcessContext.registerStringValue(dKeyNfc, dNfcNumber);
							ProcessContext.registerStringValue(dKeyPos, dPosInfo);
							ProcessContext.registerStringValue(dKeySeq, detectorSeq);						
						}
						processorMap.put("detector_num", "" + alDetectors.size());
						alDetectors = null;
					}
					//将处理器放到缓存中
					ProcessContext.registerProcessor(processorMap);
				}
				log.info("初始化缓存，完成处理器、探测器的加载.");
				ProcessContext.registerDetectorNum(detectors);
				alProcessors = null;
				//ProcessContext.registerStringValue("isInitBaseData", "Y");
			}
			
			//初始化摄像头与设备关联关系 
			String getCameraDeploySql = ConfigSQLUtil.getCacheSql("alarm-camera-getAllCameraDeploy");
			//摄像头与设备关联
			List<Map<String, Object>> alCameraDeploys = null;
			try {
				alCameraDeploys = springJdbcDao.queryForList(getCameraDeploySql);
			} catch (DataAccessException dae) {
				log.info("初始化缓存，获取设备与摄像头关联信息出错.");
			}
			if(alCameraDeploys != null && alCameraDeploys.size() >0) {
				for(Map<String, Object> cameraDeploy : alCameraDeploys) {
					if(cameraDeploy.get("nfc_number") !=null ) {
						ProcessContext.registerCameraDeploy(cameraDeploy.get("nfc_number").toString(), cameraDeploy);
					}
				}
			}
	}
	
}
