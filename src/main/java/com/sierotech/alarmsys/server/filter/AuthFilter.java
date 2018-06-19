/**
* <p>版权所有:(C)2018-2022 天津航峰希萨科技有限公司 </p>
* @创建人: lwm
* @创建日期: 2018年4月16日
* @修改人: 
* @修改日期：
* @描述: 
 */
package com.sierotech.alarmsys.server.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @JDK版本: 1.7
 * @创建人: lwm
 * @创建日期：2018年4月16日 
 * @功能描述: 拦截未登录的用户直接访问
 */
public class AuthFilter implements Filter {
	private List<String> exceptPaths = new ArrayList<String>();

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String ctxPath = req.getContextPath();
		Map loginUser = (Map) req.getSession().getAttribute("loginUser");

		response.setContentType("text/html; charset=UTF-8");
		
		if (loginUser == null) {
			if (isExtendPath(ctxPath, req.getRequestURI())) {
				filterChain.doFilter(request, response);
			} else {
				// 无回话信息
				String alertStr = "用户未登录或登录超时，请登录!";
				StringBuilder outStr = new StringBuilder();
				String indexUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort()
						+ req.getContextPath();
				// 如果还未访问系统，直接输入的是jsp链接访问，则top.Dialog是获取不到的，直接用alert
				outStr.append("<script>if(top.Dialog){top.Dialog.alert('" + alertStr + "',function(){"
						+ "top.window.location.href=\"" + indexUrl + "\";});}else{alert('" + alertStr
						+ "');top.window.location.href=\"" + indexUrl + "\"; }</script>");
				
				response.setContentType("text/html;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.write(outStr.toString());
				out.flush();
				out.close();
			}
		} else {
			filterChain.doFilter(request, response);
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		String loginUrl = "";
		if (filterConfig.getInitParameter("loginUrl") != null) {
			loginUrl = filterConfig.getInitParameter("loginUrl");
		}
		if (filterConfig.getInitParameter("_except_urlpattern") != null) {
			String[] exceptUrl = filterConfig.getInitParameter("_except_urlpattern").trim().split(",");
			if (exceptUrl != null) {
				for (int index = 0; index < exceptUrl.length; ++index) {
					this.exceptPaths.add(exceptUrl[index].trim());
				}
			}
		}
	}

	public void destroy() {

	}

	private boolean isExtendPath(String ctxPath, String path) {
		boolean result = false;
		String ctxPath2 = ctxPath + "/";// ctxPath例如/uas，请求的路径有可能为/uas/
		if (ctxPath2.indexOf(path) >= 0) {
			return true;
		}
		if (path != null)
			if (path.endsWith(".js")) {
				return true;
			} else if (path.endsWith(".png") || path.endsWith(".jpg")) {
				return true;
			} else if (path.endsWith(".css")) {
				return true;
			}

		for (int i = 0; i < exceptPaths.size(); i++) {
			// String url = ctxPath + exceptPaths.get(i);
			String url = exceptPaths.get(i);
			if (path.indexOf(url) >= 0) {
				result = true;
				break;
			}
		}
		return result;
	}
}
