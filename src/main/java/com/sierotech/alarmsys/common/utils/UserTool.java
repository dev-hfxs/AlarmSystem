/**
* <p>版权所有:(C)2018-2022 天津航峰希萨科技有限公司 </p>
* @创建人: lwm
* @创建日期: 2018年4月17日
* @修改人: 
* @修改日期：
* @描述: 
 */
package com.sierotech.alarmsys.common.utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @JDK版本: 1.7
 * @创建人: lwm
 * @创建日期：2018年4月17日 
 * @功能描述: 前端用户对象工具类
 */
public class UserTool {
	public static Map<String, String> getLoginUser(HttpServletRequest request) {
		Map<String, String> loginUser;
		if(request.getSession().getAttribute("loginUser") == null) {
			loginUser = new HashMap<String,String>();
		}else {
			loginUser = (Map<String, String>) request.getSession().getAttribute("loginUser");
		}
		
		return loginUser;
	}
}
