/**
* <p>版权所有:(C)2018-2022 天津航峰希萨科技有限公司 </p>
* @创建人: lwm
* @创建日期: 2018年7月27日
* @修改人: 
* @修改日期：
* @描述: 
 */
package com.sierotech.alarmsys.server.service.impl;

import java.io.File;
import java.util.ArrayList;
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
import com.sierotech.alarmsys.common.utils.FileUtil;
import com.sierotech.alarmsys.common.utils.UUIDGenerator;
import com.sierotech.alarmsys.context.AppContext;
import com.sierotech.alarmsys.context.ProcessContext;
import com.sierotech.alarmsys.server.service.IAlarmInfoService;

/**
* @JDK版本: 1.7
* @创建人: lwm
* @创建日期：2018年7月27日
* @功能描述: 
 */
@Service
public class AlarmInfoServiceImpl implements IAlarmInfoService {
	static final Logger log = LoggerFactory.getLogger(AlarmInfoServiceImpl.class);
	
	@Autowired	
	private JdbcTemplate springJdbcDao;
	
	/*
	 * 值班员获取最新的报警信息
	 * */
	@Override
	public List<Map<String, Object>> getNewAlarm4Observer() throws BusinessException {
		List<Map<String, Object>> result = null;
		// 获取上次读取报警信息的，最新报警日期
		String lastAlarmDate = ProcessContext.getLastReadAlarmDate();
		//根据上一次读取的最新报警日期，获取报警信息
		String getPreSql = ConfigSQLUtil.getCacheSql("alarm-alarminfo-getNewAlarm");
		Map<String, String> paramsMap = new HashMap<String, String>();
		if(lastAlarmDate != null && !"".equals(lastAlarmDate)) {
			paramsMap.put("lastDate", lastAlarmDate);
		}
		String getSql = ConfigSQLUtil.preProcessSQL(getPreSql, paramsMap);
		try {
			result = springJdbcDao.queryForList(getSql);
			if(result != null && result.size() > 0) {
				Map<String, Object> endAlarmMap = result.get(result.size() - 1);
				String newAlarmDate = endAlarmMap.get("alarm_date").toString();
				ProcessContext.registerLastReadAlarmDate(newAlarmDate);
				//ProcessContext.registerHadReadAlarmMsg(result);
			}
		} catch (DataAccessException ex) {
			log.info(ex.getMessage());
		} catch (Exception ex) {
			log.info(ex.getMessage());
		}
		return result;
	}

