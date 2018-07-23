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
import com.sierotech.alarmsys.common.utils.LogOperationUtil;
import com.sierotech.alarmsys.common.utils.UUIDGenerator;
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
		LogOperationUtil.logAdminOperation(adminUser, "机箱维护", "删除机箱:[" + processorObj.get("nfc_number").toString() + "].");
	}
}
