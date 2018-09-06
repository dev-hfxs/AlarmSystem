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

import com.sierotech.alarmsys.common.utils.BaseDataInitializor;
import com.sierotech.alarmsys.context.ProcessContext;
import com.sierotech.alarmsys.server.service.IOrgService;

/*
 * 
 * 缓存相关请求
 */
@Controller
@RequestMapping("/cache/mgr/")
@Scope("request")
public class CacheMgrControl{
	@Autowired
	private BaseDataInitializor baseDataInit;
	
	@RequestMapping(value = "/refresh")
    @ResponseBody
    public Map<String,Object> refreshCache(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        //重新加载设备信息到缓存中
        baseDataInit.run();
		result.put("returnCode", "success");
		return result;
    }
	
	@RequestMapping(value = "/getDeviceCamera")
    @ResponseBody
    public Map<String,Object> getDeviceCamera(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        String nfcNumber = request.getParameter("nfcNumber");
        if(nfcNumber == null || "".equals(nfcNumber)) {
        	result.put("returnCode", "fail");
        	result.put("msg", "设备信息不存在.");
        	return result;
        }
        Map<String, Object> cameraDeploy = ProcessContext.getCameraDeploy(nfcNumber);
        if(cameraDeploy !=null) {
        	result.put("returnCode", "success");
        	result.put("data", cameraDeploy);
        	result.put("msg", "");
        }else {
        	result.put("returnCode", "fail");
        	result.put("msg", "设备信息不存在.");
        }        
		return result;
    }
}
