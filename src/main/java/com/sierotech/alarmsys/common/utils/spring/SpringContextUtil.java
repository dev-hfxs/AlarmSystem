package com.sierotech.alarmsys.common.utils.spring;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.xml.sax.InputSource;

import com.sierotech.alarmsys.common.utils.ConfigPojo;

public class SpringContextUtil
  implements ApplicationContextAware
{
  private static Logger log = LoggerFactory.getLogger(SpringContextUtil.class);

  private static final ResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
  private static ApplicationContext applicationContext;

  public void setApplicationContext(ApplicationContext context)
    throws BeansException
  {
    applicationContext = context;
    initAutoRun();
  }

  public static ApplicationContext getApplicationContext() {
	  if (applicationContext == null) {
		log.error("applicaitonContext未初始化！");
		throw new IllegalStateException("applicaitonContext未初始化！");
	  }
    return  applicationContext;
  }

  public static <T> T getInterfaceBean(Class<T> clazz)
  {
    Map<String, T> maps = applicationContext.getBeansOfType(clazz);
    String key = null;
    for (String s : maps.keySet()) {
      if (key != null) {
        break;
      }
      key = s;
    }

    return maps.get(key);
  }

  public static <T> T getInterfaceBean(String className, Class<T> clazz)
  {
	Map<String, T> maps = applicationContext.getBeansOfType(clazz);
    T t = null;
    for (String s : maps.keySet()) {
      if (AopUtils.getTargetClass(maps.get(s)).getName().equals(className)) {
        t = maps.get(s);
        break;
      }
    }
    return t;
  }

  public static <T> T getBean(String name) throws BeansException
  {
    return (T) applicationContext.getBean(name);
  }

  public static <T> T getBean(String name, Class<T> clazz) throws BeansException
  {
    return applicationContext.getBean(name, clazz);
  }

  private void initAutoRun() {
    Map<String, IAutoRunBean> beans = applicationContext.getBeansOfType(IAutoRunBean.class);
    List iAutoRunBean = new ArrayList();
    for (IAutoRunBean bean : beans.values()) {
      Class targetClass = AopUtils.getTargetClass(bean);
      if (!iAutoRunBean.contains(targetClass)) {
        initAutoRun(bean, iAutoRunBean);
      }
    }
    iAutoRunBean.clear();
  }

  private void initAutoRun(IAutoRunBean bean, List<Class<?>> iAutoRunBean) {
    Class targetClass = AopUtils.getTargetClass(bean);
    InitBeanOrder order = (InitBeanOrder)targetClass.getAnnotation(InitBeanOrder.class);
    if (order != null) {
      Class[] classes = order.value();
      for (Class c : classes) {
        if (!iAutoRunBean.contains(c)) {
          IAutoRunBean ib = (IAutoRunBean)getInterfaceBean(c.getName(), IAutoRunBean.class);
          initAutoRun(ib, iAutoRunBean);
        }
      }
    }
    bean.run();
    iAutoRunBean.add(targetClass);
  }

  public static List<ConfigPojo> getFilesByClasspath(List<String> classpath) {
    List result = new ArrayList();
    for (String path : classpath) {
      try {
        Resource[] resources = resourceLoader.getResources(path);
        for (Resource resource : resources) {
          URL url = resource.getURL();
          String p = url.getPath();
          int i = p.lastIndexOf("/");
          i = i == -1 ? 0 : i;
          String name = p.substring(i + 1);
          InputSource is = new InputSource(url.toExternalForm());
          ConfigPojo cp = new ConfigPojo(is, name);
          if (!result.contains(cp))
            result.add(cp);
        }
      }
      catch (IOException e) {
        log.error(e.getMessage());
      }
    }
    return result;
  }
}