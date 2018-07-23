/**
* <p>版权所有:(C)2018-2022 天津航峰希萨科技有限公司 </p>
* @创建人: lwm
* @创建日期: 2018年4月19日
* @修改人: 
* @修改日期：
* @描述: 
 */
package com.sierotech.alarmsys.server.service;

import java.util.List;
import java.util.Map;

import com.sierotech.alarmsys.common.BusinessException;

/**
* @JDK版本: 1.7
* @创建人: lwm
* @创建日期：2018年4月19日
* @功能描述: 处理器维护接口
 */
public interface IProcessorService {
	public boolean checkMoxaNumber(String processorId, String moxaNumber) throws BusinessException;
	
	public boolean checkNfcNumber(String processorId, String nfcNumber) throws BusinessException;
	
	public void addProcessor(String adminUser,Map<String,Object> processorObj) throws BusinessException;
	
	public void updateProcessor(String adminUser,Map<String,Object> processorObj) throws BusinessException;
	
	public void deleteProcessor(String adminUser,String processorId) throws BusinessException;
		
}
