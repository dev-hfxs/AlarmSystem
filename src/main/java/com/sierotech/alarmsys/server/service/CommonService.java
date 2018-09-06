/**
* <p>版权所有:(C)2018-2022 天津航峰希萨科技有限公司 </p>
* @创建人: lwm
* @创建日期: 2018年4月17日
* @修改人: 
* @修改日期：
* @描述: 
 */
package com.sierotech.alarmsys.server.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sierotech.alarmsys.common.utils.ConfigSQLUtil;


/**
 * @JDK版本: 1.7
 * @创建人: lwm
 * @创建日期：2018年4月17日 @功能描述:
 */
@Service
@Transactional
public class CommonService {
	private static final Logger log = LoggerFactory.getLogger(CommonService.class);
	
	@Autowired
	private JdbcTemplate springJdbcDao;

	public List<Map<String, Object>> queryForList(String sqlId, Map<String, String> paramMap) {
		List<Map<String, Object>> result = null;
		String preSql = ConfigSQLUtil.getCacheSql(sqlId);
		if(preSql == null || "".equals(preSql)) {
			return null;
		}
		String sql = ConfigSQLUtil.preProcessSQL(preSql, paramMap);
		result = springJdbcDao.queryForList(sql);
		return result;
	}

	public Map<String, Object> queryForPage(Map<String, String> paramMap) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> listData = null;
		int totalRows = 0;
		String sqlId = paramMap.get("sqlId");
		if (null == sqlId || "".equals(sqlId)) {
			return null;
		}
		int pageNo = new Integer(paramMap.get("page"));
		int pageSize = new Integer(paramMap.get("rows"));
		result.put("total", totalRows);
		result.put("pageNo", pageNo);
		result.put("pageSize", pageSize);
		
		String sortColumns = paramMap.get("sort");
		String sortOrders = paramMap.get("order");
		String sqlOrderPart = "";
		if(sortColumns != null && sortColumns.length() > 0) {
			String[] arySortColumn = sortColumns.split(",");
			String[] arySortOrder = sortOrders.split(",");
			if(arySortColumn != null && arySortOrder != null) {
				for(int i=0; i<arySortColumn.length;i++ ) {
					String column = arySortColumn[i];
					String order = arySortOrder[i];
					sqlOrderPart = ", " +column + " " + order;
				}
				if(null !=sqlOrderPart && sqlOrderPart.length() > 0) {
					sqlOrderPart = sqlOrderPart.substring(1, sqlOrderPart.length());
				}				
			}			
		}
		
		String preSql = ConfigSQLUtil.getCacheSql(sqlId);
		if (null == preSql || "".equals(preSql)) {
			return null;
		}
		String sql = ConfigSQLUtil.preProcessSQL(preSql, paramMap);

		String countSql = ConfigSQLUtil.getCountSql(sql);
		//有排序的字段参数，则增加排序
		if(sqlOrderPart.length() > 0) {
			sql = " SELECT * FROM (" + sql + ") ttd ORDER BY " + sqlOrderPart;
		}
		
		String sTotalRows = springJdbcDao.queryForMap(countSql).get("totalRows").toString();
		if (sTotalRows != null) {
			totalRows = new Integer(sTotalRows);
		}
		if (totalRows > 0) {
			String pageSql = ConfigSQLUtil.getPageSql(sql, pageNo, pageSize, "MYSQL");
			listData = springJdbcDao.queryForList(pageSql);
		}

		result.put("total", totalRows);
		result.put("rows", listData);
		return result;
	}
}
