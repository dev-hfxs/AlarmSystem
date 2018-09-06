/**
* <p>版权所有:(C)2018-2022 天津航峰希萨科技有限公司 </p>
* @创建人: lwm
* @创建日期: 2018年4月17日
* @修改人: 
* @修改日期：
* @描述: 
 */
package com.sierotech.alarmsys.server.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sierotech.alarmsys.common.BusinessException;
import com.sierotech.alarmsys.common.utils.ControllerUtils;
import com.sierotech.alarmsys.common.utils.UserTool;
import com.sierotech.alarmsys.context.ProcessContext;
import com.sierotech.alarmsys.server.service.IAlarmInfoService;


/**
* @JDK版本: 1.7
* @创建人: lwm
* @创建日期：2018年4月17日
* @功能描述: 
 */
@Controller
@RequestMapping({"/alarm/"})
public class AlarmControl {
	private static final Logger log = LoggerFactory.getLogger(AlarmControl.class);
	
	@Autowired
	private IAlarmInfoService alarmSrv;
	
	
	@RequestMapping({"/getNewAlarms"})
	@ResponseBody
	public List<Map<String, Object>> getNewAlarms(HttpServletRequest request) {
		List<Map<String, Object>> result = null;
		result = alarmSrv.getNewAlarm4Observer();
	    return result;
	}
	
	@RequestMapping({"/getAlarms4Observer"})
	@ResponseBody
	public List<Map<String, Object>> getAlarms4Observer(HttpServletRequest request) {
		List<Map<String, Object>> result = null;
		result = alarmSrv.getAlarm4Observer();
	    return result;
	}
	
	@RequestMapping({"/getUserUnProcessAlarms"})
	@ResponseBody
	public List<Map<String, Object>> getUserUnProcessAlarms(HttpServletRequest request) {
		List<Map<String, Object>> result = null;
		
		String lastDate = request.getParameter("lastDate");
		
		if(lastDate != null && !"".equals(lastDate)) {
			result = alarmSrv.getUserUnProcessAlarms(UserTool.getLoginUser(request).get("id"), lastDate);
		}else {
			result = alarmSrv.getUserUnProcessAlarms(UserTool.getLoginUser(request).get("id"), null);
		}
		if(result !=null && result.size() > 0) {
			lastDate = result.get(result.size() - 1).get("alarm_date").toString();
			request.getSession().setAttribute("lastDate", lastDate);
		}
	    return result;
	}
	
	@RequestMapping({"/mgr/confirm"})
	@ResponseBody
	public Map<String, String> confirm(HttpServletRequest request) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		Map alarmObj = ControllerUtils.toMap(request);
		if(null == alarmObj.get("alarmId")) {
			result.put("msg", "确认报警请求错误, 缺少报警ID参数.");
			return result;
		}
		if(null == alarmObj.get("isValid")) {
			result.put("msg", "确认报警请求错误, 缺少是否有效参数.");
			return result;
		}
		
		if("Y".equals(alarmObj.get("isValid"))) {
			if(alarmObj.get("processPerson") == null) {
				result.put("msg", "确认报警请求错误, 未选择处警用户.");
			}
		}
		
		if("N".equals(alarmObj.get("isValid"))) {
			if(alarmObj.get("remark") == null) {
				result.put("msg", "确认报警请求错误, 未填写备注.");
			}
		}
		
		try {
			alarmSrv.updateAlarm4Confirm(UserTool.getLoginUser(request).get("id"), alarmObj);
		}catch(BusinessException be) {
			result.put("msg", be.getMessage());
			return result;
		}
		
		result.put("returnCode", "success");
		result.put("msg", "");
		return result;
	}
	
	@RequestMapping({"/mgr/reconfirm"})
	@ResponseBody
	public Map<String, String> reconfirm(HttpServletRequest request) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		Map alarmObj = ControllerUtils.toMap(request);
		if(null == alarmObj.get("alarmId")) {
			result.put("msg", "重新确认报警请求错误, 缺少报警ID参数.");
			return result;
		}
		if(null == alarmObj.get("isValid")) {
			result.put("msg", "重新确认报警请求错误, 缺少是否有效参数.");
			return result;
		}
		
		if("Y".equals(alarmObj.get("isValid"))) {
			if(alarmObj.get("processPerson") == null) {
				result.put("msg", "重新确认报警请求错误, 未选择处警用户.");
			}
		}
		
		if("N".equals(alarmObj.get("isValid"))) {
			if(alarmObj.get("remark") == null) {
				result.put("msg", "重新确认报警请求错误, 未填写备注.");
			}
		}
		
		try {
			alarmSrv.updateAlarm4Reconfirm(UserTool.getLoginUser(request).get("id"), alarmObj);
		}catch(BusinessException be) {
			result.put("msg", be.getMessage());
			return result;
		}
		
		result.put("returnCode", "success");
		result.put("msg", "");
		return result;
	}
	
	@RequestMapping({"/mgr/process"})
	@ResponseBody
	public Map<String, String> process(HttpServletRequest request) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		Map alarmObj = ControllerUtils.toMap(request);
		if(null == alarmObj.get("alarmId")) {
			result.put("msg", "处理报警请求错误, 缺少报警ID参数.");
			return result;
		}
		if(null == alarmObj.get("processMethod")) {
			result.put("msg", "处理报警请求错误, 处理方法不能为空.");
			return result;
		}
		if(null == alarmObj.get("processResult")) {
			result.put("msg", "处理报警请求错误, 处理结果不能为空.");
			return result;
		}
		
		try {
			alarmSrv.updateAlarm4Process(UserTool.getLoginUser(request).get("id"), alarmObj);
		}catch(BusinessException be) {
			result.put("msg", be.getMessage());
			return result;
		}
		
		result.put("returnCode", "success");
		result.put("msg", "");
		return result;
	}
	
	@RequestMapping({"/mgr/recoverValid"})
	@ResponseBody
	public Map<String, String> recoverValid(HttpServletRequest request) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("returnCode", "fail");
		Map alarmObj = ControllerUtils.toMap(request);
		if(null == alarmObj.get("alarmId")) {
			result.put("msg", "恢复报警状态为有效请求错误, 缺少报警ID参数.");
			return result;
		}
		
		try {
			alarmSrv.updateAlarm4RecoverValid(UserTool.getLoginUser(request).get("id"), alarmObj);
		}catch(BusinessException be) {
			result.put("msg", be.getMessage());
			return result;
		}
		
		result.put("returnCode", "success");
		result.put("msg", "");
		return result;
	}
}
