/**
* <p>版权所有:(C)2018-2022 天津航峰希萨科技有限公司 </p>
* @创建人: lwm
* @创建日期: 2018年8月22日
* @修改人: 
* @修改日期：
* @描述: 
 */
package com.sierotech.alarmsys.camera;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sun.jna.NativeLong;

/**
* @JDK版本: 1.7
* @创建人: lwm
* @创建日期：2018年8月22日
* @功能描述: 
 */
public class CameraHandle {
	private static final Logger log = LoggerFactory.getLogger(CameraHandle.class);
	
	private static CameraHandle instance;
	
	private HCNetSDK hcNetSdk;
	
	private com.sierotech.alarmsys.camera.linux.HCNetSDK linuxHcNetSdk;
	
	private CameraHandle(){
		String os = System.getProperty("os.name");
		boolean initSuc = false;		
		if(os.toLowerCase().indexOf("win") >= 0){
			hcNetSdk = HCNetSDK.INSTANCE;
			initSuc = hcNetSdk.NET_DVR_Init();
		}else {
			linuxHcNetSdk = com.sierotech.alarmsys.camera.linux.HCNetSDK.INSTANCE;
			initSuc = linuxHcNetSdk.NET_DVR_Init();	        
		}
		if (initSuc != true)
        {
       	 	log.info("海康插件初始化失败.");
        }
	}
	
	public static CameraHandle getInstance(){
        if(instance==null){
            instance = new CameraHandle();
        }
        return instance;
    }
	
	public void goPreset4HC(String ip, int devicePort, String userName, String password, int presetNum) {
		//海康摄像头调整预置位
		if(hcNetSdk != null) {
			HCNetSDK.NET_DVR_DEVICEINFO_V30 m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
			NativeLong lUserID = hcNetSdk.NET_DVR_Login_V30(ip, (short)devicePort, userName, password, m_strDeviceInfo);
			long userID = lUserID.longValue();
			if(userID < 0 ) {
				log.info("连接摄像头ip[{}]失败,用户名:{},密码:{}.", ip, userName, password);
			}
			HCNetSDK.NET_DVR_CLIENTINFO m_strClientInfo;//用户参数
			m_strClientInfo = new HCNetSDK.NET_DVR_CLIENTINFO();
		    // 要操作的通道
			m_strClientInfo.lChannel = new NativeLong(1);
		    
		    NativeLong lPreviewHandle;//预览句柄
		    lPreviewHandle = hcNetSdk.NET_DVR_RealPlay(lUserID,m_strClientInfo);
			if (!hcNetSdk.NET_DVR_PTZPreset(lPreviewHandle, HCNetSDK.GOTO_PRESET, presetNum))
			{
				log.info("lPreviewHandle:{}, presetNum:{},errorCode:{}", lPreviewHandle, presetNum, hcNetSdk.NET_DVR_GetLastError());
		         log.info("摄像头[{}]调用预置点[{}]失败.", ip ,presetNum);
		    }
		}else if(linuxHcNetSdk != null) {
			com.sierotech.alarmsys.camera.linux.HCNetSDK.NET_DVR_DEVICEINFO_V30 m_strDeviceInfo = new com.sierotech.alarmsys.camera.linux.HCNetSDK.NET_DVR_DEVICEINFO_V30();
			NativeLong lUserID = linuxHcNetSdk.NET_DVR_Login_V30(ip, (short)devicePort, userName, password, m_strDeviceInfo);
			long userID = lUserID.longValue();
			if(userID < 0 ) {
				log.info("连接摄像头ip[{}]失败,用户名:{},密码:{}.", ip, userName, password);
			}
			com.sierotech.alarmsys.camera.linux.HCNetSDK.NET_DVR_CLIENTINFO m_strClientInfo;//用户参数
			m_strClientInfo = new com.sierotech.alarmsys.camera.linux.HCNetSDK.NET_DVR_CLIENTINFO();
		    // 要操作的通道
			m_strClientInfo.lChannel = new NativeLong(1);		    
		    NativeLong lPreviewHandle;//预览句柄
		    lPreviewHandle = linuxHcNetSdk.NET_DVR_RealPlay(lUserID,m_strClientInfo);
			if (!linuxHcNetSdk.NET_DVR_PTZPreset(lPreviewHandle, com.sierotech.alarmsys.camera.linux.HCNetSDK.GOTO_PRESET, presetNum))
			{
				log.info("lPreviewHandle:{}, presetNum:{},errorCode:{}", lPreviewHandle, presetNum, linuxHcNetSdk.NET_DVR_GetLastError());
		        log.info("摄像头[{}]调用预置点[{}]失败.", ip , presetNum);
		    }
		}else {
			log.info("预置位调用失败, 海康摄像头插件没有初始化.");
		}
	}
}
