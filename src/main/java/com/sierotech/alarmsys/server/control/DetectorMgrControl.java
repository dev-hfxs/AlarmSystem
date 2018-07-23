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
import com.sierotech.alarmsys.server.service.IDetectorService;

/*
 * 
 * 探测器维护相关请求
 */
@Controller
@RequestMapping("/detector/mgr/")
@Scope("request")
public class DetectorMgrControl{
	@Autowired
	private IDetectorService detectorService;
	
	@RequestMapping(value = "/add")
	@ResponseBody
	public Map<String, String> addDetector(HttpServletRequest request) {	
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		Map detectorObj = ControllerUtils.toMap(request);
		if(null == detectorObj.get("detectorSeq")) {
			result.put("msg", "添加探测器错误, 缺少探测器编号.");
			return result;
		}
		if(null == detectorObj.get("nfcNumber")) {
			result.put("msg", "添加探测器错误, 缺少NFC序列号.");
			return result;
		}
		
		try {
			detectorService.addDetector(UserTool.getLoginUser(request).get("user_name"), detectorObj);
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
	public Map<String, String> updateDetector(HttpServletRequest request) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		Map detectorObj = ControllerUtils.toMap(request);
		if(null == detectorObj.get("detectorId")) {
			result.put("msg", "修改探测器错误, 缺少探测器ID.");
			return result;
		}
		if(null == detectorObj.get("detectorSeq")) {
			result.put("msg", "修改探测器错误, 缺少探测器编号.");
			return result;
		}
		if(null == detectorObj.get("nfcNumber")) {
			result.put("msg", "修改探测器错误, 缺少NFC序列号.");
			return result;
		}
		try {
			detectorService.updateDetector(UserTool.getLoginUser(request).get("user_name"), detectorObj);
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
	public Map<String, String> deleteDetector(HttpServletRequest request) {		
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		
		String detectorId = request.getParameter("id");
		
		if(null == detectorId) {
			result.put("msg", "删除探测器错误, 缺少探测器ID.");
			return result;
		}
		try {
			detectorService.deleteDetector(UserTool.getLoginUser(request).get("user_name"), detectorId);
		}catch(BusinessException be) {
			result.put("msg", be.getMessage());
			return result;
		}
		
		result.put("returnCode", "success");
		result.put("msg", "");
		return result;
	}
}
