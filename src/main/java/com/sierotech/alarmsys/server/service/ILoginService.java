package com.sierotech.alarmsys.server.service;

import java.util.Map;

import com.sierotech.alarmsys.common.BusinessException;

public interface ILoginService{
	//
	public Map doLogin(String userNo,String password) throws BusinessException ;
	
}
