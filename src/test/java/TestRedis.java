import java.util.HashMap;
import java.util.Map;

import com.sierotech.alarmsys.common.utils.JedisUtil;
import com.sierotech.alarmsys.common.utils.JsonUtil;

import redis.clients.jedis.Jedis;

/**
* <p>版权所有:(C)2018-2022 天津航峰希萨科技有限公司 </p>
* @创建人: lwm
* @创建日期: 2018年6月15日
* @修改人: 
* @修改日期：
* @描述: 
 */

/**
* @JDK版本: 1.7
* @创建人: lwm
* @创建日期：2018年6月15日
* @功能描述: 
 */
public class TestRedis {
	 public static void main(String[] args) {
		 
		 JedisUtil.poolInit("localhost", 6379);
		 
		 
			 Jedis jedis = JedisUtil.getJedis();
			 if(jedis == null) {
				 System.out.println("不能连接redis");
			 }
//			 System.out.println(jedis.get("userName"));
			 Map<String,String> data2 = new HashMap<String,String>();
			 data2.put("a", "阿里");
			 data2.put("b", "北斗");
			 
			 String msg = JsonUtil.mapToJson(data2);
//			 jedis.hmset("alarmList", data2);
//			 jedis.hmget("alarmList");
			 
			 jedis.lpush("alarmList", msg);
			 jedis.lpush("alarmList", msg);
			 
			 
			 System.out.println(jedis.llen("alarmList"));
			 String data = jedis.lpop("alarmList");
			 
			 System.out.println(data);
			 
			 System.out.println(jedis.llen("alarmList"));
			 
			 
			 JedisUtil.releaseJedis(jedis);
		 
	        //连接本地的 Redis 服务
//	        Jedis jedis = new Jedis("127.0.0.1");
//		 Jedis jedis = new Jedis("192.168.1.116");
//		 
//		 try {
//			 String isConn = jedis.ping();
//			 System.out.println(isConn);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////			System.out.println("未连接.");
//		}
		
		 
//		 System.out.println("连接成功");
//	        jedis.select(0);
//	        //设置 redis 字符串数据
////	        jedis.set("userName", "liweiming6");
//	        // 获取存储的数据并输出
//	        System.out.println("redis 存储的字符串为: "+ jedis.get("userName"));
	    }
}
