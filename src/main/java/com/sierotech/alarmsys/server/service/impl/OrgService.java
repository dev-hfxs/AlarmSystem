/**
* <p>版权所有:(C)2018-2022 天津航峰希萨科技有限公司 </p>
* @创建人: lwm
* @创建日期: 2018年7月14日
* @修改人: 
* @修改日期：
* @描述: 
 */
package com.sierotech.alarmsys.server.service.impl;

import java.util.ArrayList;
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
import com.sierotech.alarmsys.common.utils.DateUtils;
import com.sierotech.alarmsys.common.utils.LogOperationUtil;
import com.sierotech.alarmsys.common.utils.UUIDGenerator;
import com.sierotech.alarmsys.server.service.IOrgService;

/**
* @JDK版本: 1.7
* @创建人: lwm
* @创建日期：2018年7月14日
* @功能描述: 
 */

@Service
public class OrgService implements IOrgService {
	static final Logger log = LoggerFactory.getLogger(OrgService.class);
	
	@Autowired	
	private JdbcTemplate springJdbcDao;
	
	@Override
	public boolean checkOrgExist(String orgId, String parentId, String orgName) throws BusinessException {
		boolean result = true;
//		if (null == parentId || "".equals(parentId)) {
//			throw new BusinessException("上级组织不能为空!");
//		}
		if (null == orgName || "".equals(orgName)) {
			throw new BusinessException("组织名称不能为空!");
		}
		if (null == orgId) {
			return true;
		}		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("orgId", orgId);
//		paramsMap.put("parentId", parentId);
		paramsMap.put("orgName", orgName);

		String preSql = ConfigSQLUtil.getCacheSql("alarm-org-checkOrgExistsByOrgName");
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
			log.info(ex.getMessage());
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return result;
	}