	/*
	 * 值班员获取所有的报警信息
	 * */
	@Override
	public List<Map<String, Object>> getAlarm4Observer() throws BusinessException {
		List<Map<String, Object>> result = null;
		String getSql = ConfigSQLUtil.getCacheSql("alarm-alarminfo-getAllAlarm");
		try {
			result = springJdbcDao.queryForList(getSql);
			if(result != null && result.size() > 0) {
				Map<String, Object> endAlarmMap = result.get(result.size() - 1);
				String newAlarmDate = endAlarmMap.get("alarm_date").toString();
				ProcessContext.registerLastReadAlarmDate(newAlarmDate);
				ProcessContext.registerHadReadAlarmMsg(result);
			}
		} catch (DataAccessException ex) {
			log.info(ex.getMessage());
		} catch (Exception ex) {
			log.info(ex.getMessage());
		}
		return result;
	}

	
	@Override
	public void updateAlarm4Confirm(String adminUser, Map<String, String> alarmObj) throws BusinessException {
		if(null == alarmObj.get("alarmId")) {
			throw new BusinessException("确认报警请求错误, 缺少报警ID参数.");
		}
		if(null == alarmObj.get("isValid")) {
			throw new BusinessException( "确认报警请求错误, 缺少是否有效参数.");
		}
		
		if("Y".equals(alarmObj.get("isValid"))) {
			if(alarmObj.get("processPerson") == null) {
				throw new BusinessException("确认报警请求错误, 未选择处警用户.");
			}
		}
		
		if("N".equals(alarmObj.get("isValid"))) {
			if(alarmObj.get("remark") == null) {
				throw new BusinessException("确认报警请求错误, 未填写备注.");
			}
		}
		
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("alarmId", alarmObj.get("alarmId"));
		paramsMap.put("isValid", alarmObj.get("isValid"));
		paramsMap.put("confirmDate", DateUtils.getNow(DateUtils.FORMAT_LONG));
		paramsMap.put("confirmPerson", adminUser);
		
		if("Y".equals(alarmObj.get("isValid"))) {
			//
			paramsMap.put("remark", "");
			paramsMap.put("status", "I");
			//检查是否已经设置处理人
			String checkAlarmSetProcessPreSql = ConfigSQLUtil.getCacheSql("alarm-alarminfo-checkAlarmSetProcess");
			String checkAlarmSetProcessSql = ConfigSQLUtil.preProcessSQL(checkAlarmSetProcessPreSql, paramsMap);
			int alarmPorcess = 0;
			try {
				Map<String, Object> record = springJdbcDao.queryForMap(checkAlarmSetProcessSql);
				if(record != null && record.get("countNum")!=null) {
					alarmPorcess = Integer.valueOf(record.get("countNum").toString());
				}
			} catch (DataAccessException ex) {
				log.info(ex.getMessage());
			} catch (Exception ex) {
				log.info(ex.getMessage());
			}
			
			String addAlarmProcessPreSql = ConfigSQLUtil.getCacheSql("alarm-alarminfo-addAlarmProcess");
			if(alarmPorcess > 0) {
				 addAlarmProcessPreSql = ConfigSQLUtil.getCacheSql("alarm-alarminfo-updateAlarmProcess");
			}
			Map<String, String> paramsMap2 = new HashMap<String, String>();
			paramsMap2.put("id", UUIDGenerator.getUUID());
			paramsMap2.put("alarmId", alarmObj.get("alarmId"));
			paramsMap2.put("processPerson", alarmObj.get("processPerson"));
			
			String addAlarmProcessSql = ConfigSQLUtil.preProcessSQL(addAlarmProcessPreSql, paramsMap2);
			try {
				springJdbcDao.update(addAlarmProcessSql);
			} catch (DataAccessException ex) {
				log.info(ex.getMessage());
			} catch (Exception ex) {
				log.info(ex.getMessage());
			}
			//更新缓存的报警信息状态
			ProcessContext.updateHadReadAlarmMsg(alarmObj.get("alarmId"), "I");
			
		}else if("N".equals(alarmObj.get("isValid"))) {
			paramsMap.put("status", "F");
			paramsMap.put("remark", alarmObj.get("remark"));
			//
		}
		//更新报警状态
		String updateAlarmPreSql = ConfigSQLUtil.getCacheSql("alarm-alarminfo-update4Confirm");
		String updateAlarmSql = ConfigSQLUtil.preProcessSQL(updateAlarmPreSql, paramsMap);		
		try {
			springJdbcDao.update(updateAlarmSql);
		} catch (DataAccessException ex) {
			log.info(ex.getMessage());
		} catch (Exception ex) {
			log.info(ex.getMessage());
		}
		//更新缓存
		if("Y".equals(alarmObj.get("isValid"))) {
			ProcessContext.updateHadReadAlarmMsg(alarmObj.get("alarmId"), "I");
		}else if("N".equals(alarmObj.get("isValid"))) {
			ProcessContext.removeHadReadAlarmMsg(alarmObj.get("alarmId"));
		}		
	}

