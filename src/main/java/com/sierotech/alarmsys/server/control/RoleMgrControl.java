package com.sierotech.alarmsys.server.control;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sierotech.alarmsys.common.BusinessException;
import com.sierotech.alarmsys.common.utils.ControllerUtils;
import com.sierotech.alarmsys.common.utils.JsonUtil;
import com.sierotech.alarmsys.common.utils.LogOperationUtil;
import com.sierotech.alarmsys.common.utils.UserTool;
import com.sierotech.alarmsys.server.service.IRoleService;
/*
 * 
 * 处理角色维护相关请求
 */
@Controller
@RequestMapping("/role/mgr/")
@Scope("request")
public class RoleMgrControl{
	@Autowired
	IRoleService roleService;
	
	
	@RequestMapping(value = "/add")
	@ResponseBody
	public Map<String, String> addRole(HttpServletRequest request) {
		
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		Map roleObj = ControllerUtils.toMap(request);
		if(null == roleObj.get("roleName")) {
			result.put("msg", "新增角色错误,缺少角色名.");
			return result;
		}
		
		try {
			roleService.addRole(UserTool.getLoginUser(request).get("user_name"), roleObj);
		}catch(BusinessException be) {
			result.put("msg", be.getMessage());
			return result;
		}
		
		result.put("returnCode", "success");
		result.put("msg", "");
		return result;
	}
	
	@RequestMapping(value = "/update")
	@ResponseBody
	public Map<String, String> updateRole(HttpServletRequest request) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		Map roleObj = ControllerUtils.toMap(request);
		
		if(null == roleObj.get("roleId")) {
			result.put("msg", "修改角色错误, 缺少角色ID.");
			return result;
		}
		if(null == roleObj.get("roleName") || "".equals(roleObj.get("roleName"))) {
			result.put("msg", "修改角色错误, 缺少角色名.");
			return result;
		}
		
		try {
			roleService.updateRole(UserTool.getLoginUser(request).get("user_name"), roleObj);
		}catch(BusinessException be) {
			result.put("msg", be.getMessage());
			return result;
		}
		
		result.put("returnCode", "success");
		result.put("msg", "");
		return result;
	}
	
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Map<String, String> deleteRole(HttpServletRequest request) {		
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		Map roleObj = ControllerUtils.toMap(request);
		
		if(null == roleObj.get("id")) {
			result.put("msg", "删除角色错误,缺少角色ID.");
			return result;
		}
		try {
			roleService.deleteRole(UserTool.getLoginUser(request).get("user_name"), roleObj.get("id").toString());
		}catch(BusinessException be) {
			result.put("msg", be.getMessage());
			return result;
		}
		
		result.put("returnCode", "success");
		result.put("msg", "");
		return result;
	}
	
	@RequestMapping(value = "/allotUser")
	@ResponseBody
	public Map<String, String> allotUser(HttpServletRequest request) {		
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		Map roleObj = ControllerUtils.toMap(request);
		
		if(null == roleObj.get("roleId")) {
			result.put("msg", "为角色分配用户错误, 缺少角色ID.");
			return result;
		}
		if(null == roleObj.get("userId")) {
			result.put("msg", "为角色分配用户错误, 缺少用户ID.");
			return result;
		}
		try {
			roleService.addRoleUser(UserTool.getLoginUser(request).get("user_name"), roleObj.get("userId").toString(), roleObj.get("roleId").toString());
		}catch(BusinessException be) {
			result.put("msg", be.getMessage());
			return result;
		}
		
		result.put("returnCode", "success");
		result.put("msg", "");
		return result;
	}
	
	@RequestMapping(value = "/unAllotUser")
	@ResponseBody
	public Map<String, String> unAllotUser(HttpServletRequest request) {		
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		Map roleObj = ControllerUtils.toMap(request);
		
		if(null == roleObj.get("roleId")) {
			result.put("msg", "为角色取消分配用户错误, 缺少角色ID.");
			return result;
		}
		if(null == roleObj.get("userId")) {
			result.put("msg", "为角色取消分配用户错误, 缺少用户ID.");
			return result;
		}
		try {
			roleService.deleteRoleUser(UserTool.getLoginUser(request).get("user_name"), roleObj.get("userId").toString(), roleObj.get("roleId").toString());
		}catch(BusinessException be) {
			result.put("msg", be.getMessage());
			return result;
		}
		
		result.put("returnCode", "success");
		result.put("msg", "");
		return result;
	}
	
	@RequestMapping(value = "/allotFunc")
	@ResponseBody
	public Map<String, String> addFunc(HttpServletRequest request) {
		
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		String roleId = request.getParameter("roleId");
		String allotFuncs = request.getParameter("allotFuncs");
		
		// allotFuncs 正确的json格式 "[{'id':'a1','action':'A'},{'id':'a2','action':'D'},{'id':'f9','action':'A'}]";
		List<Map<String,String>> alAllotFunc;
		try {
			alAllotFunc = JsonUtil.jsonToList(allotFuncs, HashMap.class);
		}catch(Exception e) {
			result.put("msg", "角色分配功能，功能权限数据为不正确的格式.");
			return result;
		}
		try {
			roleService.update4AllotFunc(UserTool.getLoginUser(request).get("user_name"), roleId, alAllotFunc);
		}catch(BusinessException be) {
			result.put("msg", be.getMessage());
			return result;
		}
		result.put("returnCode", "success");
		result.put("msg", "");
		return result;
	}
}
