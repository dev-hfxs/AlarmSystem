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
import com.sierotech.alarmsys.common.utils.LogOperationUtil;
import com.sierotech.alarmsys.server.service.ILoginService;

/*
 * 
 * 处理登录相关请求
 */
@Controller
@RequestMapping("/auth")
@Scope("request")
public class LoginControl{
	@Autowired
	private ILoginService loginService;
	
	@RequestMapping(value = "/login")
    @ResponseBody
    public Map<String,String> login(HttpServletRequest request) {
        Map<String,String> result = new HashMap<String,String>();
    	
    	String userName = request.getParameter("userName");
    	String password = request.getParameter("password");
    	
    	if (null == userName) {
			result.put("msg", "用户登录参数错误,缺少用户名.");
			return result;
		}
    	
    	if (null == password) {
			result.put("msg", "用户登录参数错误,缺少登录密码.");
			return result;
		}
		Map<String, Object> loginUser = null;
		try {
			loginUser = loginService.doLogin(userName, password);
		} catch (BusinessException be) {
			result.put("msg", be.getMessage());
			return result;
		}
		// 用户信息放入会话信息中
		if (loginUser != null) {
			//先清除会话信息
			request.getSession().invalidate();
			
			
			String clientIp = getIpAddr(request);
			LogOperationUtil.logSession(request.getSession().getId(), userName, clientIp, "");
			request.getSession().setAttribute("loginUser", loginUser);
			result.put("loginUser", loginUser.toString());
			result.put("returnCode", "success");
		} else {
			result.put("msg", "登录失败.");
		}

		return result;
    }
	
	@RequestMapping(value = "/logout")
	@ResponseBody
	public String logout(HttpServletRequest request) {
		try {
			String returnPage = "/system/login/login.jsp";
			request.getSession().invalidate();
			return returnPage;
		} catch (Exception ex) {
		}
		return null;
	}
	
	 private String getIpAddr(HttpServletRequest request){  
	        String ipAddress = request.getHeader("x-forwarded-for");  
	            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
	                ipAddress = request.getHeader("Proxy-Client-IP");  
	            }  
	            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
	                ipAddress = request.getHeader("WL-Proxy-Client-IP");  
	            }  
	            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
	                ipAddress = request.getRemoteAddr();  
	                if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){  
	                    //根据网卡取本机配置的IP  
	                    InetAddress inet=null;  
	                    try {  
	                        inet = InetAddress.getLocalHost();  
	                    } catch (UnknownHostException e) {  
	                        e.printStackTrace();  
	                    }  
	                    ipAddress= inet.getHostAddress();  
	                }  
	            }  
	            //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割  
	            if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15  
	                if(ipAddress.indexOf(",")>0){  
	                    ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));  
	                }  
	            }  
	            return ipAddress; 
	    }
}
