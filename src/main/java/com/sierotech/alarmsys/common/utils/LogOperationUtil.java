/**
* <p>版权所有:(C)2018-2022 天津航峰希萨科技有限公司 </p>
* @创建人: lwm
* @创建日期: 2018年4月15日
* @修改人: 
* @修改日期：
* @描述: 文件简要描述
 */
package com.sierotech.alarmsys.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sierotech.alarmsys.common.utils.spring.SpringContextUtil;


/**
* @JDK版本: 1.7
* @创建人: lwm
* @创建日期：2018年4月15日
* @功能描述: 日志统一记录类
 */

public class LogOperationUtil {
	static final Logger log = LoggerFactory.getLogger(LogOperationUtil.class);

	private static JdbcTemplate springJdbcDao = (JdbcTemplate)SpringContextUtil.getBean("SpringJdbcTemplate");
	
	public static void logSession(String sessionID,String userName, String clientIp, String remark) {
		Map<String,Object> paramsMap = new HashMap<String,Object>();
		paramsMap.put("id", UUIDGenerator.getUUID());
		paramsMap.put("sessionId", sessionID);
		paramsMap.put("userName", userName);
		paramsMap.put("loginDate", DateUtils.getNow(DateUtils.FORMAT_LONG));
		paramsMap.put("clientIp", clientIp);
		paramsMap.put("remark", remark);
		
		String preSql = ConfigSQLUtil.getCacheSql("alarm-log-logSession");
		String sql = ConfigSQLUtil.preProcessSQL(preSql, paramsMap);
		try {
			springJdbcDao.update(sql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
		}
	}
	
	public static void logAdminOperation(String userName,String module,String operationDesc) {
		Map<String,Object> paramsMap = new HashMap<String,Object>();
		paramsMap.put("id", UUIDGenerator.getUUID());
		paramsMap.put("userName", userName);
		paramsMap.put("operationDate", DateUtils.getNow(DateUtils.FORMAT_LONG));
		paramsMap.put("operationModule", module);
		paramsMap.put("operationDesc", operationDesc);
		
		String preSql = ConfigSQLUtil.getCacheSql("alarm-log-logAdminOperation");
		String sql = ConfigSQLUtil.preProcessSQL(preSql, paramsMap);
		try {
			springJdbcDao.update(sql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
		}
	}
	
	public static void logUserOperation(String userName,String operationType,String operationDesc) {
		
	}
}
