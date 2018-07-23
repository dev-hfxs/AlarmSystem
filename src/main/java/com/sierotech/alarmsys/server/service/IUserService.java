package com.sierotech.alarmsys.server.service;

import java.util.Map;

import com.sierotech.alarmsys.common.BusinessException;


public interface IUserService {
	//
	public boolean checkUserExist(String userId, String userName, String column ) throws BusinessException;
	
	public void addUser(String adminUser,Map<String,Object> userObj) throws BusinessException;
	
	public void updateUser(String adminUser,Map<String,Object> userObj) throws BusinessException;

	public void updateUserPwd(String adminUser,String userId) throws BusinessException;
	
	public void updateUserPwd(String userId, String oldPwd,String newPwd) throws BusinessException;
	
	public void deleteUser(String adminUser,String userId) throws BusinessException;
	
	public void updateUser4Recover(String adminUser,String userId) throws BusinessException;
}
