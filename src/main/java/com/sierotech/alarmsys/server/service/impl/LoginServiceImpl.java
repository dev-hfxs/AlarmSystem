package com.sierotech.alarmsys.server.service.impl;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.sierotech.alarmsys.common.BusinessException;
import com.sierotech.alarmsys.server.service.ILoginService;

@Service
public class LoginServiceImpl implements ILoginService{

	@Autowired	
	private JdbcTemplate springJdbc;
	
	@Override
	public Map doLogin(String userNo, String password) throws BusinessException{
		
//		springJdbc.update("insert into t_test(id,uname) values('us12311','zhangsan')");
		
		return null;
	}
	
}