	@Override
	public void addOrg(String adminUser, Map<String, Object> orgObj) throws BusinessException {
		if (null == adminUser) {
			throw new BusinessException("添加组织错误,当前操作是未知的管理员!");
		}

		if (null == orgObj.get("orgName")) {
			throw new BusinessException("添加组织错误,缺少组织名称!");
		}
		if (null == orgObj.get("orgCode")) {
			throw new BusinessException("添加组织参数错误,缺少组织编码.");
		}
		if (null == orgObj.get("parentId")) {
			throw new BusinessException("添加组织参数错误,缺少上级组织.");
		}
		if (null == orgObj.get("orderNum") || "".equals(orgObj.get("orderNum").toString())) {
			orgObj.put("orderNum", "0");
		}
		String parentId = orgObj.get("parentId").toString();
		// 检查单位名是否重复
		boolean orgExists = checkOrgExist("", parentId, orgObj.get("orgName").toString());
		if (orgExists) {
			throw new BusinessException("组织名称已存在!");
		}
		String orgId = UUIDGenerator.getUUID();
		orgObj.put("orgId", orgId);
		orgObj.put("creator", adminUser);
		orgObj.put("createDate", DateUtils.getNow(DateUtils.FORMAT_LONG));
		String preSql = ConfigSQLUtil.getCacheSql("alarm-org-addOrg");
		String sql = ConfigSQLUtil.preProcessSQL(preSql, orgObj);
		try {
			springJdbcDao.update(sql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("添加组织访问数据库异常.");
		}
		// 记录日志
		LogOperationUtil.logAdminOperation(adminUser, "组织管理", "添加组织:[" + orgObj.get("orgName").toString() + "].");
	}

	@Override
	public void updateOrg(String adminUser, Map<String, Object> orgObj) throws BusinessException {
		if (null == adminUser) {
			throw new BusinessException("修改组织错误,当前操作是未知的管理员!");
		}

		if (null == orgObj.get("orgId")) {
			throw new BusinessException("修改组织错误,缺少组织ID!");
		}
		if (null == orgObj.get("orgName")) {
			throw new BusinessException("修改组织错误,缺少组织名!");
		}
		if (null == orgObj.get("parentId")) {
			throw new BusinessException("修改组织错误,缺少上级组织.");
		}
		if (null == orgObj.get("orderNum") || "".equals(orgObj.get("orderNum").toString())) {
			orgObj.put("orderNum", "0");
		}
		// 检查单位名是否重复
		boolean orgExists = checkOrgExist(orgObj.get("orgId").toString(), orgObj.get("parentId").toString(), orgObj.get("orgName").toString());
		if (orgExists) {
			throw new BusinessException("组织名已存在!");
		}
		// 先获取单位
		String preSelectSql = ConfigSQLUtil.getCacheSql("alarm-org-getOrgById");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("orgId", orgObj.get("orgId").toString());
		String selectSql = ConfigSQLUtil.preProcessSQL(preSelectSql, paramsMap);
		List<Map<String, Object>> alOrgs;
		try {
			alOrgs = springJdbcDao.queryForList(selectSql);
		} catch (DataAccessException dae) {
			log.info(dae.getMessage());
			throw new BusinessException("修改组织错误,获取组织访问数据库异常.");
		}
		Map<String, Object> oldOrgObj;
		if (alOrgs != null && alOrgs.size() > 0) {
			oldOrgObj = alOrgs.get(0);
		} else {
			throw new BusinessException("修改组织错误,未查询到组织.");
		}
				
		String preSql = ConfigSQLUtil.getCacheSql("alarm-org-updateOrg");
		String sql = ConfigSQLUtil.preProcessSQL(preSql, orgObj);
		try {
			springJdbcDao.update(sql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("修改组织,访问数据库异常.");
		}
		// 记录日志
		LogOperationUtil.logAdminOperation(adminUser, "组织管理", "修改组织:[" + orgObj.get("orgName").toString() + "];修改前的组织名:"+oldOrgObj.get("org_name").toString());
		
	}

	@Override
	public void deleteOrg(String adminUser, String orgId) throws BusinessException {
		if (null == adminUser) {
			throw new BusinessException("删除组织错误,当前操作是未知的管理员!");
		}
		if (null == orgId) {
			throw new BusinessException("删除组织错误,缺少组织ID!");
		}

		// 先获取单位
		String preSelectSql = ConfigSQLUtil.getCacheSql("alarm-org-getOrgById");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("orgId", orgId);
		String selectSql = ConfigSQLUtil.preProcessSQL(preSelectSql, paramsMap);
		List<Map<String, Object>> alOrgs;
		try {
			alOrgs = springJdbcDao.queryForList(selectSql);
		} catch (DataAccessException dae) {
			throw new BusinessException("删除组织错误,获取组织访问数据库异常.");
		}
		Map<String, Object> orgObj;
		if (alOrgs != null && alOrgs.size() > 0) {
			orgObj = alOrgs.get(0);
		} else {
			throw new BusinessException("删除组织错误,未查询到组织.");
		}

		// 检查单位下面是否维护了人员
		String checkOrgHasUserPreSql = ConfigSQLUtil.getCacheSql("alarm-org-checkOrgHasUser");
		String checkOrgHasUserSql = ConfigSQLUtil.preProcessSQL(checkOrgHasUserPreSql, paramsMap);
		int num = 0;
		try {
			Map<String, Object> recordMap = springJdbcDao.queryForMap(checkOrgHasUserSql);
			if (recordMap != null) {
				num = Integer.valueOf(recordMap.get("countNum").toString());
			}
		} catch (DataAccessException ex) {
			log.info(ex.getMessage());
		} catch (Exception ex) {
			log.info(ex.getMessage());
		}
		if(num > 0) {
			throw new BusinessException("该组织下面维护了人员, 不能删除.");
		}
		// 检查单位下面是否存在下级组织
		String checkOrgHasChildrenPreSql = ConfigSQLUtil.getCacheSql("alarm-org-checkOrgHasChildren");
		String checkOrgHasChildrenSql = ConfigSQLUtil.preProcessSQL(checkOrgHasChildrenPreSql, paramsMap);
		num = 0;
		try {
			Map<String, Object> recordMap = springJdbcDao.queryForMap(checkOrgHasChildrenSql);
			if (recordMap != null) {
				num = Integer.valueOf(recordMap.get("countNum").toString());
			}
		} catch (DataAccessException ex) {
			log.info(ex.getMessage());
		} catch (Exception ex) {
			log.info(ex.getMessage());
		}
		if(num > 0) {
			throw new BusinessException("该组织下面包含下级组织, 不能删除.");
		}
				
		String preUpdateSql = ConfigSQLUtil.getCacheSql("alarm-org-deleteOrgById");
		paramsMap.clear();
		paramsMap.put("orgId", orgId);
		String updateSql = ConfigSQLUtil.preProcessSQL(preUpdateSql, paramsMap);
		try {
			springJdbcDao.update(updateSql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("删除组织错误,访问数据库异常.");
		}
		// 记录日志
		LogOperationUtil.logAdminOperation(adminUser, "组织管理", "删除组织:[" + orgObj.get("org_name").toString() + "].");
	}

	@Override
	public void updateOrg4Recover(String adminUser, String orgId) throws BusinessException {
		if (null == adminUser) {
			throw new BusinessException("恢复组织错误,当前操作是未知的管理员!");
		}
		if (null == orgId) {
			throw new BusinessException("恢复组织错误,缺少单位ID!");
		}

		// 先获取组织
		String preSelectSql = ConfigSQLUtil.getCacheSql("alarm-org-getOrgById");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("orgId", orgId);
		String selectSql = ConfigSQLUtil.preProcessSQL(preSelectSql, paramsMap);
		List<Map<String, Object>> alOrgs;
		try {
			alOrgs = springJdbcDao.queryForList(selectSql);
		} catch (DataAccessException dae) {
			log.info(dae.getMessage());
			throw new BusinessException("恢复组织错误,获取组织访问数据库异常.");
		}
		Map<String, Object> orgObj;
		if (alOrgs != null && alOrgs.size() > 0) {
			orgObj = alOrgs.get(0);
		} else {
			throw new BusinessException("恢复组织错误,未查询到组织.");
		}

		String preUpdateSql = ConfigSQLUtil.getCacheSql("alarm-org-recoverOrgById");
		paramsMap.clear();
		paramsMap.put("orgId", orgId);
		String updateSql = ConfigSQLUtil.preProcessSQL(preUpdateSql, paramsMap);
		try {
			springJdbcDao.update(updateSql);
		} catch (DataAccessException dae) {
			log.info(dae.toString());
			throw new BusinessException("恢复组织错误,访问数据库异常.");
		}
		// 记录日志
		LogOperationUtil.logAdminOperation(adminUser, "组织管理", "恢复组织:[" + orgObj.get("org_name").toString() + "].");
	}


	@Override
	public List<Map<String, Object>> getOrgTreeData(String parentId, boolean valid) throws BusinessException {
		// 获取组织
		List<Map<String, Object>> orgTreeData = new ArrayList<Map<String,Object>>();
		
		String preSql;
		if(valid == true) {
			preSql = ConfigSQLUtil.getCacheSql("alarm-org-getOrgTreeValidDataById");
		}else {
			preSql = ConfigSQLUtil.getCacheSql("alarm-org-getOrgTreeDataById");
		}
		Map<String, String> paramMap = new HashMap<String, String>();
	    paramMap.put("orgId", parentId);
		String sql = ConfigSQLUtil.preProcessSQL(preSql, paramMap);
		
		List<Map<String,Object>> datas = null;
		try {
			datas = springJdbcDao.queryForList(sql);
		}catch(DataAccessException dae) { 
			log.info(dae.getMessage());
		}
		if(datas != null && datas.size() > 0) {
			if("ROOT".equals(parentId)) {
				// 组织下级节点
				orgTreeData = groupData4Tree(datas, parentId);
			}else {
				for(Map<String, Object> record : datas) {
					if(parentId.equals(record.get("id").toString())) {
						// 组织下级节点
						List<Map<String, Object>> childrens = groupData4Tree(datas, parentId);
						record.put("children", childrens);
						orgTreeData.add(record);
						break;
					}
				}
			}
		}
		
		return orgTreeData;
	}
	
	private List<Map<String, Object>> groupData4Tree(List<Map<String, Object>> srcData ,String parentId){
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		if(srcData != null && srcData.size() > 0) {
			//遍历得到直接子级元素
			for(Map<String, Object> item : srcData) {
				if(parentId.equals(item.get("parent_id").toString())) {
					result.add(item);
				}
			}
			if(result.size() > 0 ) {
				//遍历得到直接子级元素的子级元素
				for(Map<String, Object> item : result) {
					String curId = item.get("id").toString();
					List<Map<String, Object>> curChildren = groupData4Tree(srcData, curId);
					item.put("children", curChildren);
				}
			}
		}
		return result;
	}

}