	@Override
	public void updateAlarm4Reconfirm(String adminUser, Map<String, String> alarmObj) throws BusinessException {
		if(null == alarmObj.get("alarmId")) {
			throw new BusinessException("重新确认报警请求错误, 缺少报警ID参数.");
		}
		if(null == alarmObj.get("isValid")) {
			throw new BusinessException( "重新确认报警请求错误, 缺少是否有效参数.");
		}
		
		if("Y".equals(alarmObj.get("isValid"))) {
			if(alarmObj.get("processPerson") == null) {
				throw new BusinessException("重新确认报警请求错误, 未选择处警用户.");
			}
		}
		
		if("N".equals(alarmObj.get("isValid"))) {
			if(alarmObj.get("remark") == null) {
				throw new BusinessException("确认报警请求错误, 未填写备注.");
			}
		}
		
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("alarmId", alarmObj.get("alarmId"));
		paramsMap.put("isValid", alarmObj.get("isValid"));
		paramsMap.put("confirmDate", DateUtils.getNow(DateUtils.FORMAT_LONG));
		paramsMap.put("confirmPerson", adminUser);
		
		if("Y".equals(alarmObj.get("isValid"))) {
			//
			paramsMap.put("remark", "");
			paramsMap.put("status", "I");
			String addAlarmProcessPreSql = ConfigSQLUtil.getCacheSql("alarm-alarminfo-updateAlarmProcess");
			Map<String, String> paramsMap2 = new HashMap<String, String>();
			//paramsMap2.put("id", UUIDGenerator.getUUID());
			paramsMap2.put("alarmId", alarmObj.get("alarmId"));
			paramsMap2.put("processPerson", alarmObj.get("processPerson"));
			
			String addAlarmProcessSql = ConfigSQLUtil.preProcessSQL(addAlarmProcessPreSql, paramsMap2);		
			try {
				springJdbcDao.update(addAlarmProcessSql);
			} catch (DataAccessException ex) {
				log.info(ex.getMessage());
			} catch (Exception ex) {
				log.info(ex.getMessage());
			}
			//更新缓存的报警信息状态
			//ProcessContext.updateHadReadAlarmMsg(alarmObj.get("alarmId"), "I");
			
		}else if("N".equals(alarmObj.get("isValid"))) {
			paramsMap.put("status", "F");
			paramsMap.put("remark", alarmObj.get("remark"));
			//
		}
		//更新报警状态
		String updateAlarmPreSql = ConfigSQLUtil.getCacheSql("alarm-alarminfo-update4Confirm");
		String updateAlarmSql = ConfigSQLUtil.preProcessSQL(updateAlarmPreSql, paramsMap);		
		try {
			springJdbcDao.update(updateAlarmSql);
		} catch (DataAccessException ex) {
			log.info(ex.getMessage());
		} catch (Exception ex) {
			log.info(ex.getMessage());
		}
		//更新缓存
		if("Y".equals(alarmObj.get("isValid"))) {
			ProcessContext.updateHadReadAlarmMsg(alarmObj.get("alarmId"), "I");
		}else if("N".equals(alarmObj.get("isValid"))) {
			ProcessContext.removeHadReadAlarmMsg(alarmObj.get("alarmId"));
		}		
	}

	@Override
	public List<Map<String, Object>> getUserUnProcessAlarms(String userId, String lastEndDate)
			throws BusinessException {
		List<Map<String, Object>> result = null;
		String getPreSql = ConfigSQLUtil.getCacheSql("alarm-alarminfo-getUserUnProcessAlarms");
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("userId", userId);
		if(lastEndDate != null && !"".equals(lastEndDate)) {
			paramsMap.put("lastDate", lastEndDate);
		}
		String getSql = ConfigSQLUtil.preProcessSQL(getPreSql, paramsMap);
		try {
			result = springJdbcDao.queryForList(getSql);
		} catch (DataAccessException ex) {
			log.info(ex.getMessage());
		} catch (Exception ex) {
			log.info(ex.getMessage());
		}
		return result;
	}

