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
import com.sierotech.alarmsys.common.utils.UUIDGenerator;
import com.sierotech.alarmsys.server.service.IRoleService;


/**
 * @JDK版本: 1.7
 * @创建人: lwm
 * @创建日期：2018年4月17日 
 * @功能描述: 角色管理服务层处理
 */
@Service
public class RoleServiceImpl implements IRoleService {

	static final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

	@Autowired
	private JdbcTemplate springJdbcDao;


	@Override
	public boolean checkRoleExist(String roleId, String roleName) throws BusinessException {
		boolean result = true;
		if (null == roleId) {
			return true;
		}
		if (null == roleName || "".equals(roleName)) {
			throw new BusinessException("角色名称不能为空!");
		}
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("roleId", roleId);
		String preSql = ConfigSQLUtil.getCacheSql("alarm-role-checkExistsRoleName");
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
			log.info("角色名验证，访问数据库异常.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void addRole(String adminUser, Map<String, Object> roleObj) throws BusinessException {
		if (null == adminUser) {
			throw new BusinessException("添加角色错误,当前操作是未知的管理员!");
		}

		if (null == roleObj.get("roleName")) {
			throw new BusinessException("添加角色错误,缺少角色名!");
		}
		
		// 检查角色名是否重复
		boolean roleExists = checkRoleExist("", roleObj.get("roleName").toString());
		if (roleExists) {
			throw new BusinessException("角色名已存在!");
		}
		
		roleObj.put("userId", UUIDGenerator.getUUID());
		roleObj.put("creator", adminUser);
		roleObj.put("createDate", DateUtils.getNow(DateUtils.FORMAT_LONG));

		String preSql = ConfigSQLUtil.getCacheSql("alarm-role-addRole");
		String sql = ConfigSQLUtil.preProcessSQL(preSql, roleObj);
		try {
			springJdbcDao.update(sql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("添加角色,访问数据库异常.");
		}
		// 记录日志
		LogOperationUtil.logAdminOperation(adminUser, "角色管理", "添加角色:[" + roleObj.get("roleName").toString() + "].");
	}

	@Override
	public void updateRole(String adminUser, Map<String, Object> roleObj) throws BusinessException {
		if (null == adminUser) {
			throw new BusinessException("修改角色错误, 当前操作是未知的管理员!");
		}

		if (null == roleObj.get("roleName") || "".equals(roleObj.get("roleName"))) {
			throw new BusinessException("修改角色错误,角色名不能为空!");
		}
		
		boolean roleExists = checkRoleExist(roleObj.get("roleId").toString(), roleObj.get("roleName").toString());
		if (roleExists) {
			throw new BusinessException("角色名已存在!");
		}

		// 先获取角色
		String preSelectSql = ConfigSQLUtil.getCacheSql("alarm-role-getRoleById");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("roleId", roleObj.get("roleId"));
		String selectSql = ConfigSQLUtil.preProcessSQL(preSelectSql, paramsMap);
		List<Map<String, Object>> alRoles;
		try {
			alRoles = springJdbcDao.queryForList(selectSql);
		} catch (DataAccessException dae) {
			throw new BusinessException("修改角色错误,数据库访问异常.");
		}
		Map<String, Object> oldRoleObj;
		if (alRoles != null && alRoles.size() > 0) {
			oldRoleObj = alRoles.get(0);
		} else {
			throw new BusinessException("修改角色错误,未查询到角色.");
		}
		String preSql = ConfigSQLUtil.getCacheSql("alarm-role-updateRole");
		String sql = ConfigSQLUtil.preProcessSQL(preSql, roleObj);
		try {
			springJdbcDao.update(sql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("修改角色,访问数据库异常.");
		}
		// 记录日志
		LogOperationUtil.logAdminOperation(adminUser, "角色管理", "修改角色:[" + roleObj.get("roleName").toString() + "];修改前的角色名:"+oldRoleObj.get("role_name").toString());

	}

	@Override
	public void deleteRole(String adminUser, String roleId) throws BusinessException {
		if (null == adminUser) {
			throw new BusinessException("删除角色错误,当前操作是未知的管理员!");
		}
		if (null == roleId) {
			throw new BusinessException("删除角色错误,缺少角色ID!");
		}

		// 先获取角色
		String preSelectSql = ConfigSQLUtil.getCacheSql("alarm-role-getRoleById");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("roleId", roleId);
		String selectSql = ConfigSQLUtil.preProcessSQL(preSelectSql, paramsMap);
		List<Map<String, Object>> alRoles;
		try {
			alRoles = springJdbcDao.queryForList(selectSql);
		} catch (DataAccessException dae) {
			throw new BusinessException("删除角色错误, 访问数据库异常.");
		}
		Map<String, Object> roleObj;
		if (alRoles != null && alRoles.size() > 0) {
			roleObj = alRoles.get(0);
		} else {
			throw new BusinessException("删除角色错误,未查询到角色.");
		}

		//检查角色是否已分配用户
		String checkRoleHasUserPreSql = ConfigSQLUtil.getCacheSql("alarm-role-checkRoleHasUser");
		String checkRoleHasUserSql = ConfigSQLUtil.preProcessSQL(checkRoleHasUserPreSql, paramsMap);
		int num = 0;
		try {
			Map<String, Object> recordMap = springJdbcDao.queryForMap(checkRoleHasUserSql);
			if (recordMap != null) {
				num = Integer.valueOf(recordMap.get("countNum").toString());
			}
		} catch (DataAccessException ex) {
			log.info("检查角色是否包含用户，访问数据库异常.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(num > 0) {
			throw new BusinessException("角色已分配用户，不能删除.");
		}
		
		String preUpdateSql = ConfigSQLUtil.getCacheSql("alarm-role-deleteRoleById");
		paramsMap.clear();
		paramsMap.put("roleId", roleId);
		String updateSql = ConfigSQLUtil.preProcessSQL(preUpdateSql, paramsMap);
		try {
			springJdbcDao.update(updateSql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("删除角色错误,访问数据库异常.");
		}
		// 记录日志
		LogOperationUtil.logAdminOperation(adminUser, "角色管理", "删除角色:[" + roleObj.get("role_name").toString() + "].");
		
	}

	@Override
	public void addRoleUser(String adminUser, String userId, String roleId) throws BusinessException {
		if (null == adminUser) {
			throw new BusinessException("为角色分配用户错误,当前操作是未知的管理员!");
		}
		if (null == roleId) {
			throw new BusinessException("为角色分配用户错误,缺少角色ID!");
		}
		if (null == userId) {
			throw new BusinessException("为角色分配用户错误,缺少用户ID!");
		}
		// 检查角色是否存在
		String checkRoleExistPreSql = ConfigSQLUtil.getCacheSql("alarm-role-getRoleById");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("roleId", roleId);
		String checkRoleExistSql = ConfigSQLUtil.preProcessSQL(checkRoleExistPreSql, paramsMap);
		List<Map<String, Object>> alRoles;
		try {
			alRoles = springJdbcDao.queryForList(checkRoleExistSql);
		} catch (DataAccessException dae) {
			throw new BusinessException("为角色分配用户错误, 访问数据库异常.");
		}
		Map<String, Object> roleObj;
		if (alRoles != null && alRoles.size() > 0) {
			roleObj = alRoles.get(0);
		} else {
			throw new BusinessException("为角色分配用户错误,未查询到角色.");
		}
		// 检查用户是否存在
		String checkUserExistPreSql = ConfigSQLUtil.getCacheSql("alarm-user-getUserById");
		paramsMap.put("userId", userId);
		String checkUserExistSql = ConfigSQLUtil.preProcessSQL(checkUserExistPreSql, paramsMap);
		List<Map<String, Object>> alUsers;
		try {
			alUsers = springJdbcDao.queryForList(checkUserExistSql);
		} catch (DataAccessException dae) {
			throw new BusinessException("为角色分配用户错误, 数据库访问异常.");
		}
		Map<String, Object> oldUserObj;
		if (alUsers != null && alUsers.size() > 0) {
			oldUserObj = alUsers.get(0);
		} else {
			throw new BusinessException("为角色分配用户错误, 未查询到用户.");
		}
				
		String preUpdateSql = ConfigSQLUtil.getCacheSql("alarm-role-addRoleUser");
		paramsMap.clear();
		paramsMap.put("id", UUIDGenerator.getUUID());
		paramsMap.put("roleId", roleId);
		paramsMap.put("userId", userId);
		String updateSql = ConfigSQLUtil.preProcessSQL(preUpdateSql, paramsMap);
		try {
			springJdbcDao.update(updateSql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("为角色分配用户错误, 访问数据库异常.");
		}
		// 记录日志
		LogOperationUtil.logAdminOperation(adminUser, "角色管理", "角色新分配用户.角色名称[" + roleObj.get("role_name") + "], 用户名[" + oldUserObj.get("user_name") + "]");
	}

	@Override
	public void deleteRoleUser(String adminUser, String userId, String roleId) throws BusinessException {
		if (null == adminUser) {
			throw new BusinessException("为角色取消分配用户错误,当前操作是未知的管理员!");
		}
		if (null == roleId) {
			throw new BusinessException("为角色取消分配用户错误,缺少角色ID!");
		}
		if (null == userId) {
			throw new BusinessException("为角色取消分配用户错误,缺少用户ID!");
		}
		// 检查角色是否存在
		String checkRoleExistPreSql = ConfigSQLUtil.getCacheSql("alarm-role-getRoleById");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("roleId", roleId);
		String checkRoleExistSql = ConfigSQLUtil.preProcessSQL(checkRoleExistPreSql, paramsMap);
		List<Map<String, Object>> alRoles;
		try {
			alRoles = springJdbcDao.queryForList(checkRoleExistSql);
		} catch (DataAccessException dae) {
			throw new BusinessException("为角色取消分配用户错误, 访问数据库异常.");
		}
		Map<String, Object> roleObj;
		if (alRoles != null && alRoles.size() > 0) {
			roleObj = alRoles.get(0);
		} else {
			throw new BusinessException("为角色取消分配用户错误,未查询到角色.");
		}
		// 检查用户是否存在
		String checkUserExistPreSql = ConfigSQLUtil.getCacheSql("alarm-user-getUserById");
		paramsMap.clear();
		paramsMap.put("userId", userId);
		String checkUserExistSql = ConfigSQLUtil.preProcessSQL(checkUserExistPreSql, paramsMap);
		List<Map<String, Object>> alUsers;
		try {
			alUsers = springJdbcDao.queryForList(checkUserExistSql);
		} catch (DataAccessException dae) {
			throw new BusinessException("为角色取消分配用户错误, 数据库访问异常.");
		}
		Map<String, Object> oldUserObj;
		if (alUsers != null && alUsers.size() > 0) {
			oldUserObj = alUsers.get(0);
		} else {
			throw new BusinessException("为角色取消分配用户错误, 未查询到用户.");
		}
		String preUpdateSql = ConfigSQLUtil.getCacheSql("alarm-role-deleteRoleUser");
		paramsMap.clear();
		paramsMap.put("roleId", roleId);
		paramsMap.put("userId", userId);
		String updateSql = ConfigSQLUtil.preProcessSQL(preUpdateSql, paramsMap);
		try {
			springJdbcDao.update(updateSql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("为角色取消分配用户错误, 访问数据库异常.");
		}
		// 记录日志
		LogOperationUtil.logAdminOperation(adminUser, "角色管理", "角色取消分配用户.角色名称[" + roleObj.get("role_name") + "], 用户名[" + oldUserObj.get("user_name") + "]");
	}

	@Override
	public void update4AllotFunc(String adminUser, String roleId, List<Map<String, String>> alAllotFunc)
			throws BusinessException {
		if(roleId == null) {
			throw new BusinessException("为角色取消分配功能错误, 缺少角色ID.");
		}
		if(alAllotFunc == null) {
			throw new BusinessException("为角色取消分配功能错误, 缺少功能权限.");
		}
		StringBuffer sbUpdateSql = new StringBuffer();
		String preAllotPreSql = ConfigSQLUtil.getCacheSql("alarm-role-addRoleFunc");
		String preUnAllotPreSql = ConfigSQLUtil.getCacheSql("alarm-role-deleteRoleFunc");
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("roleId", roleId);
		paramsMap.put("resType", "M");
		for(Map<String, String> item : alAllotFunc) {
			String menuId = item.get("id").toString();
			String action = item.get("action").toString();
			if("A".equals(action)) {
				paramsMap.put("id", UUIDGenerator.getUUID());
				paramsMap.put("resId", menuId);
				String preAllotSql = ConfigSQLUtil.preProcessSQL(preAllotPreSql, paramsMap);
				sbUpdateSql.append(preAllotSql).append(";\n");				
			}else if("D".equals(action)) {
				paramsMap.put("id", UUIDGenerator.getUUID());
				paramsMap.put("resId", menuId);
				String preAllotSql = ConfigSQLUtil.preProcessSQL(preUnAllotPreSql, paramsMap);
				sbUpdateSql.append(preAllotSql).append(";\n");				
			}
		}
		
		try {
			springJdbcDao.batchUpdate(sbUpdateSql.toString().split(";\n"));
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("为角色分配功能, 保存数据异常.");
		}
	}
}
