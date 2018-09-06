/**
* <p>版权所有:(C)2018-2022 天津航峰希萨科技有限公司 </p>
* @创建人: lwm
* @创建日期: 2018年7月26日
* @修改人: 
* @修改日期：
* @描述: 
 */
package com.sierotech.alarmsys.monitor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sierotech.alarmsys.camera.CameraHandle;
import com.sierotech.alarmsys.common.utils.ConfigSQLUtil;
import com.sierotech.alarmsys.common.utils.DateUtils;
import com.sierotech.alarmsys.common.utils.JsonUtil;
import com.sierotech.alarmsys.common.utils.spring.SpringContextUtil;
import com.sierotech.alarmsys.context.ProcessContext;

/**
* @JDK版本: 1.7
* @创建人: lwm
* @创建日期：2018年7月26日
* @功能描述: 
 */
public class ProcessorMonitor implements Runnable{
	static final Logger log = LoggerFactory.getLogger(ProcessorMonitor.class);
	private String processorId;
	
	// 处理器IP
	private String host;
	
	// 处理器端口
	private int port;
	
	// 处理器NFC序列号
	private String processorNFCNum;
	
	// 处理器包含的探测器数
	private int detectorNums;
	
	// 处理器位置信息
	private String posInfo;
	
	private int detectorEndIndex;
	
	private boolean quit = false;
	
	private Socket socket;
	private OutputStream os;
	private InputStream in;
	
	/**
	 * @param host
	 * @param port
	 */
	public ProcessorMonitor(String host, int port, int detectorNums, String posInfo, String nfcNumber, String processorId) {
		super();
		this.host = host;
		this.port = port;
		this.detectorNums = detectorNums;
		this.posInfo = posInfo;
		this.detectorEndIndex = detectorNums + 10;
		this.processorNFCNum = nfcNumber;
		this.processorId = processorId;
	}
	
	
	public boolean connect() {
		boolean isConnect = true;
		try {
			this.socket = new Socket(host, port);
		}catch (UnknownHostException e) {
			isConnect = false;
        }catch (IOException e) {
        	isConnect = false;
		}
		return isConnect;
	}
	
	public void quit() {
		this.quit = true;
	}
	
