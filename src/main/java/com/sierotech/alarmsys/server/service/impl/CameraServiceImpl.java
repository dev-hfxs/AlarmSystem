/**
* <p>版权所有:(C)2018-2022 天津航峰希萨科技有限公司 </p>
* @创建人: lwm
* @创建日期: 2018年8月17日
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
import com.sierotech.alarmsys.common.utils.LogOperationUtil;
import com.sierotech.alarmsys.common.utils.UUIDGenerator;
import com.sierotech.alarmsys.server.service.ICameraService;

/**
* @JDK版本: 1.7
* @创建人: lwm
* @创建日期：2018年8月17日
* @功能描述: 
 */
@Service
public class CameraServiceImpl implements ICameraService {
    static final Logger log = LoggerFactory.getLogger(CameraServiceImpl.class);
	
	@Autowired	
	private JdbcTemplate springJdbcDao;
	
	@Override
	public void addCamera(String adminUser, Map<String, Object> cameraObj) throws BusinessException {
		if (null == adminUser) {
			throw new BusinessException("添加摄像机错误,当前操作是未知的管理员!");
		}
		
		if (null == cameraObj.get("classId")) {
			throw new BusinessException("添加摄像机错误, 摄像机型号不能为空!");
		}
		
		if (null == cameraObj.get("cameraNum")) {
			throw new BusinessException("添加摄像机错误, 摄像机编号不能为空!");
		}
		
		if (null == cameraObj.get("ip")) {
			throw new BusinessException("添加摄像机错误, 摄像机ip不能为空!");
		}
		
		if (null == cameraObj.get("webPort")) {
			throw new BusinessException("添加摄像机错误, 摄像机web端口不能为空!");
		}
		if (null == cameraObj.get("recorderIp")) {
			throw new BusinessException("添加摄像机错误, 摄像机关联的录像机ip不能为空!");
		}
		if (null == cameraObj.get("recorderPort")) {
			throw new BusinessException("添加摄像机错误, 摄像机关联的录像机端口不能为空!");
		}
		if (null == cameraObj.get("cameraUserName")) {
			throw new BusinessException("添加摄像机错误, 摄像机登录名不能为空!");
		}
		if (null == cameraObj.get("cameraPassword")) {
			throw new BusinessException("添加摄像机错误, 摄像机密码不能为空!");
		}
		if (null == cameraObj.get("recorderUserName")) {
			throw new BusinessException("添加摄像机错误, 录像机登录名不能为空!");
		}
		if (null == cameraObj.get("recorderPassword")) {
			throw new BusinessException("添加摄像机错误, 录像机密码不能为空!");
		}
		if (null == cameraObj.get("channelId")) {
			throw new BusinessException("添加摄像机错误, 录像机通道不能为空!");
		}
		// 摄像机编号是否重复
		boolean cameraNumberExists = checkCameraNumber("", cameraObj.get("cameraNum").toString());
		if (cameraNumberExists) {
			throw new BusinessException("摄像机编号已存在!");
		}
		
		if(cameraObj.get("longitude") == null || "".equals(cameraObj.get("longitude"))) {
			cameraObj.put("longitude", "null");
		}
		if(cameraObj.get("latitude") == null || "".equals(cameraObj.get("latitude"))) {
			cameraObj.put("latitude", "null");
		}
		String cameraId = UUIDGenerator.getUUID();
		cameraObj.put("id", cameraId);
		String preSql = ConfigSQLUtil.getCacheSql("alarm-camera-addCamera");
		String sql = ConfigSQLUtil.preProcessSQL(preSql, cameraObj);
		try {
			springJdbcDao.update(sql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("添加摄像机访问数据库异常.");
		}
		// 记录日志
		LogOperationUtil.logAdminOperation(adminUser, "摄像机维护", "添加摄像机:[" + cameraObj.get("cameraNum").toString() + "].");
	}

	@Override
	public void updateCamera(String adminUser, Map<String, Object> cameraObj) throws BusinessException {
		if (null == adminUser) {
			throw new BusinessException("修改摄像机错误,当前操作是未知的管理员!");
		}
		if (null == cameraObj.get("cameraId")) {
			throw new BusinessException("修改摄像机错误, 摄像机id不能为空!");
		}
		if (null == cameraObj.get("classId")) {
			throw new BusinessException("修改摄像机错误, 摄像机型号不能为空!");
		}		
		if (null == cameraObj.get("cameraNum")) {
			throw new BusinessException("修改摄像机错误, 摄像机编号不能为空!");
		}		
		if (null == cameraObj.get("ip")) {
			throw new BusinessException("修改摄像机错误, 摄像机ip不能为空!");
		}		
		if (null == cameraObj.get("webPort")) {
			throw new BusinessException("修改摄像机错误, 摄像机web端口不能为空!");
		}
		if (null == cameraObj.get("recorderIp")) {
			throw new BusinessException("修改摄像机错误, 摄像机关联的录像机ip不能为空!");
		}
		if (null == cameraObj.get("recorderPort")) {
			throw new BusinessException("修改摄像机错误, 摄像机关联的录像机端口不能为空!");
		}
		if (null == cameraObj.get("cameraUserName")) {
			throw new BusinessException("修改摄像机错误, 摄像机登录名不能为空!");
		}
		if (null == cameraObj.get("cameraPassword")) {
			throw new BusinessException("修改摄像机错误, 摄像机密码不能为空!");
		}
		if (null == cameraObj.get("recorderUserName")) {
			throw new BusinessException("修改摄像机错误, 录像机登录名不能为空!");
		}
		if (null == cameraObj.get("recorderPassword")) {
			throw new BusinessException("修改摄像机错误, 录像机密码不能为空!");
		}
		if (null == cameraObj.get("channelId")) {
			throw new BusinessException("修改摄像机错误, 录像机通道不能为空!");
		}
		if(cameraObj.get("longitude") == null || "".equals(cameraObj.get("longitude"))) {
			cameraObj.put("longitude", "null");
		}
		if(cameraObj.get("latitude") == null || "".equals(cameraObj.get("latitude"))) {
			cameraObj.put("latitude", "null");
		}
		
		// 摄像机编号是否重复
		boolean cameraNumberExists = checkCameraNumber(cameraObj.get("cameraId").toString(), cameraObj.get("cameraNum").toString());
		if (cameraNumberExists) {
			throw new BusinessException("摄像机编号已存在!");
		}
		
		// 先获取摄像机
		String preSelectSql = ConfigSQLUtil.getCacheSql("alarm-camera-getCameraById");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("cameraId", cameraObj.get("cameraId"));
		String selectSql = ConfigSQLUtil.preProcessSQL(preSelectSql, paramsMap);
		List<Map<String, Object>> alCameras = null;
		try {
			alCameras = springJdbcDao.queryForList(selectSql);
		} catch (DataAccessException dae) {
			log.info(dae.getMessage());
		}
		Map<String, Object> oldCameraObj;
		if (alCameras != null && alCameras.size() > 0) {
			oldCameraObj = alCameras.get(0);
		} else {
			throw new BusinessException("修改摄像机错误,未查询到摄像机.");
		}
				
		String preSql = ConfigSQLUtil.getCacheSql("alarm-camera-updateCamera");
		String sql = ConfigSQLUtil.preProcessSQL(preSql, cameraObj);
		try {
			springJdbcDao.update(sql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("修改摄像机访问数据库异常.");
		}
		// 记录日志
		LogOperationUtil.logAdminOperation(adminUser, "摄像机维护", "修改摄像机:[" + cameraObj.get("cameraNum").toString() + "].");
	}

	@Override
	public void deleteCamera(String adminUser, String cameraId) throws BusinessException {
		if (null == adminUser) {
			throw new BusinessException("删除摄像机错误, 当前操作是未知的管理员!");
		}
		if (null == cameraId) {
			throw new BusinessException("删除摄像机错误, 摄像机ID不能为空!");
		}

		// 先获取摄像机
		String preSelectSql = ConfigSQLUtil.getCacheSql("alarm-camera-getCameraById");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("cameraId", cameraId);
		String selectSql = ConfigSQLUtil.preProcessSQL(preSelectSql, paramsMap);
		List<Map<String, Object>> alCameras = null;
		try {
			alCameras = springJdbcDao.queryForList(selectSql);
		} catch (DataAccessException dae) {
			log.info(dae.getMessage());
		}
		Map<String, Object> cameraObj;
		if (alCameras != null && alCameras.size() > 0) {
			cameraObj = alCameras.get(0);
		} else {
			throw new BusinessException("删除摄像机错误,未查询到摄像机.");
		}

		// 检查摄像机是否已关联设备
		String checkCameraHasUsePreSql = ConfigSQLUtil.getCacheSql("alarm-camera-checkCameraBindDevice");
		String checkCameraHasUseSql = ConfigSQLUtil.preProcessSQL(checkCameraHasUsePreSql, paramsMap);
		int num = 0;
		try {
			Map<String, Object> recordMap = springJdbcDao.queryForMap(checkCameraHasUseSql);
			if (recordMap != null) {
				num = Integer.valueOf(recordMap.get("countNum").toString());
			}
		} catch (DataAccessException ex) {
			log.info(ex.getMessage());
		} catch (Exception ex) {
			log.info(ex.getMessage());
		}
		if(num > 0) {
			throw new BusinessException("该摄像机已关联设备, 不能删除.");
		}
		String deleteCameraPreSql = ConfigSQLUtil.getCacheSql("alarm-camera-deleteCameraById");
		paramsMap.clear();
		paramsMap.put("cameraId", cameraId);
		String deleteCameraSql = ConfigSQLUtil.preProcessSQL(deleteCameraPreSql, paramsMap);
		
		String deletePresetPreSql = ConfigSQLUtil.getCacheSql("alarm-camera-deleteCameraPresetByCameraId");
		String deletePresetSql = ConfigSQLUtil.preProcessSQL(deletePresetPreSql, paramsMap);
		
		try {
			springJdbcDao.update(deletePresetSql);
			springJdbcDao.update(deleteCameraSql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("删除摄像机错误,访问数据库异常.");
		}
		// 记录日志
		LogOperationUtil.logAdminOperation(adminUser, "摄像机维护", "删除摄像机:[" + cameraObj.get("camera_num").toString() + "].");
	}

	
	@Override
	public void addPreset(String adminUser, Map<String, Object> presetObj) throws BusinessException {
		if (null == adminUser) {
			throw new BusinessException("添加摄像机预置位错误,当前操作是未知的管理员!");
		}
		
		if (null == presetObj.get("cameraId")) {
			throw new BusinessException("添加摄像机预置位错误, 摄像机ID不能为空!");
		}
		if (null == presetObj.get("presetNum")) {
			throw new BusinessException("添加摄像机预置位错误, 预置位编号不能为空!");
		}
		
		if (null == presetObj.get("presetDesc")) {
			presetObj.put("presetDesc", "");
		}
		
		String presetId = UUIDGenerator.getUUID();
		presetObj.put("id", presetId);
		String preSql = ConfigSQLUtil.getCacheSql("alarm-camera-addCameraPreset");
		String sql = ConfigSQLUtil.preProcessSQL(preSql, presetObj);
		try {
			springJdbcDao.update(sql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("添加摄像机预置位访问数据库异常.");
		}
		// 记录日志
		LogOperationUtil.logAdminOperation(adminUser, "摄像机预置位维护", "添加预置位:预置位编号[" + presetObj.get("presetNum").toString() + "],摄像机ID["+ presetObj.get("cameraId").toString() +"].");
	}

	@Override
	public void updatePreset(String adminUser, Map<String, Object> presetObj) throws BusinessException {
		if (null == adminUser) {
			throw new BusinessException("修改摄像机预置位错误,当前操作是未知的管理员!");
		}
		
		if (null == presetObj.get("presetId")) {
			throw new BusinessException("修改摄像机预置位错误, 预置位ID不能为空!");
		}
		if (null == presetObj.get("presetNum")) {
			throw new BusinessException("修改摄像机预置位错误, 预置位编号不能为空!");
		}

		if (null == presetObj.get("presetDesc")) {
			presetObj.put("presetDesc", "");
		}
		
		// 先获取摄像机预置位
		String preSelectSql = ConfigSQLUtil.getCacheSql("alarm-camera-getPresetById");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("presetId", presetObj.get("presetId"));
		String selectSql = ConfigSQLUtil.preProcessSQL(preSelectSql, paramsMap);
		List<Map<String, Object>> alPresets = null;
		try {
			alPresets = springJdbcDao.queryForList(selectSql);
		} catch (DataAccessException dae) {
			log.info(dae.getMessage());
		}
		Map<String, Object> oldPresetObj;
		if (alPresets != null && alPresets.size() > 0) {
			oldPresetObj = alPresets.get(0);
		} else {
			throw new BusinessException("修改摄像机错误,未查询到预置位.");
		}
				
		String preSql = ConfigSQLUtil.getCacheSql("alarm-camera-updateCameraPreset");
		String sql = ConfigSQLUtil.preProcessSQL(preSql, presetObj);
		try {
			springJdbcDao.update(sql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("修改摄像机预置位访问数据库异常.");
		}
		// 记录日志
		LogOperationUtil.logAdminOperation(adminUser, "摄像机预置位维护", "修改预置位:修改前的编号[" + oldPresetObj.get("preset_num").toString() + "],预置位ID["+ presetObj.get("presetId").toString() +"].");
	}

	@Override
	public void deletePreset(String adminUser, String presetId) throws BusinessException {
		if (null == adminUser) {
			throw new BusinessException("删除摄像机预置位错误, 当前操作是未知的管理员!");
		}
		if (null == presetId) {
			throw new BusinessException("删除摄像机预置位错误, 预置位ID不能为空!");
		}

		// 先获取摄像机预置位
		String preSelectSql = ConfigSQLUtil.getCacheSql("alarm-camera-getPresetById");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("presetId", presetId);
		String selectSql = ConfigSQLUtil.preProcessSQL(preSelectSql, paramsMap);
		List<Map<String, Object>> alPresets = null;
		try {
			alPresets = springJdbcDao.queryForList(selectSql);
		} catch (DataAccessException dae) {
			log.info(dae.getMessage());
		}
		Map<String, Object> presetObj;
		if (alPresets != null && alPresets.size() > 0) {
			presetObj = alPresets.get(0);
		} else {
			throw new BusinessException("删除摄像机预置位错误,未查询到预置位.");
		}

		// 检查预置位是否已关联设备
		String checkPresetHasUsePreSql = ConfigSQLUtil.getCacheSql("alarm-camera-checkPresetBindDevice");
		String checkPresetHasUseSql = ConfigSQLUtil.preProcessSQL(checkPresetHasUsePreSql, paramsMap);
		int num = 0;
		try {
			Map<String, Object> recordMap = springJdbcDao.queryForMap(checkPresetHasUseSql);
			if (recordMap != null) {
				num = Integer.valueOf(recordMap.get("countNum").toString());
			}
		} catch (DataAccessException ex) {
			log.info(ex.getMessage());
		} catch (Exception ex) {
			log.info(ex.getMessage());
		}
		if(num > 0) {
			throw new BusinessException("该预置位已关联设备, 不能删除.");
		}
		String preUpdateSql = ConfigSQLUtil.getCacheSql("alarm-camera-deleteCameraPresetById");
		paramsMap.clear();
		paramsMap.put("presetId", presetId);
		String updateSql = ConfigSQLUtil.preProcessSQL(preUpdateSql, paramsMap);
		try {
			springJdbcDao.update(updateSql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("删除摄像机预置位错误,访问数据库异常.");
		}
		// 记录日志
		LogOperationUtil.logAdminOperation(adminUser, "摄像机维护", "删除摄像机预置位:编号[" + presetObj.get("preset_num").toString() + "],摄像机ID["+presetObj.get("camera_id") +"]");	
	}

	@Override
	public boolean checkCameraNumber(String cameraId, String cameraNumber) throws BusinessException {
		boolean result = true;

		if (null == cameraNumber || "".equals(cameraNumber)) {
			throw new BusinessException("摄像机编号不能为空!");
		}
		if (null == cameraNumber) {
			return true;
		}		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("cameraId", cameraId);
		paramsMap.put("cameraNumber", cameraNumber);

		String preSql = ConfigSQLUtil.getCacheSql("alarm-camera-checkCameraNumber");
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
	public void updateCameraDeploy(String adminUser, Map<String, Object> cameraDeploy) throws BusinessException {
		if (null == adminUser) {
			throw new BusinessException("摄像机关联设备错误,当前操作是未知的管理员!");
		}		
		if (null == cameraDeploy.get("deviceId")) {
			throw new BusinessException("摄像机关联设备错误,缺少关联设备ID!");
		}
		if (null == cameraDeploy.get("cameraId")) {
			throw new BusinessException("摄像机关联设备错误,缺少摄像机ID!");
		}
		if (null == cameraDeploy.get("presetId")) {
			throw new BusinessException("摄像机关联设备错误,缺少预置位ID!");
		}
		
		//根据设备id查询摄像机关联记录
		String preSelectSql = ConfigSQLUtil.getCacheSql("alarm-camera-getCameraDeployByDeviceId");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("deviceId", cameraDeploy.get("deviceId").toString());
		String selectSql = ConfigSQLUtil.preProcessSQL(preSelectSql, paramsMap);
		List<Map<String, Object>> alDeploys = null;
		try {
			alDeploys = springJdbcDao.queryForList(selectSql);
		} catch (DataAccessException dae) {
			log.info(dae.getMessage());
		}
		Map<String, Object> oldDeploy = null;
		if (alDeploys != null && alDeploys.size() > 0) {
			oldDeploy = alDeploys.get(0);
		} else {
			//
		}
		
		String preUpdateSql = ConfigSQLUtil.getCacheSql("alarm-camera-addCameraDeploy");
		if(oldDeploy == null){
			//
			cameraDeploy.put("id", UUIDGenerator.getUUID());
			
		}else {
			preUpdateSql = ConfigSQLUtil.getCacheSql("alarm-camera-updateCameraDeploy");
		}
		
		String updateSql = ConfigSQLUtil.preProcessSQL(preUpdateSql, cameraDeploy);
		try {
			springJdbcDao.update(updateSql);
		} catch (DataAccessException dae) {
			log.info(dae.getMessage());
		}
		
		// 记录日志
		if(oldDeploy == null){
			LogOperationUtil.logAdminOperation(adminUser, "摄像机关联设备维护", "首次关联,设备ID[" + cameraDeploy.get("deviceId").toString() + "],摄像机ID["+cameraDeploy.get("cameraId") +"]");
		}else {
			LogOperationUtil.logAdminOperation(adminUser, "摄像机关联设备维护", "更新关联,设备ID[" + oldDeploy.get("device_id").toString() + "],摄像机ID["+oldDeploy.get("camera_id") +"]");
		}
	}
}
