import com.sierotech.alarmsys.common.utils.JedisUtil;

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
		 
		 JedisUtil.poolInit("192.168.1.117", 6379);
		 
		 for(int i=0; i< 100; i++) {
			 Jedis jedis = JedisUtil.getJedis();
			 if(jedis == null) {
				 System.out.println("不能连接redis");
			 }
//			 String uName = jedis.get("userName");
//			 System.out.println("i:"+i + " ; v:" +uName);
//			 JedisUtil.releaseJedis(jedis);
		 }
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
