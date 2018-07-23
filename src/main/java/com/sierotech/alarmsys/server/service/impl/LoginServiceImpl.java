package com.sierotech.alarmsys.server.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.sierotech.alarmsys.common.BusinessException;
import com.sierotech.alarmsys.common.utils.ConfigSQLUtil;
import com.sierotech.alarmsys.common.utils.PasswordUtil;
import com.sierotech.alarmsys.server.service.ILoginService;

@Service
public class LoginServiceImpl implements ILoginService{
	static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);
	
	@Autowired	
	private JdbcTemplate springJdbcDao;
	
	@Override
	public Map doLogin(String userName, String password) throws BusinessException{
		
		if (null == userName) {
			throw new BusinessException("登录名不能为空.");
		}
		Map<String, String> paramMap = new HashMap<String,String>();
		paramMap.put("userName", userName);
		
		// 获取用户
		Map<String, Object> loginUser = null;
		String preSql = ConfigSQLUtil.getCacheSql("alarm-user-getUserByUserName");
		String sql = ConfigSQLUtil.preProcessSQL(preSql, paramMap);
		
		List<Map<String,Object>> alUser = null;
		try {
			alUser = springJdbcDao.queryForList(sql);
		}catch(DataAccessException dae) {
			log.info(dae.getMessage());
		}
		if(alUser!=null && alUser.size()> 0) {
			loginUser = alUser.get(0);
		}
		
		if (null == loginUser) {
			throw new BusinessException("用户不存在.");
		}
		if ("D".equals(loginUser.get("status").toString())) {
			throw new BusinessException("用户已失效.");
		}
		
		if (!PasswordUtil.validPassword(password, loginUser.get("password").toString())){
			throw new BusinessException("用户密码不正确.");
		}
		
		//验证通过 返回loginUser
		return loginUser;
	}
	
}
