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
import com.sierotech.alarmsys.server.service.IBoxService;

/*
 * 
 * 处理机箱维护相关请求
 */
@Controller
@RequestMapping("/box/mgr/")
@Scope("request")
public class BoxMgrControl{
	@Autowired
	private IBoxService boxService;
	
	
	
	@RequestMapping(value = "/add")
	@ResponseBody
	public Map<String, String> addBox(HttpServletRequest request) {	
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		Map boxObj = ControllerUtils.toMap(request);
		if(null == boxObj.get("boxNumber")) {
			result.put("msg", "添加机箱参数错误,缺少机箱编号.");
			return result;
		}
		if(null == boxObj.get("nfcNumber")) {
			result.put("msg", "添加机箱参数错误,缺少NFC序列号.");
			return result;
		}
		
		try {
			boxService.addBox(UserTool.getLoginUser(request).get("user_name"), boxObj);
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
	public Map<String, String> updateBox(HttpServletRequest request) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		Map boxObj = ControllerUtils.toMap(request);
		if(null == boxObj.get("boxNumber")) {
			result.put("msg", "修改机箱参数错误,缺少机箱编号.");
			return result;
		}
		if(null == boxObj.get("nfcNumber")) {
			result.put("msg", "修改机箱参数错误,缺少NFC序列号.");
			return result;
		}
		try {
			boxService.updateBox(UserTool.getLoginUser(request).get("user_name"), boxObj);
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
	public Map<String, String> deleteBox(HttpServletRequest request) {		
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		Map boxObj = ControllerUtils.toMap(request);
		
		if(null == boxObj.get("id")) {
			result.put("msg", "删除机箱错误,缺少机箱ID.");
			return result;
		}
		try {
			boxService.deleteBox(UserTool.getLoginUser(request).get("user_name"), boxObj.get("id").toString());
		}catch(BusinessException be) {
			result.put("msg", be.getMessage());
			return result;
		}
		
		result.put("returnCode", "success");
		result.put("msg", "");
		return result;
	}
}
