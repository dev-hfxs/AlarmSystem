package com.sierotech.alarmsys.common.utils;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.sierotech.alarmsys.cache.CacheException;
import com.sierotech.alarmsys.cache.EhCacheProvider;
import com.sierotech.alarmsys.cache.ObjCache;
import com.sierotech.alarmsys.common.Constants;

public class ConfigSQLUtil {
	private static Logger log = LoggerFactory.getLogger(ConfigSQLUtil.class);
	private static final String escape = " ESCAPE '/' ";

//	private static final String dbType = ConfigFactory.getPropertyConfig("dbConfig.properties").getString("dbType");
	
	public static String preProcessSQL(String sql, Map<String, ?> params) {
		Assert.hasText(sql, "ConfigSQLUtil-preProcessSQL, sql should have text");
		StringBuilder sb = new StringBuilder(sql);
		String regex = "\\[[^\\]]*(:{0})[^\\]]*\\]";
		Pattern p = null;
		Matcher m = null;
		String s = null;
		for (Iterator localIterator = params.keySet().iterator(); localIterator
				.hasNext();) {
			Object key = localIterator.next();
			p = Pattern.compile(
					MessageFormat.format(regex, new Object[] { key }), 10);
			String value = null;
			if (StringUtils.isNotEmpty(params.get(key))) {
				value = params.get(key).toString();
			}
			boolean isNull = value == null;
			s = sb.toString();
			sb.setLength(0);
			m = p.matcher(s);
			int start = 0;
			int end = 0;
			while (m.find()) {
				int start0 = m.start();
				int end0 = end = m.end();
				sb.append(s.substring(start, start0));
				if (!isNull) {
					int start1 = m.start(1);
					int end1 = m.end(1);
					String str0 = m.group().toUpperCase();
					boolean isEscape = false;
//					value = value.replaceAll("'", "''");
					if (str0.matches(".*\\s+LIKE\\s+.*")) {
						String s1 = value.replaceAll("([%_/])", "/$1");
						if (!value.equals(s1)) {
							value = s1;
							isEscape = true;
						}
					}
					sb.append(s.substring(start0 + 1, start1)).append(value)
							.append(s.substring(end1, end0 - 1))
							.append(isEscape ? " ESCAPE '/' " : "");
					start = end0 + 1;
				} else {
					start = end0;
				}
			}
			if (end < s.length()) {
				sb.append(s.substring(end, s.length()));
			}
		}
		int start = 0;
		int end = 0;
		p = Pattern.compile(MessageFormat.format(regex, new Object[] { "" }));
		s = sb.toString();
		sb.setLength(0);
		m = p.matcher(s);
		while (m.find()) {
			int start0 = m.start();
			int end0 = end = m.end();
			sb.append(s.substring(start, start0));
			start = end0;
			log.debug(m.group() + "没有找到匹配项");
		}
		if (end < s.length()) {
			sb.append(s.substring(end, s.length()));
		}
		return withNoLikeNoBrackets(withLikeNoBrackets(sb.toString(), params),
				params);
	}

	private static String withLikeNoBrackets(String sql, Map<String, ?> params) {
		StringBuilder sb = new StringBuilder(sql);
		String regex = "\\sLIKE\\s+\\S*(:{0})[^\\s]*\\s";
		Pattern p = null;
		Matcher m = null;
		String s = null;
		for (Iterator localIterator = params.keySet().iterator(); localIterator
				.hasNext();) {
			Object key = localIterator.next();
			p = Pattern.compile(
					MessageFormat.format(regex, new Object[] { key }), 10);
			String value = null;
			if (params.get(key) != null) {
				value = params.get(key).toString();
			}
			boolean isNull = value == null;
			s = sb.toString();
			sb.setLength(0);
			m = p.matcher(s);
			int start = 0;
			int end = 0;
			while (m.find()) {
				int start0 = m.start();
				int end0 = end = m.end();
				sb.append(s.substring(start, start0));
				if (!isNull) {
					int start1 = m.start(1);
					int end1 = m.end(1);
					boolean isEscape = false;
//					value = value.replaceAll("'", "''");
					String s1 = value.replaceAll("([%_/])", "/$1");
					if (!value.equals(s1)) {
						value = s1;
						isEscape = true;
					}
					sb.append(s.substring(start0, start1)).append(value)
							.append(s.substring(end1, end0))
							.append(isEscape ? " ESCAPE '/' " : "");
				}
				start = end0;
			}
			if (end < s.length()) {
				sb.append(s.substring(end, s.length()));
			}
		}
		return sb.toString();
	}

	private static String withNoLikeNoBrackets(String sql, Map<String, ?> params) {
		StringBuilder sb = new StringBuilder(sql);
		String regex = ":{0}";
		Pattern p = null;
		Matcher m = null;
		String s = null;
		for (Iterator localIterator = params.keySet().iterator(); localIterator
				.hasNext();) {
			Object key = localIterator.next();
			p = Pattern.compile(
					MessageFormat.format(regex, new Object[] { key }), 10);
			String value = null;
			if (params.get(key) != null) {
				value = params.get(key).toString();
			}
			boolean isNull = value == null;
			s = sb.toString();
			sb.setLength(0);
			m = p.matcher(s);
			int start = 0;
			int end = 0;
			while (m.find()) {
				int start0 = m.start();
				int end0 = end = m.end();
				sb.append(s.substring(start, start0));
				if (!isNull) {
					sb.append(value);
				}
				start = end0;
			}
			if (end < s.length()) {
				sb.append(s.substring(end, s.length()));
			}
		}
		return sb.toString();
	}

	public static String getPageSql(String sql, int pageNo, int pageSize,String dbType) {
		int beginRow = (pageNo - 1) * pageSize;
		int endRow = beginRow + pageSize;
		StringBuffer str = new StringBuffer();
		if ("ORACLE".equals(dbType.toUpperCase())) {
			str.append("SELECT * FROM (SELECT ROW_.*,ROWNUM ROWNUM_ FROM ( ");
			str.append(sql);
			str.append(")ROW_ ) WHERE ROWNUM_ <= ");
			str.append(endRow);
			str.append(" AND ROWNUM_ > ");
			str.append(beginRow);
		} else if ("MYSQL".equals(dbType.toUpperCase())) {
			str.append(sql);
		    str.append(" limit ");
		    str.append(beginRow);
		    str.append(", ");
		    str.append(pageSize);
		} else {
			str.append(sql);
		}

		log.debug("--pagedQuery--sql-- " + str);

		return str.toString();
	}
	
	public static String getCountSql(String sql){
		String countSql = "SELECT COUNT(*) as totalRows FROM (" + sql + ") t_temp ";
		return countSql;
	}
	/*
	 * *
	 */
	public static String getCacheSql(String sqlId){
		ObjCache cache;
		String preSql = null;
		try {
			cache = EhCacheProvider.getInstance().getCache(Constants.CACHE_NAME_SQLPOOL);
			preSql = cache.get(sqlId).toString();
		} catch (CacheException e) {
			log.error("异常,获取sqlId{}的sql",sqlId);
			e.printStackTrace();
		}
		return preSql;
	}
}
