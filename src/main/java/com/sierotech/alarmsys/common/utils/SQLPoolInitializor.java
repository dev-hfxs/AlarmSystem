package com.sierotech.alarmsys.common.utils;

import java.util.Arrays;
import java.util.List;

import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sierotech.alarmsys.cache.CacheException;
import com.sierotech.alarmsys.cache.CacheProvider;
import com.sierotech.alarmsys.cache.EhCacheProvider;
import com.sierotech.alarmsys.cache.ObjCache;
import com.sierotech.alarmsys.common.Constants;
import com.sierotech.alarmsys.common.utils.ConfigFactory;
import com.sierotech.alarmsys.common.utils.spring.SpringContextUtil;


/**
 * @author liweiming
 *
 */
@Component
public class SQLPoolInitializor{
	static final Logger log = LoggerFactory.getLogger(SQLPoolInitializor.class);
	public  void run() {
		String dbType = ConfigFactory.getPropertyConfig("dbConfig.properties").getString("dbType");
		List<String> sqlPaths = null;
		if(dbType != null){
			sqlPaths = Arrays.asList(new String[] { "classpath:**/"+dbType+"/sqlpool-*.xml", "classpath*:config/**/" + dbType +"/sqlpool-*.xml" });
		}else{
			sqlPaths = Arrays.asList(new String[] { "classpath:**/sqlpool-*.xml", "classpath*:config/**/sqlpool-*.xml" });
		}
		List<ConfigPojo> sqlXmlFiles = SpringContextUtil.getFilesByClasspath(sqlPaths);
		CacheProvider cacheProvider = EhCacheProvider.getInstance();
		ObjCache cache = null;
		try {
			cacheProvider.buildCache(Constants.CACHE_NAME_SQLPOOL, String.class, String.class, null);
			cache = cacheProvider.getCache(Constants.CACHE_NAME_SQLPOOL);
		} catch (CacheException ce) {
			log.error("建立SQL语句缓存对象失败.");
			ce.printStackTrace();
		}
		for (ConfigPojo cp : sqlXmlFiles) {
			try {
				parse(cp,cache);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
	    }
	}
	
	public  void parse(ConfigPojo cp,ObjCache cache) throws DocumentException{
		String name = cp.getName();
	    name = name.substring(name.indexOf("-") + 1, name.length() - 4);
	    SAXReader reader = new SAXReader();
	    IXMLConfig config = new XMLConfigImpl(reader.read(cp.getIs()).getDocument());
	    List<INode> sqlNodes = config.getNodes("sql");
	    for (INode node : sqlNodes) {
	      String sqlId = node.getAttrValue("id");
	      if ((sqlId != null) && (sqlId.matches("^" + name + "-.+"))) {
	        String content = node.getSubNode("content").getValue();
	        try {
	        	cache.put(sqlId, StringUtils.parseToString(content));
			} catch (CacheException e) {
				e.printStackTrace();
			}
	      } else {
	         log.error("sqlpool-" + name + ".xml中 id=" + sqlId + "不符合要求,请检查");
	      }
	    }
	}
}
