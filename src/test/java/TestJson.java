import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sierotech.alarmsys.common.utils.JsonUtil;

/**
* <p>版权所有:(C)2018-2022 天津航峰希萨科技有限公司 </p>
* @创建人: lwm
* @创建日期: 2018年7月18日
* @修改人: 
* @修改日期：
* @描述: 
 */

/**
* @JDK版本: 1.7
* @创建人: lwm
* @创建日期：2018年7月18日
* @功能描述: 
 */
public class TestJson {
	public static void main(String[] args) {
		String strJson = "[{'id':'a1','action':'A'},{'id':'a2','action':'D'},{'id':'f9','action':'A'}]";
		List<Map<String,String>> alAllotFunc = JsonUtil.jsonToList(strJson, HashMap.class);
		System.out.println(alAllotFunc.get(0).get("action").toString());
		System.out.println(alAllotFunc.toString());
	}
}
