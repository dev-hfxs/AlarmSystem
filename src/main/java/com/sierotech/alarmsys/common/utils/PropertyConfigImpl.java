package com.sierotech.alarmsys.common.utils;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyConfigImpl
  implements IPropertyConfig
{
  private Properties props;

  public PropertyConfigImpl(InputStream is)
  {
    this.props = new Properties();
    try {
      this.props.load(is);
      is.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public String getString(String key)
  {
    return this.props.getProperty(key);
  }

  public Integer getInt(String key)
  {
    return this.props.getProperty(key) == null ? null : Integer.valueOf(Integer.parseInt(this.props.getProperty(key)));
  }

  public Boolean getBoolean(String key)
  {
    return this.props.getProperty(key) == null ? null : Boolean.valueOf(Boolean.parseBoolean(this.props.getProperty(key)));
  }

  public Double getDouble(String key)
  {
    return this.props.getProperty(key) == null ? null : Double.valueOf(Double.parseDouble(this.props.getProperty(key)));
  }

  public Float getFloat(String key)
  {
    return this.props.getProperty(key) == null ? null : Float.valueOf(Float.parseFloat(this.props.getProperty(key)));
  }

  public Long getLong(String key)
  {
    return this.props.getProperty(key) == null ? null : Long.valueOf(Long.parseLong(this.props.getProperty(key)));
  }

  public Short getShort(String key)
  {
    return this.props.getProperty(key) == null ? null : Short.valueOf(Short.parseShort(this.props.getProperty(key)));
  }
}