	@Override
	public void updateAlarm4Process(String adminUser, Map<String, String> alarmObj) throws BusinessException {
		if(null == alarmObj.get("alarmId")) {
			throw new BusinessException("处理报警请求错误, 缺少报警ID参数.");
		}
		if(null == alarmObj.get("processMethod")) {
			throw new BusinessException( "处理报警请求错误, 处置方法不能为空.");
		}
		if(null == alarmObj.get("processResult")) {
			throw new BusinessException( "处理报警请求错误, 处置结果不能为空.");
		}
		
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("alarmId", alarmObj.get("alarmId"));
		paramsMap.put("status", "F");
		String updateAlarmStatusPreSql = ConfigSQLUtil.getCacheSql("alarm-alarminfo-updateAlarmStatus");
		String updateAlarmStatusSql = ConfigSQLUtil.preProcessSQL(updateAlarmStatusPreSql, paramsMap);
		try {
			springJdbcDao.update(updateAlarmStatusSql);
		} catch (DataAccessException ex) {
			log.info(ex.getMessage());
		} catch (Exception ex) {
			log.info(ex.getMessage());
		}
		String alarmId = alarmObj.get("alarmId").toString();
		String alarmImage = alarmObj.get("alarmImage") == null ? "" : alarmObj.get("alarmImage").toString();
		String personFeature = alarmObj.get("personFeature") == null ? "" : alarmObj.get("personFeature").toString();
		String alarmReason = alarmObj.get("alarmReason") == null ? "" : alarmObj.get("alarmReason").toString();
		String outPolice = alarmObj.get("outPolice") == null ? "" : alarmObj.get("outPolice").toString();
		
		String imageDir = AppContext.getAlarmImageDir() + File.separator + alarmId;
		//删除该目录下除当前报警图片以外的文件
		FileUtil.deleteDirectoryFile(imageDir, alarmImage);
		
		// 更新处理结果
		paramsMap.clear();
		paramsMap.put("alarmId", alarmObj.get("alarmId"));
		paramsMap.put("processPerson", adminUser);
		paramsMap.put("processDate", DateUtils.getNow(DateUtils.FORMAT_LONG));
		paramsMap.put("processMethod", alarmObj.get("processMethod"));
		paramsMap.put("processResult", alarmObj.get("processResult"));
		paramsMap.put("personFeature", personFeature);
		paramsMap.put("alarmImage", alarmImage);
		paramsMap.put("alarmReason", alarmReason);
		paramsMap.put("outPolice", outPolice);
		
		String updateAlarmProcessPreSql = ConfigSQLUtil.getCacheSql("alarm-alarminfo-update4Process");
		String updateAlarmProcessSql = ConfigSQLUtil.preProcessSQL(updateAlarmProcessPreSql, paramsMap);
		try {
			springJdbcDao.update(updateAlarmProcessSql);
		} catch (DataAccessException ex) {
			log.info(ex.getMessage());
		} catch (Exception ex) {
			log.info(ex.getMessage());
		}
		
		//更新缓存的报警信息状态
		ProcessContext.removeHadReadAlarmMsg(alarmObj.get("alarmId"));
			
	}

	@Override
	public Map<String, String> getAlarms4Area() throws BusinessException {
		Map<String, String> result = new HashMap<String, String>();
		//获取所有的机箱
		Map<String, String> paramsMap = new HashMap<String, String>();
		String getBoxsPreSql = ConfigSQLUtil.getCacheSql("alarm-box-getBoxs");
		String getBoxsSql = ConfigSQLUtil.preProcessSQL(getBoxsPreSql, paramsMap);
		
		List<Map<String, Object>> alBoxs = null;
		try {
			alBoxs = springJdbcDao.queryForList(getBoxsSql);
		} catch (DataAccessException dae) {
			//
			log.info("报表统计, 获取机箱访问数据库异常.");
		}
		
		String curYear =  DateUtils.getYear() + "-01-01";
		String getAareAlarmNumPreSql = ConfigSQLUtil.getCacheSql("alarm-report-alarmNumByBoxid");
		
		if(alBoxs != null && alBoxs.size() > 0) {
			for(Map<String, Object> box : alBoxs) {
				String boxId = box.get("id").toString();
				String posDesc = box.get("pos_desc").toString();
				if(posDesc == null || "".equals(posDesc)) {
					posDesc = "机箱:"+box.get("nfc_number").toString();
				}
				paramsMap.clear();
				paramsMap.put("boxId", boxId);
				paramsMap.put("beginDate", curYear);
				
				String getAareAlarmNumSql = ConfigSQLUtil.preProcessSQL(getAareAlarmNumPreSql, paramsMap);
				// 获取该机箱下的报警数
				int alarmCount = 0;				
				try {
					Map<String,Object> record = springJdbcDao.queryForMap(getAareAlarmNumSql);
					if(record!=null && record.get("countNum")!= null) {
						alarmCount = Integer.valueOf(record.get("countNum").toString());
					}
				} catch (DataAccessException dae) {
					//
					log.info("报表统计, 获取机箱区域报警数访问数据库异常.");
				} catch (Exception e) {
					//
					log.info(e.getMessage());
				}
				result.put(posDesc, alarmCount+"");
			}
		}
		return result;
	}

