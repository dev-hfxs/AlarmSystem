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
import com.sierotech.alarmsys.common.utils.UserTool;
import com.sierotech.alarmsys.server.service.IProcessorService;

/*
 * 
 * 处理器维护相关请求
 */
@Controller
@RequestMapping("/processor/mgr/")
@Scope("request")
public class ProcessorMgrControl{
	@Autowired
	private IProcessorService processorService;
	
	
	
	@RequestMapping(value = "/add")
	@ResponseBody
	public Map<String, String> addProcessor(HttpServletRequest request) {	
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		Map processorObj = ControllerUtils.toMap(request);
		if(null == processorObj.get("moxaNumber")) {
			result.put("msg", "添加处理器错误, 缺少moxa序列号.");
			return result;
		}
		if(null == processorObj.get("nfcNumber")) {
			result.put("msg", "添加处理器错误, 缺少NFC序列号.");
			return result;
		}
		
		try {
			processorService.addProcessor(UserTool.getLoginUser(request).get("user_name"), processorObj);
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
	public Map<String, String> updateProcessor(HttpServletRequest request) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		Map processorObj = ControllerUtils.toMap(request);
		if(null == processorObj.get("moxaNumber")) {
			result.put("msg", "修改处理器错误, 缺少moxa序列号.");
			return result;
		}
		if(null == processorObj.get("nfcNumber")) {
			result.put("msg", "修改处理器错误, 缺少NFC序列号.");
			return result;
		}
		try {
			processorService.updateProcessor(UserTool.getLoginUser(request).get("user_name"), processorObj);
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
	public Map<String, String> deleteProcessor(HttpServletRequest request) {		
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		
		String processorId = request.getParameter("id");
		
		if(null == processorId) {
			result.put("msg", "删除处理器错误, 缺少处理器ID.");
			return result;
		}
		try {
			processorService.deleteProcessor(UserTool.getLoginUser(request).get("user_name"), processorId);
		}catch(BusinessException be) {
			result.put("msg", be.getMessage());
			return result;
		}
		
		result.put("returnCode", "success");
		result.put("msg", "");
		return result;
	}
}
