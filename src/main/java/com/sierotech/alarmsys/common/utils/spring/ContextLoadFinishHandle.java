package com.sierotech.alarmsys.common.utils.spring;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener; 
import org.springframework.context.event.ContextRefreshedEvent; 
import org.springframework.stereotype.Component;

import com.sierotech.alarmsys.common.utils.BaseDataInitializor;
import com.sierotech.alarmsys.common.utils.ProcessorMonitorInitializor;
import com.sierotech.alarmsys.common.utils.SQLPoolInitializor;
import com.sierotech.alarmsys.context.ProcessContext;
import com.sierotech.alarmsys.monitor.AlarmMessageHandlerTask;

@Component
public class ContextLoadFinishHandle implements 
ApplicationListener<ContextRefreshedEvent>{
	static final Logger log = LoggerFactory.getLogger(ContextLoadFinishHandle.class);
	
	@Autowired
	private SQLPoolInitializor sqlPoolInit;
	
	@Autowired
	private BaseDataInitializor baseDataInit;
	
	@Autowired
	private ProcessorMonitorInitializor pMonitorInit;
	
	@Override 
	public void onApplicationEvent(ContextRefreshedEvent event) { 
		if(event.getApplicationContext().getDisplayName().startsWith("Root WebApplicationContext")){
			ProcessContext.initRedis();
			
			sqlPoolInit.run();
			baseDataInit.run();
			// 与维护的处理器建立连接,监听处理器状态
			
			pMonitorInit.run();
			
			//启动报警消息处理线程
			AlarmMessageHandlerTask alarmMsgTask = new AlarmMessageHandlerTask();
			Thread t = new Thread(alarmMsgTask);
			t.setName("报警消息处理");
			t.start();
		}
	}
}
