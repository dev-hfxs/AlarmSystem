import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* <p>版权所有:(C)2018-2022 天津航峰希萨科技有限公司 </p>
* @创建人: lwm
* @创建日期: 2018年7月14日
* @修改人: 
* @修改日期：
* @描述: 
 */

/**
* @JDK版本: 1.7
* @创建人: lwm
* @创建日期：2018年7月14日
* @功能描述: 
 */
public class TestTree {
	public static void main(String[] args) {
		List<Map<String, Object>>  orgs = new ArrayList<Map<String, Object>>();
		Map<String, Object> data1 = new HashMap<String, Object>();
		data1.put("id", "init_001");
		data1.put("parent_id", "ROOT");
		data1.put("org_name", "北京");
		orgs.add(data1);
		
		Map<String, Object> data2 = new HashMap<String, Object>();
		data2.put("id", "init_002");
		data2.put("parent_id", "ROOT");
		data2.put("org_name", "湖南");
		orgs.add(data2);
		
		Map<String, Object> data3 = new HashMap<String, Object>();
		data3.put("id", "init_021");
		data3.put("parent_id", "init_001");
		data3.put("org_name", "海淀");
		orgs.add(data3);
		
		Map<String, Object> data4 = new HashMap<String, Object>();
		data4.put("id", "init_022");
		data4.put("parent_id", "init_001");
		data4.put("org_name", "东城");
		orgs.add(data4);
		
		Map<String, Object> data5 = new HashMap<String, Object>();
		data5.put("id", "init_031");
		data5.put("parent_id", "init_002");
		data5.put("org_name", "怀化");
		orgs.add(data5);
		
		Map<String, Object> data6 = new HashMap<String, Object>();
		data6.put("id", "init_053");
		data6.put("parent_id", "init_022");
		data6.put("org_name", "东直门");
		orgs.add(data6);
		
		List<Map<String, Object>> orgTreeData = groupData4Tree(orgs,"init_001");
		
		System.out.println(orgTreeData.toString());
	}
	
	private static List<Map<String, Object>> groupData4Tree(List<Map<String, Object>> srcData ,String parentId){
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		if(srcData != null && srcData.size() > 0) {
			//遍历得到直接子级元素
			for(Map<String, Object> item : srcData) {
				if(parentId.equals(item.get("parent_id").toString())) {
					result.add(item);
				}
			}
			if(result.size() > 0 ) {
				//遍历得到直接子级元素的子级元素
				for(Map<String, Object> item : result) {
					String curId = item.get("id").toString();
					List<Map<String, Object>> curChildren = groupData4Tree(srcData, curId);
					item.put("children", curChildren);
				}
			}
		}
		return result;
	}
}
