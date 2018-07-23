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
 * @功能描述: 转换requst的参数
 */
public class ControllerUtils {
	public static Map<String, String> toMap(HttpServletRequest request) {
		Map<String, String[]> paramMap = request.getParameterMap();
		Map newMap = new HashMap();
		for (Map.Entry e : paramMap.entrySet()) {
			String key = (String) e.getKey();
			String[] value = (String[]) e.getValue();
			String valueStr = "";
			if (value[0] != null) {
				if (value.length == 1) {
					valueStr = value[0];
				} else {
					valueStr = valueStr + value[0];
					for (int i = 1; i < value.length; i++) {
						valueStr = valueStr + "," + value[i];
					}
				}
				newMap.put(key, valueStr);
			}
		}
		return newMap;
	}
}
