package com.sierotech.alarmsys.common.utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.JSONUtils;

import org.apache.commons.lang.time.DateFormatUtils;

public class JsonUtil {

	private static void setDataFormat2JAVA() {
		// 设定日期转换格式
		JSONUtils.getMorpherRegistry().registerMorpher(
				new DateMorpher(new String[] { "yyyy-MM-dd",
						"yyyy-MM-dd HH:mm:ss" }));
	}

	/**
	 * 将数据对象转换成Json格式字符串
	 * 
	 * @param object
	 *            POJO、Collection或Object[]
	 * @return String
	 */
	public static String beanToJson(Object object) {
		String jsonString = null;
		// 日期值处理器
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonValueProcessor() {

					@Override
					public Object processObjectValue(String key, Object value,
							JsonConfig jsonConfig) {
						return processValue(value);
					}

					@Override
					public Object processArrayValue(Object value,
							JsonConfig jsonConfig) {
						return processValue(value);
					}

					private Object processValue(Object value) {
						if (value != null) {
							return DateFormatUtils.format((Date) value,
									"yyyy-MM-dd");
						} else {
							return "";
						}
					}
				});
		jsonConfig.registerJsonValueProcessor(Timestamp.class,
				new JsonValueProcessor() {

					@Override
					public Object processObjectValue(String key, Object value,
							JsonConfig jsonConfig) {
						return processValue(value);
					}

					@Override
					public Object processArrayValue(Object value,
							JsonConfig jsonConfig) {
						return processValue(value);
					}

					private Object processValue(Object value) {
						if (value != null) {
							String dts = DateFormatUtils.format(
									(Timestamp) value, "yyyy-MM-dd HH:mm:ss");
							if (dts.endsWith("00:00:00")) {
								return dts.substring(0, 10);
							}
							return dts;
						} else {
							return "";
						}
					}
				});
		if (object != null) {
			if (object instanceof Collection || object instanceof Object[]) {
				jsonString = JSONArray.fromObject(object, jsonConfig)
						.toString();
			} else {
				jsonString = JSONObject.fromObject(object, jsonConfig)
						.toString();
			}
		}
		return jsonString == null ? "{}" : jsonString.replace(":null", ":\"\"");
	}

	
	/**
	 * 将数据对象转换成Json格式字符串
	 * 
	 * @param object
	 *            POJO、Collection或Object[]
	 * @return String
	 */
	public static String beanToJson2(Object object) {
		String jsonString = null;
		// 日期值处理器
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonValueProcessor() {

					@Override
					public Object processObjectValue(String key, Object value,
							JsonConfig jsonConfig) {
						return processValue(value);
					}

					@Override
					public Object processArrayValue(Object value,
							JsonConfig jsonConfig) {
						return processValue(value);
					}

					private Object processValue(Object value) {
						if (value != null) {
							return DateFormatUtils.format((Date) value,
									"yyyy-MM-dd");
						} else {
							return "";
						}
					}
				});
		jsonConfig.registerJsonValueProcessor(Timestamp.class,
				new JsonValueProcessor() {

					@Override
					public Object processObjectValue(String key, Object value,
							JsonConfig jsonConfig) {
						return processValue(value);
					}

					@Override
					public Object processArrayValue(Object value,
							JsonConfig jsonConfig) {
						return processValue(value);
					}

					private Object processValue(Object value) {
						if (value != null) {
							String dts = DateFormatUtils.format(
									(Timestamp) value, "yyyy-MM-dd HH:mm:ss");
							if (dts.endsWith("00:00:00")) {
								return dts.substring(0, 10);
							}
							return dts;
						} else {
							return "";
						}
					}
				});
		if (object != null) {
			if (object instanceof Collection || object instanceof Object[]) {
				jsonString = JSONArray.fromObject(object, jsonConfig)
						.toString();
			} else {
				jsonString = JSONObject.fromObject(object, jsonConfig)
						.toString();
			}
		}
		return jsonString == null ? "{}" : jsonString;
	}
	
	/**
	 * 将Map数据对象转换成Json格式字符串
	 * 
	 * @param map
	 *            Map对象
	 * @return String
	 */
	public static String mapToJson(Map map) {
		return beanToJson(map);
	}

	/**
	 * 将Map数据对象转换成Json格式字符串,对json中的:null不进行处理
	 * 
	 * @param map
	 *            Map对象
	 * @return String
	 */
	public static String mapToJson2(Map map) {
		return beanToJson2(map);
	}
	
	/**
	 * 将Collection数据对象转换成Json格式字符串
	 * 
	 * @param coll
	 *            Collection对象
	 * @return String
	 */
	public static String listToJson(Collection coll) {
		return beanToJson(coll);
	}

	/**
	 * 将Object数组数据对象转换成Json格式字符串
	 * 
	 * @param objects
	 *            Object对象数组
	 * @return String
	 */
	public static String arrayToJson(Object[] objects) {
		return beanToJson(objects);
	}

	/**
	 * 将Json格式字符串转换成Java对象
	 * 
	 * @param jsonString
	 *            Json格式字符串
	 * @param beanClass
	 *            Java对象Class
	 * @return Object
	 */
	public static Object jsonToBean(String jsonString, Class beanClass) {
		JSONObject jsonObject = null;
		try {
			setDataFormat2JAVA();
			jsonObject = JSONObject.fromObject(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSONObject.toBean(jsonObject, beanClass);
	}

	/**
	 * 将Json格式字符串转换成Java对象
	 * 
	 * @param jsonString
	 *            Json格式字符串
	 * @param beanClass
	 *            Java对象Class
	 * @param classMap
	 *            包含的对象集合中的Java对象Class
	 * @return Object
	 */
	public static Object jsonToBean(String jsonString, Class beanClass,
			Map classMap) {
		JSONObject jsonObject = null;
		try {
			setDataFormat2JAVA();
			jsonObject = JSONObject.fromObject(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSONObject.toBean(jsonObject, beanClass, classMap);
	}

	/**
	 * 将Json格式字符串转换成Java对象数组
	 * 
	 * @param jsonString
	 *            Json格式字符串
	 * @param beanClass
	 *            Java对象Class
	 * @return Object[]
	 */
	public static Object[] jsonToArray(String jsonString, Class beanClass) {
		setDataFormat2JAVA();
		JSONArray array = JSONArray.fromObject(jsonString);
		Object[] obj = new Object[array.size()];
		for (int i = 0; i < array.size(); i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			obj[i] = JSONObject.toBean(jsonObject, beanClass);
		}
		return obj;
	}

	/**
	 * 将Json格式字符串转换成Java对象数组
	 * 
	 * @param jsonString
	 *            Json格式字符串
	 * @param beanClass
	 *            Java对象Class
	 * @param classMap
	 *            包含的对象集合中的Java对象Class
	 * @return Object[]
	 */
	public static Object[] jsonToArray(String jsonString, Class beanClass,
			Map classMap) {
		setDataFormat2JAVA();
		JSONArray array = JSONArray.fromObject(jsonString);
		Object[] obj = new Object[array.size()];
		for (int i = 0; i < array.size(); i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			obj[i] = JSONObject.toBean(jsonObject, beanClass, classMap);
		}
		return obj;
	}

	/**
	 * 将Json格式字符串转换成Java对象集合
	 * 
	 * @param jsonString
	 *            Json格式字符串
	 * @param beanClass
	 *            Java对象Class
	 * @return List
	 */
	public static List jsonToList(String jsonString, Class beanClass) {
		setDataFormat2JAVA();
		JSONArray array = JSONArray.fromObject(jsonString);
		List list = new ArrayList();
		for (Iterator iter = array.iterator(); iter.hasNext();) {
			JSONObject jsonObject = (JSONObject) iter.next();
			list.add(JSONObject.toBean(jsonObject, beanClass));
		}
		return list;
	}

	/**
	 * 将Json格式字符串转换成Java对象集合
	 * 
	 * @param jsonString
	 *            Json格式字符串
	 * @param beanClass
	 *            Java对象Class
	 * @param classMap
	 *            包含的对象集合中的Java对象Class
	 * @return
	 */
	public static List jsonToList(String jsonString, Class beanClass,
			Map classMap) {
		setDataFormat2JAVA();
		JSONArray array = JSONArray.fromObject(jsonString);
		List list = new ArrayList();
		for (Iterator iter = array.iterator(); iter.hasNext();) {
			JSONObject jsonObject = (JSONObject) iter.next();
			list.add(JSONObject.toBean(jsonObject, beanClass, classMap));
		}
		return list;
	}

	
	/**
	 * 将Json格式字符串转换成Map对象
	 * 
	 * @param jsonString
	 *            Json格式字符串
	 * @return Map
	 */
	public static Map jsonToMap(String jsonString,boolean keyLowcase) {
		setDataFormat2JAVA();
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		Map map = new HashMap();
		if(keyLowcase){
			for (Iterator iter = jsonObject.keys(); iter.hasNext();) {
				String key = (String) iter.next();
//				map.put(key.toLowerCase(), jsonObject.get(key));
				map.put(key.toLowerCase(), jsonObject.get(key).toString());
			}
		}else{
			for (Iterator iter = jsonObject.keys(); iter.hasNext();) {
				String key = (String) iter.next();
//				map.put(key, jsonObject.get(key));
				map.put(key, jsonObject.get(key).toString());
			}
		}
		return map;
	}

	/**
	 * 将Json格式字符串转换成Object对象数组
	 * 
	 * @param jsonString
	 *            Json格式字符串
	 * @return Object[]
	 */
	public static Object[] jsonToArray(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		return jsonArray.toArray();
	}

	/**
	 * 判断JSON对象是否为空
	 * 
	 * @param jsonObj
	 *            JSON对象
	 * @return 是否为空
	 */
	public static boolean jsonIsEmpty(JSONObject jsonObj) {
		return jsonObj == null || jsonObj.isNullObject() || jsonObj.isEmpty();
	}

	/**
	 * 判断JSON数组对象是否为空
	 * 
	 * @param jsonArr
	 *            JSON数组对象
	 * @return 是否为空
	 */
	public static boolean jsonIsEmpty(JSONArray jsonArr) {
		return jsonArr == null || jsonArr.isEmpty();
	}

	/**
	 * 将JSONArray对象转换为字符串
	 * 
	 * @param jsonArr
	 *            JSONArray对象--分隔字符串为“,”
	 * @return
	 */
	public static String jsonArray2Str(JSONArray jsonArr) {
		return jsonArray2Str(jsonArr, null);
	}

	/**
	 * 将JSONArray对象转换为字符串
	 * 
	 * @param jsonArr
	 *            JSONArray对象
	 * @param splitStr
	 *            分隔字符串--默认为“,”
	 * @return
	 */
	public static String jsonArray2Str(JSONArray jsonArr, String splitStr) {
		StringBuffer stringBuffer = new StringBuffer();

		if (splitStr == null || "".equals(splitStr)) {
			splitStr = ",";
		}

		for (Object str : jsonArr) {
			stringBuffer.append(splitStr).append(str);
		}

		String s = stringBuffer.toString();
		if (s.length() == 0) {
			return "";
		}

		return s.substring(1);
	}
}
