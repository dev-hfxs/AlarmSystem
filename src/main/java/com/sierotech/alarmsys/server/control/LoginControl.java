package com.sierotech.alarmsys.server.control;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    	loginService.doLogin("","");
    	
		result.put("returnCode", "0");
//		result.put("loginRetCode", loginUser.getLoginStatus());
		return result;
    }
}
