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
import com.sierotech.alarmsys.common.utils.LogOperationUtil;
import com.sierotech.alarmsys.common.utils.UserTool;
import com.sierotech.alarmsys.server.service.ILoginService;
import com.sierotech.alarmsys.server.service.IOrgService;

/*
 * 
 * 处理组织维护相关请求
 */
@Controller
@RequestMapping("/org/mgr/")
@Scope("request")
public class OrgMgrControl{
	@Autowired
	private IOrgService orgService;
	
	@RequestMapping(value = "/getOrgTreeValidData")
    @ResponseBody
    public Map<String,Object> getOrgTreeValidData(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
    	String parentId = request.getParameter("parentId");
    	    	
		List<Map<String, Object>> orgTreeList = null;
		try {
			orgTreeList = orgService.getOrgTreeData(parentId,true);
			result.put("data", orgTreeList);
		} catch (BusinessException be) {
			result.put("msg", be.getMessage());
			return result;
		}
		result.put("returnCode", "success");
		return result;
    }
	
	@RequestMapping(value = "/getOrgTreeData")
    @ResponseBody
    public Map<String,Object> getOrgTreeData(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
    	String parentId = request.getParameter("parentId");
    	    	
		List<Map<String, Object>> orgTreeList = null;
		try {
			orgTreeList = orgService.getOrgTreeData(parentId, false);
			result.put("data", orgTreeList);
		} catch (BusinessException be) {
			result.put("msg", be.getMessage());
			return result;
		}
		result.put("returnCode", "success");
		return result;
    }
	
	@RequestMapping(value = "/add")
	@ResponseBody
	public Map<String, String> addOrg(HttpServletRequest request) {	
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		Map orgObj = ControllerUtils.toMap(request);
		if(null == orgObj.get("orgName")) {
			result.put("msg", "添加组织参数错误,缺少组织名.");
			return result;
		}
		if(null == orgObj.get("parentId")) {
			result.put("msg", "添加组织参数错误,缺少上级组织.");
			return result;
		}
		
		try {
			orgService.addOrg(UserTool.getLoginUser(request).get("user_name"), orgObj);
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
	public Map<String, String> updateOrg(HttpServletRequest request) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		Map orgObj = ControllerUtils.toMap(request);
		if(null == orgObj.get("orgName")) {
			result.put("msg", "修改组织参数错误,缺少组织名.");
			return result;
		}
		if(null == orgObj.get("orgId")) {
			result.put("msg", "修改组织参数错误,缺少组织ID.");
			return result;
		}
		if(null == orgObj.get("parentId")) {
			result.put("msg", "修改组织参数错误,缺少上级组织.");
			return result;
		}
		try {
			orgService.updateOrg(UserTool.getLoginUser(request).get("user_name"), orgObj);
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
	public Map<String, String> deleteOrg(HttpServletRequest request) {		
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		Map orgObj = ControllerUtils.toMap(request);
		
		if(null == orgObj.get("id")) {
			result.put("msg", "删除单位错误,缺少单位ID.");
			return result;
		}
		try {
			orgService.deleteOrg(UserTool.getLoginUser(request).get("user_name"), orgObj.get("id").toString());
		}catch(BusinessException be) {
			result.put("msg", be.getMessage());
			return result;
		}
		
		result.put("returnCode", "success");
		result.put("msg", "");
		return result;
	}
	
	@RequestMapping(value = "/recover")
	@ResponseBody
	public Map<String, String> recoverOrg(HttpServletRequest request) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		Map orgObj = ControllerUtils.toMap(request);
		
		if(null == orgObj.get("id")) {
			result.put("msg", "恢复组织错误,缺少组织ID.");
			return result;
		}
		try {
			orgService.updateOrg4Recover(UserTool.getLoginUser(request).get("user_name"), orgObj.get("id").toString());
		}catch(BusinessException be) {
			result.put("msg", be.getMessage());
			return result;
		}
		
		result.put("returnCode", "success");
		result.put("msg", "");
		return result;
	}
}
