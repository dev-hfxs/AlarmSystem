/**
* <p>版权所有:(C)2018-2022 天津航峰希萨科技有限公司 </p>
* @创建人: lwm
* @创建日期: 2018年7月27日
* @修改人: 
* @修改日期：
* @描述: 
 */
package com.sierotech.alarmsys.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sierotech.alarmsys.common.utils.BaseDataInitializor;
import com.sierotech.alarmsys.common.utils.ConfigFactory;
import com.sierotech.alarmsys.common.utils.ConfigSQLUtil;
import com.sierotech.alarmsys.common.utils.JsonUtil;
import com.sierotech.alarmsys.common.utils.UUIDGenerator;
import com.sierotech.alarmsys.common.utils.spring.SpringContextUtil;
import com.sierotech.alarmsys.context.ProcessContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
* @JDK版本: 1.7
* @创建人: lwm
* @创建日期：2018年7月27日
* @功能描述: 
 */
public class AlarmMessageHandlerTask implements Runnable{
	static final Logger log = LoggerFactory.getLogger(AlarmMessageHandlerTask.class);
	
	private JdbcTemplate jdbcTemplate;
	private String gateway; 
	
	public AlarmMessageHandlerTask() {
		this.gateway = ConfigFactory.getPropertyConfig("alarm.properties").getString("gateway");
		this.jdbcTemplate = (JdbcTemplate) SpringContextUtil.getBean("SpringJdbcTemplate");
	}
	
	 public boolean localNetIsConnect(){
	    	boolean connect = false;
	    	Runtime runtime = Runtime.getRuntime();
	    	Process process;
			try {
				process = runtime.exec("ping " + this.gateway);
				InputStream is = process.getInputStream(); 
		        InputStreamReader isr = new InputStreamReader(is); 
		        BufferedReader br = new BufferedReader(isr); 
		        String line = null; 
		        StringBuffer sb = new StringBuffer();
		        int i=0;
		        while ((line = br.readLine()) != null) {
		        	if(line != null && line.indexOf("TTL") > 0) {
		            	connect = true;
		            	break;
		            }
		        }
		        is.close();
		        isr.close();
		        br.close();	 
			} catch (IOException e) {
				//e.printStackTrace();
			} 
	        return connect;
	}
	 
	@Override
	public void run() {
		String addAlarmMessagePreSql = ConfigSQLUtil.getCacheSql("alarm-alarminfo-add");
		// 解析报警消息
		while(true) {
			String message = ProcessContext.popAlarmMessage();
			if(message == null) {
				try {
					//没有消息时, 休眠一下
					Thread.sleep(500);
				} catch (InterruptedException e) {
					//
				}
			}else {
				//处理消息
//				System.out.println(message); 
				try {
					Map<String, String> msgMap = JsonUtil.jsonToMap(message, false);
					
					String deviceType = msgMap.get("deviceType");
					String nfcNumber = msgMap.get("nfcNumber");
					String posInfo = msgMap.get("posInfo");
					
					Map<String, String> paramsMap = new HashMap<String, String>();
					paramsMap.put("id", UUIDGenerator.getUUID());
					paramsMap.put("nfcNumber", nfcNumber);
					paramsMap.put("status", "N");
					if(posInfo != null) {
						Map<String, String> posMap = JsonUtil.jsonToMap(posInfo, false);
						paramsMap.put("longitude", posMap.get("longitude"));
						paramsMap.put("latitude", posMap.get("latitude"));
						paramsMap.put("posDesc", posMap.get("pos_desc"));						
					}else {
						paramsMap.put("longitude", "null");
						paramsMap.put("latitude", "null");
						paramsMap.put("posDesc", "null");
					}
					paramsMap.put("alarmDate", msgMap.get("alarmDate"));
					paramsMap.put("alarmDesc", msgMap.get("alarmDesc"));
					paramsMap.put("deviceInfo", "");
					
//					System.out.println("deviceType："+deviceType);
					
					if("processor".equals(deviceType)) {
						// 处理器不能连接报警
						// 判断是不是服务器网络问题
						if(localNetIsConnect()) {
							//
//							System.out.println("处理器连接状态"+localNetIsConnect());
						}else {
							//服务器自身网络不可用，不报警
							continue;
						}
						paramsMap.put("deviceType", "P");
						paramsMap.put("alarmType", "1");
						paramsMap.put("alarmDesc", msgMap.get("alarmDesc"));
					}else if("detector".equals(deviceType)) {

						// 探测器报警
						paramsMap.put("deviceType", "D");
						
						String alarmType = msgMap.get("alarmType").toString();
				
						if("offLine".equals(alarmType)) {
							//不在线报警
							paramsMap.put("alarmType", "1");
						}else {
							String isLead = msgMap.get("isLead");
							if("true".equals(isLead)) {
								//
							}else {
								continue;
							}
							//感知报警
							paramsMap.put("alarmType", "2");
						}
						paramsMap.put("alarmDesc", msgMap.get("alarmDesc"));
					 }
					 // 存储报警消息
					String addAlarmMessageSql = ConfigSQLUtil.preProcessSQL(addAlarmMessagePreSql, paramsMap);
					try {
						jdbcTemplate.update(addAlarmMessageSql);
					} catch (DataAccessException dae) {
						log.info("sql:{}",addAlarmMessageSql);
						log.info("报警消息入库错误.消息内容:{}", JsonUtil.mapToJson(paramsMap));
					}		
				}catch(Exception e) {
					//错误的消息格式,不处理
				}
			}
		}
	}
}
