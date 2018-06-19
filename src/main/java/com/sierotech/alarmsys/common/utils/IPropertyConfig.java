package com.sierotech.alarmsys.common.utils;


public abstract interface IPropertyConfig
{
  public abstract String getString(String paramString);

  public abstract Integer getInt(String paramString);

  public abstract Boolean getBoolean(String paramString);

  public abstract Double getDouble(String paramString);

  public abstract Float getFloat(String paramString);

  public abstract Long getLong(String paramString);

  public abstract Short getShort(String paramString);
}