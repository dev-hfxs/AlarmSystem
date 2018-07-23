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
import com.sierotech.alarmsys.server.service.IDetectorService;

/**
* @JDK版本: 1.7
* @创建人: lwm
* @创建日期：2018年7月22日
* @功能描述: 
 */
@Service
public class DetectorServiceImpl implements IDetectorService {
	static final Logger log = LoggerFactory.getLogger(DetectorServiceImpl.class);
	
	@Autowired	
	private JdbcTemplate springJdbcDao;
	
	@Override
	public boolean checkNfcNumber(String detectorId, String nfcNumber) throws BusinessException {
		boolean result = true;

		if (null == nfcNumber || "".equals(nfcNumber)) {
			throw new BusinessException("NFC序列号不能为空!");
		}
		if (null == detectorId) {
			return true;
		}		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("detectorId", detectorId);
		paramsMap.put("nfcNumber", nfcNumber);

		String preSql = ConfigSQLUtil.getCacheSql("alarm-detector-checkNfcNumber");
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
	public void addDetector(String adminUser, Map<String, Object> detectorObj) throws BusinessException {
		if (null == adminUser) {
			throw new BusinessException("添加探测器错误,当前操作是未知的管理员!");
		}

		if (null == detectorObj.get("processorId")) {
			throw new BusinessException("添加探测器错误, 缺少处理器ID!");
		}
		
		if (null == detectorObj.get("detectorSeq")) {
			throw new BusinessException("添加探测器错误, 缺少探测器编号!");
		}
		
		if (null == detectorObj.get("nfcNumber")) {
			throw new BusinessException("添加探测器错误, 缺少NFC序列号!");
		}
		boolean nfcNumberExists = checkNfcNumber("", detectorObj.get("nfcNumber").toString());
		if (nfcNumberExists) {
			throw new BusinessException("探测器NFC序列号已存在!");
		}
		
		//获取当前处理器下维护的探测器数		
		String getCountPreSql = ConfigSQLUtil.getCacheSql("alarm-detector-getDetectorNumByProcessorId");
		Map<String, Object> paramMap = new HashMap<String, Object>();
	    paramMap.clear();
	    paramMap.put("processorId", detectorObj.get("processorId"));
		String getCountSql = ConfigSQLUtil.preProcessSQL(getCountPreSql, paramMap);
		Map<String,Object> recordMap = null;
		try {
			recordMap = springJdbcDao.queryForMap(getCountSql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("添加探测器错误,未获取到当前处理器下的探测器数.");
		}
		int curCount = 0; 
		if(recordMap!= null) {
			try{
				curCount = Integer.parseInt(recordMap.get("countNum").toString());	
			}catch(NumberFormatException ne) {
				log.info(ne.getMessage());
			}
			if(curCount > 240) {
				throw new BusinessException("添加探测器错误, 当前处理器探测器数已到上限.");
			}
		}
		// 保存数据到库
		int orderNum = curCount + 1;
		String detectorId = UUIDGenerator.getUUID();
		detectorObj.put("detectorId", detectorId);
		detectorObj.put("orderNum", orderNum);
		String preSql = ConfigSQLUtil.getCacheSql("alarm-detector-addDetector");
		String sql = ConfigSQLUtil.preProcessSQL(preSql, detectorObj);
		try {
			springJdbcDao.update(sql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("添加探测器访问数据库异常.");
		}
		// 记录日志
		LogOperationUtil.logAdminOperation(adminUser, "探测器维护", "添加探测器:nfc-number[" + detectorObj.get("nfcNumber").toString() + "].");
		
	}

	@Override
	public void updateDetector(String adminUser, Map<String, Object> detectorObj) throws BusinessException {
		if (null == adminUser) {
			throw new BusinessException("修改探测器错误,当前操作是未知的管理员!");
		}
		
		if (null == detectorObj.get("detectorId")) {
			throw new BusinessException("修改探测器错误, 缺少探测器ID!");
		}
		
		if (null == detectorObj.get("detectorSeq")) {
			throw new BusinessException("修改探测器错误, 缺少探测器编号!");
		}
		
		if (null == detectorObj.get("nfcNumber")) {
			throw new BusinessException("修改探测器错误, 缺少NFC序列号!");
		}
		boolean nfcNumberExists = checkNfcNumber(detectorObj.get("detectorId").toString(), detectorObj.get("nfcNumber").toString());
		if (nfcNumberExists) {
			throw new BusinessException("探测器NFC序列号已存在!");
		}
		String preSql = ConfigSQLUtil.getCacheSql("alarm-detector-updateDetector");
		String sql = ConfigSQLUtil.preProcessSQL(preSql, detectorObj);
		try {
			springJdbcDao.update(sql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("修改探测器访问数据库异常.");
		}
		// 记录日志
		LogOperationUtil.logAdminOperation(adminUser, "探测器维护", "修改探测器:nfc-number[" + detectorObj.get("nfcNumber").toString() + "].");
		
	}

	@Override
	public void deleteDetector(String adminUser, String detectorId) throws BusinessException {
		if (null == adminUser) {
			throw new BusinessException("删除探测器错误, 当前操作是未知的管理员!");
		}
		if (null == detectorId) {
			throw new BusinessException("删除探测器错误, 缺少探测器ID!");
		}

		// 先获取探测器
		String preSelectSql = ConfigSQLUtil.getCacheSql("alarm-detector-getDetectorById");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("detectorId", detectorId);
		String selectSql = ConfigSQLUtil.preProcessSQL(preSelectSql, paramsMap);
		List<Map<String, Object>> alDetectors;
		try {
			alDetectors = springJdbcDao.queryForList(selectSql);
		} catch (DataAccessException dae) {
			throw new BusinessException("删除探测器错误, 获取探测器访问数据库异常.");
		}
		Map<String, Object> detectorObj;
		if (alDetectors != null && alDetectors.size() > 0) {
			detectorObj = alDetectors.get(0);
		} else {
			throw new BusinessException("删除探测器错误,未查询到探测器.");
		}

		String preUpdateSql = ConfigSQLUtil.getCacheSql("alarm-detector-deleteDetector");
		paramsMap.clear();
		paramsMap.put("detectorId", detectorId);
		String updateSql = ConfigSQLUtil.preProcessSQL(preUpdateSql, paramsMap);
		try {
			springJdbcDao.update(updateSql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("删除探测器错误,访问数据库异常.");
		}
		// 记录日志
		LogOperationUtil.logAdminOperation(adminUser, "探测器维护", "删除探测器:[" + detectorObj.get("nfc_number").toString() + "].");		
	}

}
