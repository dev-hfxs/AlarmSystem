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

import com.sierotech.alarmsys.common.utils.ControllerUtils;
import com.sierotech.alarmsys.server.service.CommonService;


/**
* @JDK版本: 1.7
* @创建人: lwm
* @创建日期：2018年4月17日
* @功能描述: 
 */
@Controller
@RequestMapping({"/comm/"})
public class CommonControl {
	private static final Logger log = LoggerFactory.getLogger(CommonControl.class);
	
	@Autowired
	private CommonService commSrv;
	
	
	@RequestMapping({"/queryForList"})
	@ResponseBody
	public List<Map<String, Object>> queryForList(HttpServletRequest request) {
		List<Map<String, Object>> result = null;
		try {
	    	String sqlId = request.getParameter("sqlId");
	    	Map paramMap = ControllerUtils.toMap(request);
	    	result = this.commSrv.queryForList(sqlId,paramMap);
	    } catch (Exception ex) {
	    	log.error("queryForList error",ex);
	    }
	    return result;
	}
	
	@RequestMapping({"/queryForPage"})
	@ResponseBody
	public Map<String, Object> queryForPage(HttpServletRequest request) {
		Map<String, Object> result = null;
		try {
	    	Map paramMap = ControllerUtils.toMap(request);
	    	result = this.commSrv.queryForPage(paramMap);
	    } catch (Exception ex) {
	    	log.error("queryForPage error",ex);
	    }
		return result;
	}
}
