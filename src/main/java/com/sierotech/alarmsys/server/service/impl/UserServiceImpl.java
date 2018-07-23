/**
* <p>版权所有:(C)2018-2022 天津航峰希萨科技有限公司 </p>
* @创建人: lwm
* @创建日期: 2018年4月17日
* @修改人: 
* @修改日期：
* @描述: 
 */
package com.sierotech.alarmsys.server.service.impl;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.sierotech.alarmsys.common.BusinessException;
import com.sierotech.alarmsys.common.utils.ConfigSQLUtil;
import com.sierotech.alarmsys.common.utils.DateUtils;
import com.sierotech.alarmsys.common.utils.LogOperationUtil;
import com.sierotech.alarmsys.common.utils.PasswordUtil;
import com.sierotech.alarmsys.common.utils.UUIDGenerator;
import com.sierotech.alarmsys.server.service.IUserService;


/**
 * @JDK版本: 1.7
 * @创建人: lwm
 * @创建日期：2018年4月17日 
 * @功能描述: 用户管理服务层处理
 */
@Service
public class UserServiceImpl implements IUserService {

	static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private JdbcTemplate springJdbcDao;

	@Override
	public boolean checkUserExist(String userId, String userName, String column) throws BusinessException {
		boolean result = true;
		if (null == userId) {
			return true;
		}
		if (null == userName || "".equals(userName)) {
			throw new BusinessException("用户名验证,用户名参数为空!");
		}
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("userId", userId);
		String sqlKey = "alarm-user-checkUserExistsByUserName";
		if("fullName".equals(column)) {
			paramsMap.put("fullName", userName);
			sqlKey = "alarm-user-checkUserExistsByFullName";
		}else if ("userName".equals(column)) {
			paramsMap.put("userName", userName);
			sqlKey = "alarm-user-checkUserExistsByUserName";
		}else {
			return true;
		}
		String preSql = ConfigSQLUtil.getCacheSql(sqlKey);
		String sql = ConfigSQLUtil.preProcessSQL(preSql, paramsMap);
		try {
			Map<String, Object> recordMap = springJdbcDao.queryForMap(sql);
			if (recordMap != null) {
				int num = Integer.valueOf(recordMap.get("countNum").toString());
				if (num < 1) {
					result = false;
				}
			}
		} catch (DataAccessException ex) {
			log.info("用户名验证，访问数据库异常.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void addUser(String adminUser, Map<String, Object> userObj) throws BusinessException {
		if (null == adminUser) {
			throw new BusinessException("添加用户错误,当前操作是未知的管理员!");
		}

		if (null == userObj.get("userName")) {
			throw new BusinessException("添加用户错误,缺少用户名!");
		}
		if (null == userObj.get("fullName")) {
			throw new BusinessException("添加用户参数错误,缺少用户姓名.");
		}
		if (null == userObj.get("orgId")) {
			throw new BusinessException("添加用户参数错误,缺少用户单位.");
		}
		if (null == userObj.get("contactNumber")) {
			throw new BusinessException("添加用户参数错误,缺少用户联系电话.");
		}
		// 检查用户名是否重复
		boolean userExists = checkUserExist("", userObj.get("userName").toString(), "userName");
		if (userExists) {
			throw new BusinessException("用户名已存在!");
		}
		// 根据电话号码设置初始密码
		String password = PasswordUtil.encrypt(userObj.get("contactNumber").toString());
		userObj.put("password", password);
		userObj.put("userId", UUIDGenerator.getUUID());
		userObj.put("creator", adminUser);
		userObj.put("createDate", DateUtils.getNow(DateUtils.FORMAT_LONG));

		String preSql = ConfigSQLUtil.getCacheSql("alarm-user-addUser");
		String sql = ConfigSQLUtil.preProcessSQL(preSql, userObj);
		try {
			springJdbcDao.update(sql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("添加用户,访问数据库异常.");
		}
		// 记录日志
		LogOperationUtil.logAdminOperation(adminUser, "用户管理", "添加用户:[" + userObj.get("userName").toString() + "].");
	}

	@Override
	public void updateUser(String adminUser, Map<String, Object> userObj) throws BusinessException {
		if (null == adminUser) {
			throw new BusinessException("修改用户错误,当前操作是未知的管理员!");
		}

		if (null == userObj.get("userName")) {
			throw new BusinessException("修改用户错误,缺少用户名!");
		}
		if (null == userObj.get("fullName")) {
			throw new BusinessException("修改用户参数错误,缺少用户姓名.");
		}
		if (null == userObj.get("orgId")) {
			throw new BusinessException("修改用户参数错误,缺少用户单位.");
		}
		if (null == userObj.get("contactNumber")) {
			throw new BusinessException("修改用户参数错误,缺少用户联系电话.");
		}
		// 检查用户名是否重复
		boolean userExists = checkUserExist(userObj.get("id").toString(), userObj.get("userName").toString(), "userName");
		if (userExists) {
			throw new BusinessException("用户名已存在!");
		}

		// 先获取用户
		String preSelectSql = ConfigSQLUtil.getCacheSql("alarm-user-getUserById");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("userId", userObj.get("id"));
		String selectSql = ConfigSQLUtil.preProcessSQL(preSelectSql, paramsMap);
		List<Map<String, Object>> alUsers;
		try {
			alUsers = springJdbcDao.queryForList(selectSql);
		} catch (DataAccessException dae) {
			throw new BusinessException("修改用户错误,获取用户数据库访问异常.");
		}
		Map<String, Object> oldUserObj;
		if (alUsers != null && alUsers.size() > 0) {
			oldUserObj = alUsers.get(0);
		} else {
			throw new BusinessException("修改用户错误,未查询到用户.");
		}
				
		userObj.put("userId", userObj.get("id"));

		String preSql = ConfigSQLUtil.getCacheSql("alarm-user-updateUser");
		String sql = ConfigSQLUtil.preProcessSQL(preSql, userObj);
		try {
			springJdbcDao.update(sql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("修改用户,访问数据库异常.");
		}
		// 记录日志
		LogOperationUtil.logAdminOperation(adminUser, "用户管理", "修改用户:[" + userObj.get("userName").toString() + "];修改前的用户名:"+oldUserObj.get("user_name").toString()+",姓名:"+oldUserObj.get("full_name").toString());

	}

	@Override
	public void updateUserPwd(String adminUser, String userId) throws BusinessException {
		if (null == adminUser) {
			throw new BusinessException("重置用户密码错误,当前操作是未知的管理员!");
		}

		if (null == userId) {
			throw new BusinessException("重置用户密码错误,缺少用户ID!");
		}

		// 先获取用户
		String preSelectSql = ConfigSQLUtil.getCacheSql("alarm-user-getUserById");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("userId", userId);
		String selectSql = ConfigSQLUtil.preProcessSQL(preSelectSql, paramsMap);
		List<Map<String, Object>> alUsers;
		try {
			alUsers = springJdbcDao.queryForList(selectSql);
		} catch (DataAccessException dae) {
			throw new BusinessException("重置用户密码错误,获取用户数据库访问异常.");
		}
		Map<String, Object> userObj;
		if (alUsers != null && alUsers.size() > 0) {
			userObj = alUsers.get(0);
		} else {
			throw new BusinessException("重置用户密码错误,未查询到用户.");
		}

		String newPassword = "";
		if (userObj.get("contact_number") != null) {
			String contactNumber = userObj.get("contact_number").toString();
			newPassword = PasswordUtil.encrypt(contactNumber);
		}
		if ("".equals(newPassword)) {
			throw new BusinessException("重置用户密码错误,未能按规则生成密码.");
		}
		String preUpdateSql = ConfigSQLUtil.getCacheSql("alarm-user-updatePwdByUsrId");
		paramsMap.clear();
		paramsMap.put("userId", userId);
		paramsMap.put("newPassword", newPassword);
		paramsMap.put("initPassword", 'Y');

		String updateSql = ConfigSQLUtil.preProcessSQL(preUpdateSql, paramsMap);
		try {
			springJdbcDao.update(updateSql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("重置用户密码错误,访问数据库异常.");
		}
		// 记录日志
		LogOperationUtil.logAdminOperation(adminUser, "用户管理", "重置用户密码:[" + userObj.get("user_name").toString() + "].");

	}

	@Override
	public void updateUserPwd(String userId, String oldPwd, String newPwd) throws BusinessException {
		if (null == userId) {
			throw new BusinessException("用户修改密码错误,缺少用户ID!");
		}
		// 先获取用户
		String preSelectSql = ConfigSQLUtil.getCacheSql("alarm-user-getUserById");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("userId", userId);
		String selectSql = ConfigSQLUtil.preProcessSQL(preSelectSql, paramsMap);
		List<Map<String, Object>> alUsers;
		try {
			alUsers = springJdbcDao.queryForList(selectSql);
		} catch (DataAccessException dae) {
			throw new BusinessException("用户修改密码错误,获取用户数据库访问异常.");
		}
		Map<String, Object> userObj;
		if (alUsers != null && alUsers.size() > 0) {
			userObj = alUsers.get(0);
		} else {
			throw new BusinessException("用户修改密码错误,未查询到用户.");
		}
		String encryptOldPwd = PasswordUtil.encrypt(oldPwd);
		if (encryptOldPwd == null || !encryptOldPwd.equals(userObj.get("password"))) {
			throw new BusinessException("用户修改密码错误,原密码错误.");
		}
		String newPassword = PasswordUtil.encrypt(newPwd);
		String preUpdateSql = ConfigSQLUtil.getCacheSql("alarm-user-updatePwdByUsrId");
		paramsMap.clear();
		paramsMap.put("userId", userId);
		paramsMap.put("newPassword", newPassword);
		paramsMap.put("initPassword", 'N');
		String updateSql = ConfigSQLUtil.preProcessSQL(preUpdateSql, paramsMap);
		try {
			springJdbcDao.update(updateSql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("用户修改密码错误,访问数据库异常.");
		}
	}

	@Override
	public void deleteUser(String adminUser, String userId) throws BusinessException {
		if (null == adminUser) {
			throw new BusinessException("删除用户错误,当前操作是未知的管理员!");
		}
		if (null == userId) {
			throw new BusinessException("删除用户错误,缺少用户ID!");
		}

		// 先获取用户
		String preSelectSql = ConfigSQLUtil.getCacheSql("alarm-user-getUserById");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("userId", userId);
		String selectSql = ConfigSQLUtil.preProcessSQL(preSelectSql, paramsMap);
		List<Map<String, Object>> alUsers;
		try {
			alUsers = springJdbcDao.queryForList(selectSql);
		} catch (DataAccessException dae) {
			throw new BusinessException("删除用户错误,获取用户访问数据库异常.");
		}
		Map<String, Object> userObj;
		if (alUsers != null && alUsers.size() > 0) {
			userObj = alUsers.get(0);
		} else {
			throw new BusinessException("删除用户错误,未查询到用户.");
		}

		String preUpdateSql = ConfigSQLUtil.getCacheSql("alarm-user-deleteUserByUsrId");
		paramsMap.clear();
		paramsMap.put("userId", userId);
		String updateSql = ConfigSQLUtil.preProcessSQL(preUpdateSql, paramsMap);
		try {
			springJdbcDao.update(updateSql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("删除用户错误,访问数据库异常.");
		}
		// 记录日志
		LogOperationUtil.logAdminOperation(adminUser, "用户管理", "删除用户:[" + userObj.get("user_name").toString() + "].");
	}

	@Override
	public void updateUser4Recover(String adminUser, String userId) throws BusinessException {
		if (null == adminUser) {
			throw new BusinessException("恢复用户错误,当前操作是未知的管理员!");
		}
		if (null == userId) {
			throw new BusinessException("恢复用户错误,缺少用户ID!");
		}

		// 先获取用户
		String preSelectSql = ConfigSQLUtil.getCacheSql("alarm-user-getUserById");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("userId", userId);
		String selectSql = ConfigSQLUtil.preProcessSQL(preSelectSql, paramsMap);
		List<Map<String, Object>> alUsers;
		try {
			alUsers = springJdbcDao.queryForList(selectSql);
		} catch (DataAccessException dae) {
			throw new BusinessException("恢复用户错误,获取用户数据库访问异常.");
		}
		Map<String, Object> userObj;
		if (alUsers != null && alUsers.size() > 0) {
			userObj = alUsers.get(0);
		} else {
			throw new BusinessException("恢复用户错误,未查询到用户.");
		}

		String preUpdateSql = ConfigSQLUtil.getCacheSql("alarm-user-recoverUserByUsrId");
		paramsMap.clear();
		paramsMap.put("userId", userId);
		String updateSql = ConfigSQLUtil.preProcessSQL(preUpdateSql, paramsMap);
		try {
			springJdbcDao.update(updateSql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("恢复用户错误,访问数据库异常.");
		}
		// 记录日志
		LogOperationUtil.logAdminOperation(adminUser, "用户管理", "恢复用户:[" + userObj.get("user_name").toString() + "].");
	}
}
