/**
* <p>版权所有:(C)2018-2022 天津航峰希萨科技有限公司 </p>
* @创建人: lwm
* @创建日期: 2018年7月20日
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
import com.sierotech.alarmsys.server.service.IBoxService;

/**
* @JDK版本: 1.7
* @创建人: lwm
* @创建日期：2018年7月20日
* @功能描述: 
 */
@Service
public class BoxServiceImpl implements IBoxService{
	static final Logger log = LoggerFactory.getLogger(BoxServiceImpl.class);
	
	@Autowired	
	private JdbcTemplate springJdbcDao;
	
	@Override
	public boolean checkBoxNumber(String boxId, String boxNumber) throws BusinessException {
		boolean result = true;

		if (null == boxNumber || "".equals(boxNumber)) {
			throw new BusinessException("机箱编号不能为空!");
		}
		if (null == boxId) {
			return true;
		}		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("boxId", boxId);
		paramsMap.put("boxNumber", boxNumber);

		String preSql = ConfigSQLUtil.getCacheSql("alarm-box-checkBoxNumber");
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
	public boolean checkBoxNfcNumber(String boxId, String nfcNumber) throws BusinessException {
		boolean result = true;
		if (null == nfcNumber || "".equals(nfcNumber)) {
			throw new BusinessException("机箱NFC序列号不能为空!");
		}
		if (null == boxId) {
			return true;
		}		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("boxId", boxId);
		paramsMap.put("nfcNumber", nfcNumber);

		String preSql = ConfigSQLUtil.getCacheSql("alarm-box-checkBoxNfcNumber");
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
	public void addBox(String adminUser, Map<String, Object> boxObj) throws BusinessException {
		if (null == adminUser) {
			throw new BusinessException("添加机箱错误,当前操作是未知的管理员!");
		}

		if (null == boxObj.get("boxNumber")) {
			throw new BusinessException("添加机箱错误,缺少机箱编号!");
		}
		
		if (null == boxObj.get("nfcNumber")) {
			throw new BusinessException("添加机箱错误,缺少NFC序列号!");
		}
		
		// 检查机箱编号是否重复
		boolean boxNumberExists = checkBoxNumber("", boxObj.get("boxNumber").toString());
		if (boxNumberExists) {
			throw new BusinessException("机箱编号已存在!");
		}
		// 检查NFC序列号是否重复
		boolean nfcNumberExists = checkBoxNumber("", boxObj.get("nfcNumber").toString());
		if (nfcNumberExists) {
			throw new BusinessException("机箱NFC序列号已存在!");
		}
		String boxId = UUIDGenerator.getUUID();
		boxObj.put("boxId", boxId);
		boxObj.put("createDate", DateUtils.getNow(DateUtils.FORMAT_LONG));
		String preSql = ConfigSQLUtil.getCacheSql("alarm-box-addBox");
		String sql = ConfigSQLUtil.preProcessSQL(preSql, boxObj);
		try {
			springJdbcDao.update(sql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("添加机箱访问数据库异常.");
		}
		// 记录日志
		LogOperationUtil.logAdminOperation(adminUser, "机箱维护", "添加机箱:[" + boxObj.get("boxNumber").toString() + "].");
		
	}

	@Override
	public void updateBox(String adminUser, Map<String, Object> boxObj) throws BusinessException {
		if (null == adminUser) {
			throw new BusinessException("修改机箱错误,当前操作是未知的管理员!");
		}
		if (null == boxObj.get("boxId")) {
			throw new BusinessException("修改机箱错误,缺少机箱ID!");
		}
		
		if (null == boxObj.get("boxNumber")) {
			throw new BusinessException("修改机箱错误,缺少机箱编号!");
		}
		
		if (null == boxObj.get("nfcNumber")) {
			throw new BusinessException("修改机箱错误,缺少NFC序列号!");
		}
		
		// 检查机箱编号是否重复
		boolean boxNumberExists = checkBoxNumber(boxObj.get("boxId").toString(), boxObj.get("boxNumber").toString());
		if (boxNumberExists) {
			throw new BusinessException("机箱编号已存在!");
		}
		// 检查NFC序列号是否重复
		boolean nfcNumberExists = checkBoxNfcNumber(boxObj.get("boxId").toString(), boxObj.get("nfcNumber").toString());
		if (nfcNumberExists) {
			throw new BusinessException("机箱NFC序列号已存在!");
		}
		boxObj.put("boxId", boxObj.get("boxId"));
		String preSql = ConfigSQLUtil.getCacheSql("alarm-box-updateBox");
		String sql = ConfigSQLUtil.preProcessSQL(preSql, boxObj);
		try {
			springJdbcDao.update(sql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("修改机箱访问数据库异常.");
		}
		// 记录日志
		LogOperationUtil.logAdminOperation(adminUser, "机箱维护", "修改机箱:[" + boxObj.get("boxNumber").toString() + "].");
	}

	@Override
	public void deleteBox(String adminUser, String boxId) throws BusinessException {
		if (null == adminUser) {
			throw new BusinessException("删除机箱错误,当前操作是未知的管理员!");
		}
		if (null == boxId) {
			throw new BusinessException("删除机箱错误,缺少机箱ID!");
		}

		// 先获取机箱
		String preSelectSql = ConfigSQLUtil.getCacheSql("alarm-box-getBoxById");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("boxId", boxId);
		String selectSql = ConfigSQLUtil.preProcessSQL(preSelectSql, paramsMap);
		List<Map<String, Object>> alBoxs;
		try {
			alBoxs = springJdbcDao.queryForList(selectSql);
		} catch (DataAccessException dae) {
			throw new BusinessException("删除机箱错误,获取机箱访问数据库异常.");
		}
		Map<String, Object> boxObj;
		if (alBoxs != null && alBoxs.size() > 0) {
			boxObj = alBoxs.get(0);
		} else {
			throw new BusinessException("删除机箱错误,未查询到机箱.");
		}

		// 检查机箱下面是否维护了处理器
		String checkBoxHasProcessorPreSql = ConfigSQLUtil.getCacheSql("alarm-box-checkBoxHasProcessor");
		String checkBoxHasProcessorSql = ConfigSQLUtil.preProcessSQL(checkBoxHasProcessorPreSql, paramsMap);
		int num = 0;
		try {
			Map<String, Object> recordMap = springJdbcDao.queryForMap(checkBoxHasProcessorSql);
			if (recordMap != null) {
				num = Integer.valueOf(recordMap.get("countNum").toString());
			}
		} catch (DataAccessException ex) {
			log.info(ex.getMessage());
		} catch (Exception ex) {
			log.info(ex.getMessage());
		}
		if(num > 0) {
			throw new BusinessException("该机箱下面维护了处理器, 不能删除.");
		}
		String preUpdateSql = ConfigSQLUtil.getCacheSql("alarm-box-deleteBoxById");
		paramsMap.clear();
		paramsMap.put("boxId", boxId);
		String updateSql = ConfigSQLUtil.preProcessSQL(preUpdateSql, paramsMap);
		try {
			springJdbcDao.update(updateSql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("删除机箱错误,访问数据库异常.");
		}
		// 记录日志
		LogOperationUtil.logAdminOperation(adminUser, "机箱维护", "删除机箱:[" + boxObj.get("box_number").toString() + "].");
		
	}

}
