package com.sierotech.alarmsys.server.service;

import java.util.List;
import java.util.Map;

import com.sierotech.alarmsys.common.BusinessException;


public interface IRoleService {
	//
	public boolean checkRoleExist(String roleId, String roleName) throws BusinessException;
	
	public void addRole(String adminUser,Map<String,Object> roleObj) throws BusinessException;
	
	public void updateRole(String adminUser,Map<String,Object> roleObj) throws BusinessException;

	public void deleteRole(String adminUser,String roleId) throws BusinessException;
	
	public void addRoleUser(String adminUser, String userId, String roleId) throws BusinessException;
	
	public void deleteRoleUser(String adminUser, String userId, String roleId) throws BusinessException;
		
	public void update4AllotFunc(String adminUser, String roleId, List<Map<String,String>> alAllotFunc) throws BusinessException;

}
