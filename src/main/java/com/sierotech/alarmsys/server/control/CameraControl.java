/**
* <p>版权所有:(C)2018-2022 天津航峰希萨科技有限公司 </p>
* @创建人: lwm
* @创建日期: 2018年8月17日
* @修改人: 
* @修改日期：
* @描述: 
 */
package com.sierotech.alarmsys.server.control;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sierotech.alarmsys.common.BusinessException;
import com.sierotech.alarmsys.common.utils.ControllerUtils;
import com.sierotech.alarmsys.common.utils.UserTool;
import com.sierotech.alarmsys.server.service.ICameraService;

/**
* @JDK版本: 1.7
* @创建人: lwm
* @创建日期：2018年8月17日
* @功能描述: 
 */
@Controller
@RequestMapping("/camera/mgr/")
@Scope("request")
public class CameraControl {
	@Autowired
	private ICameraService cameraService;
	
	@RequestMapping(value = "/addCamera")
	@ResponseBody
	public Map<String, String> addCamera(HttpServletRequest request) {	
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		Map cameraObj = ControllerUtils.toMap(request);
		
		if (null == cameraObj.get("classId")) {
			result.put("msg", "添加摄像机错误, 摄像机型号不能为空!");
			return result;
		}
		
		if (null == cameraObj.get("cameraNum")) {
			result.put("msg", "添加摄像机错误, 摄像机编号不能为空!");
			return result;
		}
		
		if (null == cameraObj.get("ip")) {
			result.put("msg", "添加摄像机错误, 摄像机ip不能为空!");
			return result;
		}
		
		if (null == cameraObj.get("webPort")) {
			result.put("msg", "添加摄像机错误, 摄像机web端口不能为空!");
			return result;
		}
		if (null == cameraObj.get("recorderIp")) {
			result.put("msg", "添加摄像机错误, 摄像机关联的录像机ip不能为空!");
			return result;
		}
		if (null == cameraObj.get("recorderPort")) {
			result.put("msg", "添加摄像机错误, 摄像机关联的录像机端口不能为空!");
			return result;
		}
		if (null == cameraObj.get("cameraUserName")) {
			result.put("msg", "添加摄像机错误, 摄像机登录名不能为空!");
			return result;
		}
		if (null == cameraObj.get("cameraPassword")) {
			result.put("msg", "添加摄像机错误, 摄像机密码不能为空!");
			return result;
		}
		if (null == cameraObj.get("recorderUserName")) {
			result.put("msg", "添加摄像机错误, 录像机登录名不能为空!");
			return result;
		}
		if (null == cameraObj.get("recorderPassword")) {
			result.put("msg", "添加摄像机错误, 录像机密码不能为空!");
			return result;
		}
		if (null == cameraObj.get("channelId")) {
			result.put("msg", "添加摄像机错误, 录像机通道不能为空!");
			return result;
		}
		try {
			cameraService.addCamera(UserTool.getLoginUser(request).get("user_name"), cameraObj);
		}catch(BusinessException be) {
			result.put("msg", be.getMessage());
			return result;
		}
		result.put("returnCode", "success");
		result.put("msg", "");
		return result;
	}
	
	@RequestMapping(value = "/updateCamera")
	@ResponseBody
	public Map<String, String> updateCamera(HttpServletRequest request) {	
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		Map cameraObj = ControllerUtils.toMap(request);
		
		if (null == cameraObj.get("cameraId")) {
			result.put("msg", "修改摄像机错误, 摄像机ID不能为空!");
			return result;
		}
		if (null == cameraObj.get("classId")) {
			result.put("msg", "修改摄像机错误, 摄像机型号不能为空!");
			return result;
		}
		if (null == cameraObj.get("cameraNum")) {
			result.put("msg", "修改摄像机错误, 摄像机编号不能为空!");
			return result;
		}		
		if (null == cameraObj.get("ip")) {
			result.put("msg", "修改摄像机错误, 摄像机ip不能为空!");
			return result;
		}		
		if (null == cameraObj.get("webPort")) {
			result.put("msg", "修改摄像机错误, 摄像机web端口不能为空!");
			return result;
		}
		if (null == cameraObj.get("recorderIp")) {
			result.put("msg", "修改摄像机错误, 摄像机关联的录像机ip不能为空!");
			return result;
		}
		if (null == cameraObj.get("recorderPort")) {
			result.put("msg", "修改摄像机错误, 摄像机关联的录像机端口不能为空!");
			return result;
		}
		if (null == cameraObj.get("cameraUserName")) {
			result.put("msg", "修改摄像机错误, 摄像机登录名不能为空!");
			return result;
		}
		if (null == cameraObj.get("cameraPassword")) {
			result.put("msg", "修改摄像机错误, 摄像机密码不能为空!");
			return result;
		}
		if (null == cameraObj.get("recorderUserName")) {
			result.put("msg", "修改摄像机错误, 录像机登录名不能为空!");
			return result;
		}
		if (null == cameraObj.get("recorderPassword")) {
			result.put("msg", "修改摄像机错误, 录像机密码不能为空!");
			return result;
		}
		if (null == cameraObj.get("channelId")) {
			result.put("msg", "修改摄像机错误, 录像机通道不能为空!");
			return result;
		}
		try {
			cameraService.updateCamera(UserTool.getLoginUser(request).get("user_name"), cameraObj);
		}catch(BusinessException be) {
			result.put("msg", be.getMessage());
			return result;
		}
		result.put("returnCode", "success");
		result.put("msg", "");
		return result;
	}
	