	@Override
	public List<String> getAlarms4Month() throws BusinessException {
		List<String> result = new ArrayList<String>();
		String getMonthAlarmNumPreSql = ConfigSQLUtil.getCacheSql("alarm-report-alarmNumByMonth");
		Map<String, String> paramsMap = new HashMap<String, String>();
		
		String curYear = "" + DateUtils.getYear();
		int curMonth = DateUtils.getMonth();
		for(int i=1; i<= curMonth; i++) {
			 String monthBeginDay = curYear +"-"+ i+"-01";
			 if(i< 10) {
				 monthBeginDay = curYear + "-0"+i+"-01";
			 }
			 
			 int nextMonth = i + 1;
			 String nextMonthBeginDay = curYear +"-" + nextMonth + "-01";
			 if(nextMonth< 10) {
				 nextMonthBeginDay = curYear + "-0" + nextMonth + "-01";
			 }
			 if(nextMonth == 13) {
				 int nextYear = DateUtils.getYear() + 1;
				 nextMonthBeginDay = nextYear + "-01-01";
			 }
			 paramsMap.clear();
			 paramsMap.put("beginDate", monthBeginDay);
			 paramsMap.put("endDate", nextMonthBeginDay);
			 String getMonthAlarmNumSql = ConfigSQLUtil.preProcessSQL(getMonthAlarmNumPreSql, paramsMap);
			 int alarmCount = 0;				
			 try {
				Map<String,Object> record = springJdbcDao.queryForMap(getMonthAlarmNumSql);
				if(record!=null && record.get("countNum")!= null) {
					alarmCount = Integer.valueOf(record.get("countNum").toString());
				}
			 } catch (DataAccessException dae) {
				//
				log.info("报表统计, 获取月报警数访问数据库异常.");
			 } catch (Exception e) {
				//
				log.info(e.getMessage());
			 }
			 result.add(alarmCount+"");
		 }
		return result;
	}

	@Override
	public void updateAlarm4RecoverValid(String adminUser, Map<String, String> alarmObj) throws BusinessException {
		if(null == alarmObj.get("alarmId")) {
			throw new BusinessException("恢复报警状态为有效错误, 缺少报警ID参数.");
		}
		//先获取报警信息
		String preSelectSql = ConfigSQLUtil.getCacheSql("alarm-alarminfo-getAlarminfoById");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("alarmId", alarmObj.get("alarmId"));
		String selectSql = ConfigSQLUtil.preProcessSQL(preSelectSql, paramsMap);
		List<Map<String, Object>> alAlarms=null;
		try {
			alAlarms = springJdbcDao.queryForList(selectSql);
		} catch (DataAccessException dae) {
			log.info("恢复报警状态为有效错误:{}", dae.getMessage());
		}
		Map<String, Object> oldAlarmObj;
		if (alAlarms != null && alAlarms.size() > 0) {
			oldAlarmObj = alAlarms.get(0);
		} else {
			throw new BusinessException("恢复报警状态为有效错误, 根据ID未查询到相应报警信息.");
		}
		paramsMap.clear();
		paramsMap.put("alarmId", alarmObj.get("alarmId"));
		paramsMap.put("status", "N");
		paramsMap.put("isValid", "Y");
		
		String updateAlarmStatusPreSql = ConfigSQLUtil.getCacheSql("alarm-alarminfo-updateAlarmIsValid");
		String updateAlarmStatusSql = ConfigSQLUtil.preProcessSQL(updateAlarmStatusPreSql, paramsMap);
		log.info("updateAlarmStatusSql:{}", updateAlarmStatusSql);
		try {
			springJdbcDao.update(updateAlarmStatusSql);
		} catch (DataAccessException ex) {
			log.info(ex.getMessage());
		} catch (Exception ex) {
			log.info(ex.getMessage());
		}
		
	}
}
