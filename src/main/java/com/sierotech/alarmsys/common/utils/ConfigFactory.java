
package com.sierotech.alarmsys.common.utils;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigFactory
{
  private static final Logger log = LoggerFactory.getLogger(ConfigFactory.class);
  private static Map<String, IXMLConfig> xmlConfigs = new ConcurrentHashMap();
  private static Map<String, IPropertyConfig> propertyConfigs = new ConcurrentHashMap();
  private static final String defaultXMLFilename = "dft-cfg.xml";

  static
  {
    try
    {
      IXMLConfig defaultXMLConfig = getXMLConfigByFilename("dft-cfg.xml");
      if (defaultXMLConfig != null) {
        xmlConfigs.put("dft-cfg.xml", defaultXMLConfig);
      }
      log.debug("get default xml config success");
    } catch (Exception ex) {
      log.error("get default xml config error");
    }
  }

  public static IXMLConfig getXMLConfig() {
    return getXMLConfig(null);
  }

  public static IXMLConfig getXMLConfig(String fileName) {
    if (fileName == null) {
      return (IXMLConfig)xmlConfigs.get("dft-cfg.xml");
    }
    IXMLConfig config = (IXMLConfig)xmlConfigs.get(fileName);
    if (config == null) {
      try {
        config = getXMLConfigByFilename(fileName);
        xmlConfigs.put(fileName, config);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return config;
  }

  public static IPropertyConfig getPropertyConfig(String fileName)
  {
    if (fileName == null) {
      log.error("property config file name is null");
      return null;
    }
    IPropertyConfig config = (IPropertyConfig)propertyConfigs.get(fileName);
    if (config == null) {
      config = getPropertyConfigByFileName(fileName);
      propertyConfigs.put(fileName, config);
    }
    return config;
  }

  public static int getXMLConfigSize()
  {
    return xmlConfigs.size();
  }

  public static int getPropertyConfigSize() {
    return propertyConfigs.size();
  }

  private static IXMLConfig getXMLConfigByFilename(String fileName) throws Exception {
    InputStream is = ConfigFactory.class.getClassLoader().getResourceAsStream(fileName);
    if (is == null) {
      log.error("xml config file is not find, file name is :" + fileName);
      return null;
    }
    SAXReader reader = new SAXReader();
    Document document = reader.read(is);
    IXMLConfig config = new XMLConfigImpl(document);
    return config;
  }

  private static IPropertyConfig getPropertyConfigByFileName(String fileName)
  {
    InputStream is = ConfigFactory.class.getClassLoader().getResourceAsStream(fileName);
    if (is == null) {
      log.error("property config file is not find, file name is :" + fileName);
      return null;
    }
    IPropertyConfig config = new PropertyConfigImpl(is);
    return config;
  }
}