	@RequestMapping(value = "/deleteCamera")
	@ResponseBody
	public Map<String, String> deleteCamera(HttpServletRequest request) {		
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		Map cameraObj = ControllerUtils.toMap(request);
		
		if(null == cameraObj.get("id")) {
			result.put("msg", "删除摄像机错误,缺少摄像机ID.");
			return result;
		}
		try {
			cameraService.deleteCamera(UserTool.getLoginUser(request).get("user_name"), cameraObj.get("id").toString());
		}catch(BusinessException be) {
			result.put("msg", be.getMessage());
			return result;
		}
		
		result.put("returnCode", "success");
		result.put("msg", "");
		return result;
	}
	
	@RequestMapping(value = "/checkCameraNum")
	@ResponseBody
	public Map<String, String> checkCameraNum(HttpServletRequest request) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		String msg = "";
		String cameraId = request.getParameter("cameraId");
		if(cameraId == null) {
			result.put("msg", "摄像机编号验证,缺少ID.");
			return result;
		}
		String cameraNum = request.getParameter("cameraNum");
		if(cameraNum == null) {
			result.put("msg", "摄像机编号验证,缺少摄像机编号.");
			return result;
		}
		boolean cameraExist = true;
		try {
			cameraExist = cameraService.checkCameraNumber(cameraId, cameraNum);
		}catch(BusinessException be) {
			msg = be.getMessage();
		}
		if (cameraExist) {
			result.put("exist", "true");
		}else {
			result.put("exist", "false");
		}
		result.put("returnCode", "success");
		result.put("msg", msg);
		return result;
	}
	
	@RequestMapping(value = "/addPreset")
	@ResponseBody
	public Map<String, String> addPreset(HttpServletRequest request) {	
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		Map presetObj = ControllerUtils.toMap(request);
		
		if (null == presetObj.get("cameraId")) {
			result.put("msg", "添加预置位错误, 摄像机ID不能为空!");
			return result;
		}
		
		if (null == presetObj.get("presetNum")) {
			result.put("msg", "添加预置位错误, 预置位编号不能为空!");
			return result;
		}
		
		if (null == presetObj.get("presetDesc")) {
			presetObj.put("presetDesc", "");
		}
		try {
			cameraService.addPreset(UserTool.getLoginUser(request).get("user_name"), presetObj);
		}catch(BusinessException be) {
			result.put("msg", be.getMessage());
			return result;
		}
		result.put("returnCode", "success");
		result.put("msg", "");
		return result;
	}
	
	@RequestMapping(value = "/updatePreset")
	@ResponseBody
	public Map<String, String> updatePreset(HttpServletRequest request) {	
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		Map presetObj = ControllerUtils.toMap(request);
		
		if (null == presetObj.get("presetId")) {
			result.put("msg", "修改预置位错误, 预置位ID不能为空!");
			return result;
		}
		
		if (null == presetObj.get("presetNum")) {
			result.put("msg", "修改预置位错误, 预置位编号不能为空!");
			return result;
		}
		
		if (null == presetObj.get("presetDesc")) {
			presetObj.put("presetDesc", "");
		}
		try {
			cameraService.updatePreset(UserTool.getLoginUser(request).get("user_name"), presetObj);
		}catch(BusinessException be) {
			result.put("msg", be.getMessage());
			return result;
		}
		result.put("returnCode", "success");
		result.put("msg", "");
		return result;
	}
	
	@RequestMapping(value = "/deletePreset")
	@ResponseBody
	public Map<String, String> deletePreset(HttpServletRequest request) {		
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		Map presetObj = ControllerUtils.toMap(request);
		
		if(null == presetObj.get("id")) {
			result.put("msg", "删除预置位错误,缺少预置位ID.");
			return result;
		}
		try {
			cameraService.deletePreset(UserTool.getLoginUser(request).get("user_name"), presetObj.get("id").toString());
		}catch(BusinessException be) {
			result.put("msg", be.getMessage());
			return result;
		}
		
		result.put("returnCode", "success");
		result.put("msg", "");
		return result;
	}
	
	@RequestMapping(value = "/bindDevice")
	@ResponseBody
	public Map<String, String> updateCameraDeploy(HttpServletRequest request) {		
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		Map cameraDeploy = ControllerUtils.toMap(request);
		if (null == cameraDeploy.get("deviceId")) {
			result.put("msg", "摄像机关联设备错误,缺少关联设备ID!");
			return result;
		}
		if (null == cameraDeploy.get("cameraId")) {
			result.put("msg", "摄像机关联设备错误,缺少摄像机ID!");
			return result;
		}
		if (null == cameraDeploy.get("presetId")) {
			result.put("msg", "摄像机关联设备错误,缺少预置位ID!");
			return result;
		}
		
		try {
			cameraService.updateCameraDeploy(UserTool.getLoginUser(request).get("user_name"), cameraDeploy);
		}catch(BusinessException be) {
			result.put("msg", be.getMessage());
			return result;
		}
		
		result.put("returnCode", "success");
		result.put("msg", "");
		return result;
	}
}
