package com.sierotech.alarmsys.server.service;

import java.util.List;
import java.util.Map;

import com.sierotech.alarmsys.common.BusinessException;


public interface IAlarmInfoService {
	public List<Map<String,Object>> getNewAlarm4Observer() throws BusinessException;
	
	public List<Map<String,Object>> getAlarm4Observer() throws BusinessException;
	
	public void updateAlarm4Confirm(String adminUser, Map<String,String> alarmObj) throws BusinessException;
	
	public void updateAlarm4Reconfirm(String adminUser, Map<String,String> alarmObj) throws BusinessException;
	
	public void updateAlarm4RecoverValid(String adminUser, Map<String,String> alarmObj) throws BusinessException;
	
	public void updateAlarm4Process(String adminUser, Map<String,String> alarmObj) throws BusinessException;
	
	public List<Map<String,Object>> getUserUnProcessAlarms(String userId, String lastEndDate) throws BusinessException;
	
	public Map<String,String> getAlarms4Area() throws BusinessException;
	
	public List<String> getAlarms4Month() throws BusinessException;
	
}
