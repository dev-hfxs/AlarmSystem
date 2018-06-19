package com.sierotech.alarmsys.common.utils;
import java.util.List;

public abstract interface IXMLConfig
{
  public abstract String getString(String paramString);

  public abstract Integer getInt(String paramString);

  public abstract Boolean getBoolean(String paramString);

  public abstract Double getDouble(String paramString);

  public abstract Float getFloat(String paramString);

  public abstract Long getLong(String paramString);

  public abstract Short getShort(String paramString);

  public abstract INode getNode(String paramString);

  public abstract List<INode> getNodes(String paramString);
}