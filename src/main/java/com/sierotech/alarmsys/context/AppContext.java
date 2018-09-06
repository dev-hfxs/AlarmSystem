/**
* <p>版权所有:(C)2018-2022 天津航峰希萨科技有限公司 </p>
* @创建人: lwm
* @创建日期: 2018年4月15日
* @修改人: 
* @修改日期：
* @描述: 文件简要描述
 */
package com.sierotech.alarmsys.context;


import com.sierotech.alarmsys.common.utils.ConfigFactory;


/**
* @JDK版本: 1.7
* @创建人: lwm
* @创建日期：2018年4月15日
* @功能描述: 系统应用上下文,方便获取系统应用加载的配置信息
 */

public class AppContext {
	private static String alarmImageDir = ConfigFactory.getPropertyConfig("alarm.properties").getString("upload.alarmimage.dir");
	
	public static String getAlarmImageDir() {
		return alarmImageDir;
	}
	
}
