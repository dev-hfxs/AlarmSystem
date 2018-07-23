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
* @功能描述: 机箱维护接口
 */
public interface IBoxService {
	public boolean checkBoxNumber(String boxId, String boxNumber) throws BusinessException;
	
	public boolean checkBoxNfcNumber(String boxId, String nfcNumber) throws BusinessException;
	
	public void addBox(String adminUser,Map<String,Object> boxObj) throws BusinessException;
	
	public void updateBox(String adminUser,Map<String,Object> boxObj) throws BusinessException;
	
	public void deleteBox(String adminUser,String boxId) throws BusinessException;
		
}