	@Override
	public void run() {
		//问询处理器的报文
		byte[] sendData = {(byte)0x2A, (byte)0x01, (byte)0x07, (byte)0x01, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x53,(byte)0xb5, (byte)0x23};
        byte offLineStatus = (byte)0xFF;
        log.info("开始监听处理器：{}的状态.",this.host);
		try {
			os = socket.getOutputStream();
			in = socket.getInputStream();
			
			while(this.quit == false) {
					os.write(sendData);
					os.flush();
					
					int readSize = 512;
					int responseDataLen = 0;
					byte[] responseData = new byte[268];
					byte[] readData = new byte[readSize];
					int readLen = in.read(readData);
					try {
						System.arraycopy(readData, 0, responseData, responseDataLen, readLen);
					}catch (Exception e){
						
					}
					responseDataLen = responseDataLen + readLen;
					int reNums = 1;
					while(responseDataLen < 268 && in.available() > 0 ) {
						//小于268字节，未读完整的数据
						log.info(" receive processor host:{}, port:{} response data. receive num:{}", host, port, reNums);
						reNums++;
						byte[] reReadData = new byte[readSize];
						int reReadLen = in.read(reReadData);
						try {
							System.arraycopy(readData, 0, responseData, responseDataLen, readLen);
						}catch (Exception e){
							
						}
						responseDataLen = responseDataLen + reReadLen;
					}
					if(responseDataLen < 268) {
						//响应报文数据不完整,不处理
					}else {
						int curDetectorAlarmIndex = -1;
						int nearNum = 0;
						
						//分析报文,检查探测器是否在线、是否报警
						for(int i = 11; i <= detectorEndIndex; i++) {
                			byte b = responseData[i];
                			
                			if(offLineStatus == b) {
                				//探测器不在线
                				//int detectorIndex = i - 11;
                				int detectorIndex = i - 10;
                				
                				//获取探测器信息
                				String keyPos = "pos_"  + this.processorNFCNum + "_" + detectorIndex;
                				String keyNfc = "nfc_"  + this.processorNFCNum + "_" + detectorIndex;
                				
                				String detectorPos = ProcessContext.getStringValue(keyPos);
                				String detectorNfc = ProcessContext.getStringValue(keyNfc);
                				
                				Map<String,String> alarmMap = new HashMap<String, String>();
                	        	alarmMap.put("alarmDate", DateUtils.getNow(DateUtils.FORMAT_LONG));
                				alarmMap.put("deviceType", "detector");
                				alarmMap.put("nfcNumber", detectorNfc);
                				alarmMap.put("detectorSeq", "" + detectorIndex);
                				alarmMap.put("posInfo", detectorPos);
                				alarmMap.put("alarmType", "offLine");
                				
                				String keySeq = "seq_"  + this.processorNFCNum + "_" + detectorIndex;
                				String detectorSeq = ProcessContext.getStringValue(keySeq);
                				
                				//int endDetectorOrderNum = this.detectorNums - 1;
                				int endDetectorOrderNum = this.detectorNums;
                				String keyEndSeq = "seq_"  + this.processorNFCNum + "_" + endDetectorOrderNum;
                				String detectorEndSeq = ProcessContext.getStringValue(keyEndSeq);
                				//获取关联摄像机
                				Map<String, Object> camera = ProcessContext.getCameraDeploy(detectorNfc);
                				//调用摄像机
                				callCamera(camera);
                				alarmMap.put("alarmDesc", "探测器不在线,编号[" + detectorSeq + " - " + detectorEndSeq + "],处理器NFC号["+ this.processorNFCNum + "]");
                				ProcessContext.registerAlarmMessage(alarmMap);
                				//不在线, 后面的探测器不检测 , 跳出该循环
                				break;
                			}else {
                				int highValue = b >> 7 & 1;
                    			if(highValue == 1) {
                    				// 探测器感知报警
                    				int detectorIndex = i - 10;
                    				//int detectorIndex = i - 11;
                    				//获取探测器信息
                    				String keyPos = "pos_"  + this.processorNFCNum + "_" + detectorIndex;
                    				String keyNfc = "nfc_"  + this.processorNFCNum + "_" + detectorIndex;                    				
                    				String keySeq = "seq_"  + this.processorNFCNum + "_" + detectorIndex;
                    				String detectorSeq = ProcessContext.getStringValue(keySeq);
                    				
                    				String detectorPos = ProcessContext.getStringValue(keyPos);
                    				String detectorNfc = ProcessContext.getStringValue(keyNfc);
                    				
                    				boolean isLead = true;
                    				if(curDetectorAlarmIndex > 0) {
                    					if(detectorIndex == curDetectorAlarmIndex + 1 ) {
                    						curDetectorAlarmIndex = detectorIndex;
                    						nearNum ++;
                    						if(nearNum < 5) {
                    							//相领探测器报警
                    							isLead = false;
                    						}else {
                    							// 超出相邻范围的探测器报警
                    							isLead = true;
                    							curDetectorAlarmIndex = -1;
                    							nearNum = 0;
                    						}	
                    					}
                    				}else {
                    					curDetectorAlarmIndex = detectorIndex;
                    				}
                    				
                    				if(isLead) {
                    					//领头探测器报警调整到摄像机预置位
                    					//获取关联摄像机
                        				Map<String, Object> camera = ProcessContext.getCameraDeploy(detectorNfc);
                        				//调用摄像机
                        				callCamera(camera);
                    				}
                    				// isLead:true 是领头报警的探测器, isLead:false 跟随的报警(相邻范围的探测器报警), 发送报警消息
                    				Map<String,String> alarmMap = new HashMap<String, String>();
                    				alarmMap.put("alarmDesc", "探测器受干扰, 编号["+detectorSeq+"],处理器NFC号["+this.processorNFCNum+"].");
                    	        	alarmMap.put("alarmDate", DateUtils.getNow(DateUtils.FORMAT_LONG));
                    				alarmMap.put("deviceType", "detector");
                    				alarmMap.put("nfcNumber", detectorNfc);
                    				alarmMap.put("detectorSeq", "" + detectorSeq);
                    				alarmMap.put("alarmType", "touch");
                    				alarmMap.put("isLead", Boolean.toString(isLead));
                    				alarmMap.put("posInfo", detectorPos);
                    				ProcessContext.registerAlarmMessage(alarmMap);
                    			}
                			}
						}
					}
				try {
					Thread.sleep(600);
				} catch (InterruptedException e) {
					//
				}
			}
		}catch (UnknownHostException e) {
			// 不能连接处理器, 触发处理器不在线告警
			//String posInfo = ProcessContext.getStringValue(this.processorNFCNum);
			Map<String,String> alarmMap = new HashMap<String, String>();
			alarmMap.put("alarmDesc", "处理器不能连接,IP[" + this.host +"]、NFC号["+this.processorNFCNum+"].");
			alarmMap.put("alarmDate", DateUtils.getNow(DateUtils.FORMAT_LONG));
			alarmMap.put("deviceType", "processor");
			alarmMap.put("nfcNumber", this.processorNFCNum);
			alarmMap.put("detectorNums", "" + this.detectorNums);			
			alarmMap.put("host", this.host);
			alarmMap.put("posInfo", this.posInfo);
			
			//获取关联摄像机
			Map<String, Object> camera = ProcessContext.getCameraDeploy(this.processorNFCNum);
			//调用摄像机
			callCamera(camera);
			ProcessContext.registerAlarmMessage(alarmMap);
			
			// 设置处理器在线状态
			String updateProcessorOnlinePreSql = ConfigSQLUtil.getCacheSql("alarm-processor-updateProcessorOnline");
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.clear();
			paramsMap.put("processorId", this.processorId);
			paramsMap.put("online", "N");		
			String updateProcessorOnlineSql =  ConfigSQLUtil.preProcessSQL(updateProcessorOnlinePreSql, paramsMap);
			JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringContextUtil.getBean("SpringJdbcTemplate");
			try {
				jdbcTemplate.update(updateProcessorOnlineSql);
			} catch (DataAccessException dae) {
				//
			}
        }catch (IOException e) {
        	// 不能连接处理器, 触发处理器不在线告警        	
        	Map<String,String> alarmMap = new HashMap<String, String>();
        	alarmMap.put("alarmDesc", "处理器不能连接,IP[" + this.host +"]、NFC号["+this.processorNFCNum+"].");
        	alarmMap.put("alarmDate", DateUtils.getNow(DateUtils.FORMAT_LONG));
			alarmMap.put("deviceType", "processor");
			alarmMap.put("nfcNumber", this.processorNFCNum);
			alarmMap.put("detectorNums", "" + this.detectorNums);
			alarmMap.put("host", this.host);
			alarmMap.put("posInfo", this.posInfo);
			
			//获取关联摄像机
			Map<String, Object> camera = ProcessContext.getCameraDeploy(this.processorNFCNum);
			//调用摄像机
			callCamera(camera);
			
			ProcessContext.registerAlarmMessage(alarmMap);
			// 设置处理器在线状态
			String updateProcessorOnlinePreSql = ConfigSQLUtil.getCacheSql("alarm-processor-updateProcessorOnline");
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.clear();
			paramsMap.put("processorId", this.processorId);
			paramsMap.put("online", "N");		
			String updateProcessorOnlineSql =  ConfigSQLUtil.preProcessSQL(updateProcessorOnlinePreSql, paramsMap);
			JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringContextUtil.getBean("SpringJdbcTemplate");
			try {
				jdbcTemplate.update(updateProcessorOnlineSql);
			} catch (DataAccessException dae) {
				//
			}
		}
	}
	
	private void callCamera(Map<String, Object> camera) {
		if(camera != null) {
			//调用摄像机调整到预置位
			String ip = camera.get("camera_ip") == null ? "" : camera.get("camera_ip").toString();
			String devicePort = camera.get("device_port") == null ? "0" : camera.get("device_port").toString();
			String userName = camera.get("camera_user_name") == null ? "" : camera.get("camera_user_name").toString();
			String password = camera.get("camera_password") == null ? "" : camera.get("camera_password").toString();
			String presetNum = camera.get("preset_num") == null ? "0" : camera.get("preset_num").toString();
			int iDevicePort = 0;
			int iPresetNum = 0;
			try {
				iDevicePort = Integer.valueOf(devicePort);
				iPresetNum = Integer.valueOf(presetNum);
			}catch(Exception e) {
				log.info(e.getMessage());
			}
			try {
				CameraHandle.getInstance().goPreset4HC(ip, iDevicePort, userName, password, iPresetNum);
			}catch(Exception e) {
				log.info("摄像机预置位调用失败:{}",e.getMessage());
			}			
		}
	}
}
