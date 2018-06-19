package com.sierotech.alarmsys.common.utils.spring;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener; 
import org.springframework.context.event.ContextRefreshedEvent; 
import org.springframework.stereotype.Component;

import com.sierotech.alarmsys.common.utils.SQLPoolInitializor;

@Component
public class ContextLoadFinishHandle implements 
ApplicationListener<ContextRefreshedEvent>{
	static final Logger log = LoggerFactory.getLogger(ContextLoadFinishHandle.class);
	
	@Autowired
	private SQLPoolInitializor sqlPoolInit;
	
//	@Autowired
//	private DataSourceInitializor dataSrcInit;
	
	@Override 
	public void onApplicationEvent(ContextRefreshedEvent event) { 
		if(event.getApplicationContext().getDisplayName().startsWith("Root WebApplicationContext")){
			sqlPoolInit.run();
			
		}
	}
}
