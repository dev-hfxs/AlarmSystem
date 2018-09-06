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
@RequestMapping({"/report/"})
public class ReportControl {
	private static final Logger log = LoggerFactory.getLogger(ReportControl.class);
	
	@Autowired
	private IAlarmInfoService alarmSrv;
	
	
	@RequestMapping({"/getAreaAlarmCount"})
	@ResponseBody
	public Map<String, String> getAreaAlarmCount(HttpServletRequest request) {
		return alarmSrv.getAlarms4Area();
	}
	
	@RequestMapping({"/getMonthAlarmCount"})
	@ResponseBody
	public List<String> getMonthAlarmCount(HttpServletRequest request) {
		return alarmSrv.getAlarms4Month();
	}
}
