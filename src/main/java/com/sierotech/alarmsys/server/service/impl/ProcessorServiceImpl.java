/**
* <p>版权所有:(C)2018-2022 天津航峰希萨科技有限公司 </p>
* @创建人: lwm
* @创建日期: 2018年7月22日
* @修改人: 
* @修改日期：
* @描述: 
 */
package com.sierotech.alarmsys.server.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.sierotech.alarmsys.common.BusinessException;
import com.sierotech.alarmsys.common.utils.ConfigSQLUtil;
import com.sierotech.alarmsys.common.utils.DateUtils;
import com.sierotech.alarmsys.common.utils.JsonUtil;
import com.sierotech.alarmsys.common.utils.LogOperationUtil;
import com.sierotech.alarmsys.common.utils.UUIDGenerator;
import com.sierotech.alarmsys.context.ProcessContext;
import com.sierotech.alarmsys.monitor.ProcessorMonitor;
import com.sierotech.alarmsys.server.service.IProcessorService;

/**
* @JDK版本: 1.7
* @创建人: lwm
* @创建日期：2018年7月22日
* @功能描述: 
 */

@Service
public class ProcessorServiceImpl implements IProcessorService{

	static final Logger log = LoggerFactory.getLogger(ProcessorServiceImpl.class);
	
	@Autowired	
	private JdbcTemplate springJdbcDao;
	
