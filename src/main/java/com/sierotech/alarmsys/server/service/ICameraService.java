/**
* <p>版权所有:(C)2018-2022 天津航峰希萨科技有限公司 </p>
* @创建人: lwm
* @创建日期: 2018年4月19日
* @修改人: 
* @修改日期：
* @描述: 
 */
package com.sierotech.alarmsys.server.service;

import java.util.List;
import java.util.Map;

import com.sierotech.alarmsys.common.BusinessException;

/**
* @JDK版本: 1.7
* @创建人: lwm
* @创建日期：2018年4月19日
* @功能描述: 摄像机维护接口
 */
public interface ICameraService {
	
	public void addCamera(String adminUser,Map<String,Object> cameraObj) throws BusinessException;
	
	public void updateCamera(String adminUser,Map<String,Object> cameraObj) throws BusinessException;
	
	public void deleteCamera(String adminUser,String cameraId) throws BusinessException;

	public void addPreset(String adminUser,Map<String,Object> presetObj) throws BusinessException;
	
	public void updatePreset(String adminUser,Map<String,Object> presetObj) throws BusinessException;
	
	public void deletePreset(String adminUser,String presetId) throws BusinessException;
    
	public boolean checkCameraNumber(String cameraId, String cameraNumber) throws BusinessException;
	
	public void updateCameraDeploy(String adminUser,Map<String,Object> cameraDeploy) throws BusinessException;
}