	@Override
	public boolean checkMoxaNumber(String processorId, String moxaNumber) throws BusinessException {
		boolean result = true;

		if (null == moxaNumber || "".equals(moxaNumber)) {
			throw new BusinessException("MOXA序列号不能为空!");
		}
		if (null == processorId) {
			return true;
		}		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("processorId", processorId);
		paramsMap.put("moxaNumber", moxaNumber);

		String preSql = ConfigSQLUtil.getCacheSql("alarm-processor-checkMoxaNumber");
		String sql = ConfigSQLUtil.preProcessSQL(preSql, paramsMap);
		try {
			Map<String, Object> recordMap = springJdbcDao.queryForMap(sql);
			if (recordMap != null) {
				int num = Integer.valueOf(recordMap.get("countNum").toString());
				if (num < 1) {
					result = false;
				}
			}
		} catch (DataAccessException ex) {
			log.info(ex.getMessage());
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return result;
	}

	@Override
	public boolean checkNfcNumber(String processorId, String nfcNumber) throws BusinessException {
		boolean result = true;

		if (null == nfcNumber || "".equals(nfcNumber)) {
			throw new BusinessException("NFC序列号不能为空!");
		}
		if (null == processorId) {
			return true;
		}		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("processorId", processorId);
		paramsMap.put("nfcNumber", nfcNumber);

		String preSql = ConfigSQLUtil.getCacheSql("alarm-processor-checkNfcNumber");
		String sql = ConfigSQLUtil.preProcessSQL(preSql, paramsMap);
		try {
			Map<String, Object> recordMap = springJdbcDao.queryForMap(sql);
			if (recordMap != null) {
				int num = Integer.valueOf(recordMap.get("countNum").toString());
				if (num < 1) {
					result = false;
				}
			}
		} catch (DataAccessException ex) {
			log.info(ex.getMessage());
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return result;
	}

	@Override
	public void addProcessor(String adminUser, Map<String, Object> processorObj) throws BusinessException {
		if (null == adminUser) {
			throw new BusinessException("添加处理器错误,当前操作是未知的管理员!");
		}

		if (null == processorObj.get("moxaNumber")) {
			throw new BusinessException("添加处理器错误, 缺少MOXA序列号!");
		}
		
		if (null == processorObj.get("nfcNumber")) {
			throw new BusinessException("添加处理器错误, 缺少NFC序列号!");
		}
		
		boolean moxaNumberExists = checkMoxaNumber("", processorObj.get("moxaNumber").toString());
		if (moxaNumberExists) {
			throw new BusinessException("处理器MOXA序列号已存在!");
		}
		boolean nfcNumberExists = checkNfcNumber("", processorObj.get("nfcNumber").toString());
		if (nfcNumberExists) {
			throw new BusinessException("处理器NFC序列号已存在!");
		}
		String processorId = UUIDGenerator.getUUID();
		processorObj.put("processorId", processorId);
		processorObj.put("createDate", DateUtils.getNow(DateUtils.FORMAT_LONG));
		String preSql = ConfigSQLUtil.getCacheSql("alarm-processor-addProcessor");
		String sql = ConfigSQLUtil.preProcessSQL(preSql, processorObj);
		try {
			springJdbcDao.update(sql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("添加处理器访问数据库异常.");
		}
		// 记录日志
		LogOperationUtil.logAdminOperation(adminUser, "处理器维护", "添加处理器:nfc-number[" + processorObj.get("nfcNumber").toString() + "].");
		
	}

	@Override
	public void updateProcessor(String adminUser, Map<String, Object> processorObj) throws BusinessException {
		if (null == adminUser) {
			throw new BusinessException("修改处理器错误, 当前操作是未知的管理员!");
		}
		
		if (null == processorObj.get("processorId")) {
			throw new BusinessException("修改处理器错误, 缺少处理器ID!");
		}
		
		if (null == processorObj.get("moxaNumber")) {
			throw new BusinessException("修改处理器错误, 缺少MOXA序列号!");
		}
		
		if (null == processorObj.get("nfcNumber")) {
			throw new BusinessException("修改处理器错误, 缺少NFC序列号!");
		}
		
		boolean moxaNumberExists = checkMoxaNumber(processorObj.get("processorId").toString(), processorObj.get("moxaNumber").toString());
		if (moxaNumberExists) {
			throw new BusinessException("处理器MOXA序列号已存在!");
		}
		boolean nfcNumberExists = checkNfcNumber(processorObj.get("processorId").toString(), processorObj.get("nfcNumber").toString());
		if (nfcNumberExists) {
			throw new BusinessException("处理器NFC序列号已存在!");
		}
		
		String preSql = ConfigSQLUtil.getCacheSql("alarm-processor-updateProcessor");
		String sql = ConfigSQLUtil.preProcessSQL(preSql, processorObj);
		try {
			springJdbcDao.update(sql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("修改处理器访问数据库异常.");
		}
		// 记录日志
		LogOperationUtil.logAdminOperation(adminUser, "处理器维护", "修改处理器:nfc-number[" + processorObj.get("nfcNumber").toString() + "].");
	}

	@Override
	public void deleteProcessor(String adminUser, String processorId) throws BusinessException {
		if (null == adminUser) {
			throw new BusinessException("删除处理器错误, 当前操作是未知的管理员!");
		}
		if (null == processorId) {
			throw new BusinessException("删除处理器错误, 缺少处理器ID!");
		}

		// 先获取处理器
		String preSelectSql = ConfigSQLUtil.getCacheSql("alarm-processor-getProcessorById");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("processorId", processorId);
		String selectSql = ConfigSQLUtil.preProcessSQL(preSelectSql, paramsMap);
		List<Map<String, Object>> alProcessors;
		try {
			alProcessors = springJdbcDao.queryForList(selectSql);
		} catch (DataAccessException dae) {
			throw new BusinessException("删除处理器错误, 获取处理器访问数据库异常.");
		}
		Map<String, Object> processorObj;
		if (alProcessors != null && alProcessors.size() > 0) {
			processorObj = alProcessors.get(0);
		} else {
			throw new BusinessException("删除处理器错误,未查询到处理器.");
		}

		// 检查处理器下面是否维护了探测器
		String checkProcessorHasDetectorPreSql = ConfigSQLUtil.getCacheSql("alarm-processor-checkProcessorHasDetector");
		String checkProcessorHasDetectorSql = ConfigSQLUtil.preProcessSQL(checkProcessorHasDetectorPreSql, paramsMap);
		int num = 0;
		try {
			Map<String, Object> recordMap = springJdbcDao.queryForMap(checkProcessorHasDetectorSql);
			if (recordMap != null) {
				num = Integer.valueOf(recordMap.get("countNum").toString());
			}
		} catch (DataAccessException ex) {
			log.info(ex.getMessage());
		} catch (Exception ex) {
			log.info(ex.getMessage());
		}
		if(num > 0) {
			throw new BusinessException("该处理器下面维护了探测器, 不能删除.");
		}
		String preUpdateSql = ConfigSQLUtil.getCacheSql("alarm-processor-deleteProcessor");
		paramsMap.clear();
		paramsMap.put("processorId", processorId);
		String updateSql = ConfigSQLUtil.preProcessSQL(preUpdateSql, paramsMap);
		try {
			springJdbcDao.update(updateSql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("删除处理器错误,访问数据库异常.");
		}
		// 记录日志
		LogOperationUtil.logAdminOperation(adminUser, "处理器维护", "删除处理器:[" + processorObj.get("nfc_number").toString() + "].");
	}

	
	@Override
	public void update4Start(String adminUser, String processorId) throws BusinessException {
		// 先获取处理器
		String preSelectSql = ConfigSQLUtil.getCacheSql("alarm-processor-getProcessorById");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("processorId", processorId);
		String selectSql = ConfigSQLUtil.preProcessSQL(preSelectSql, paramsMap);
		List<Map<String, Object>> alProcessors;
		try {
			alProcessors = springJdbcDao.queryForList(selectSql);
		} catch (DataAccessException dae) {
			throw new BusinessException("启动处理器错误, 获取处理器访问数据库异常.");
		}
		Map<String, Object> processorObj;
		if (alProcessors != null && alProcessors.size() > 0) {
			processorObj = alProcessors.get(0);
		} else {
			throw new BusinessException("启动处理器错误, 未查询到处理器.");
		}
		//获取探测器数
		String getDetectorNumPreSql = ConfigSQLUtil.getCacheSql("alarm-detector-getDetectorNumByProcessorId");
		paramsMap.clear();
		paramsMap.put("processorId", processorId);
		String getDetectorNumSql =  ConfigSQLUtil.preProcessSQL(getDetectorNumPreSql, paramsMap);
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
		if(detectorNum < 1) {
			throw new BusinessException("处理器下未维护探测器,启动失败.");
//			paramsMap.clear();
//			paramsMap.put("id", UUIDGenerator.getUUID());
//			paramsMap.put("processorId", processorId);
//			paramsMap.put("operationDate", DateUtils.getNow(DateUtils.FORMAT_LONG));
//			paramsMap.put("startStatus", "0");
//			paramsMap.put("errorInfo", "处理器未维护探测器，未启动.");
//			// 记录处理器启动日志
//			String addProcessorLogPreSql = ConfigSQLUtil.getCacheSql("alarm-processor-addProcessorLog");		
//			String addProcessorLogSql =  ConfigSQLUtil.preProcessSQL(addProcessorLogPreSql, paramsMap);
//			try {
//				springJdbcDao.update(addProcessorLogSql);
//			} catch (DataAccessException dae) {
//				//
//			}
		}else {
			String nfcNumber = processorObj.get("nfc_number").toString();
			String longitude = processorObj.get("longitude").toString();
			String latitude = processorObj.get("latitude").toString();
			String ip = processorObj.get("ip").toString();
			String posDesc = processorObj.get("pos_desc").toString();
			Map<String,String> posMap = new HashMap<String, String>();
			posMap.put("longitude", longitude);
			posMap.put("latitude", latitude);
			posMap.put("posDesc", posDesc);
			
			//String posInfo = "["+ longitude + "," + latitude + "]-" + posDesc;
			String posInfo = JsonUtil.mapToJson(posMap);		
			ProcessorMonitor pm = new ProcessorMonitor(ip, 4001, detectorNum, posInfo, nfcNumber, processorId);
			paramsMap.clear();
			paramsMap.put("id", UUIDGenerator.getUUID());
			paramsMap.put("processorId", processorId);
			paramsMap.put("operationDate", DateUtils.getNow(DateUtils.FORMAT_LONG));
			
			if(pm.connect()) {
				// 连接上探测器, 设置处理器状态为启动成功, 启动监听
				Thread tPM = new Thread(pm);
				tPM.start();
				// 设置处理器在线状态
				String updateProcessorOnlinePreSql = ConfigSQLUtil.getCacheSql("alarm-processor-updateProcessorOnline");
				Map<String, String> paramsMap2 = new HashMap<String, String>();
				paramsMap2.clear();
				paramsMap2.put("processorId", processorId);
				paramsMap2.put("online", "Y");		
				String updateProcessorOnlineSql =  ConfigSQLUtil.preProcessSQL(updateProcessorOnlinePreSql, paramsMap2);
				try {
					springJdbcDao.update(updateProcessorOnlineSql);
				} catch (DataAccessException dae) {
					//
				}
				ProcessContext.registerProcessorMonitor(nfcNumber, pm);
				paramsMap.put("startStatus", "1");
				paramsMap.put("errorInfo", "");
			}else {
				throw new BusinessException("不能连接处理器,未启动.");
				//不能连接探测器, 设置处理器状态为启动失败
				//paramsMap.put("startStatus", "2");
				//paramsMap.put("errorInfo", "不能连接处理器,未启动.");
			}
			// 记录处理器启动日志
			String addProcessorLogPreSql = ConfigSQLUtil.getCacheSql("alarm-processor-addProcessorLog");		
			String addProcessorLogSql =  ConfigSQLUtil.preProcessSQL(addProcessorLogPreSql, paramsMap);
			try {
				springJdbcDao.update(addProcessorLogSql);
			} catch (DataAccessException dae) {
				//
			}
			// 记录日志
			LogOperationUtil.logAdminOperation(adminUser, "处理器维护", "启动处理器监听:[" + processorObj.get("nfc_number").toString() + "].");
		}
	}

	@Override
	public void update4Stop(String adminUser, String processorId) throws BusinessException {
		// 先获取处理器
		String preSelectSql = ConfigSQLUtil.getCacheSql("alarm-processor-getProcessorById");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("processorId", processorId);
		String selectSql = ConfigSQLUtil.preProcessSQL(preSelectSql, paramsMap);
		List<Map<String, Object>> alProcessors;
		try {
			alProcessors = springJdbcDao.queryForList(selectSql);
		} catch (DataAccessException dae) {
			throw new BusinessException("停止处理器错误, 获取处理器访问数据库异常.");
		}
		Map<String, Object> processorObj;
		if (alProcessors != null && alProcessors.size() > 0) {
			processorObj = alProcessors.get(0);
		} else {
			throw new BusinessException("停止处理器错误,未查询到处理器.");
		}
		// 设置处理器在线状态
		String updateProcessorOnlinePreSql = ConfigSQLUtil.getCacheSql("alarm-processor-updateProcessorOnline");
		paramsMap.clear();
		paramsMap.put("processorId", processorId);
		paramsMap.put("online", "N");		
		String updateProcessorOnlineSql =  ConfigSQLUtil.preProcessSQL(updateProcessorOnlinePreSql, paramsMap);
		try {
			springJdbcDao.update(updateProcessorOnlineSql);
		} catch (DataAccessException dae) {
			//
		}
		ProcessContext.unRegisterProcessorMonitor(processorObj.get("nfc_number").toString());
				
		// 记录日志
		LogOperationUtil.logAdminOperation(adminUser, "处理器维护", "停止处理器监听:[" + processorObj.get("nfc_number").toString() + "].");
	}
}
