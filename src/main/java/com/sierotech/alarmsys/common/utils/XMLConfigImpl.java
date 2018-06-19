package com.sierotech.alarmsys.common.utils;

import java.util.ArrayList;
import java.util.List;
import org.dom4j.Document;

public class XMLConfigImpl
implements IXMLConfig
{
private org.dom4j.Node rootNode;

public XMLConfigImpl(Document document)
{
  this.rootNode = document.getRootElement();
}

public String getString(String path)
{
  INode node = getNode(path);
  if (node == null) return null;
  return node.getValue().trim();
}

public Integer getInt(String path)
{
  return getString(path) == null ? null : Integer.valueOf(Integer.parseInt(getString(path)));
}

public Boolean getBoolean(String path)
{
  return getString(path) == null ? null : Boolean.valueOf(Boolean.parseBoolean(getString(path)));
}

public Double getDouble(String path)
{
  return getString(path) == null ? null : Double.valueOf(Double.parseDouble(getString(path)));
}

public Float getFloat(String path)
{
  return getString(path) == null ? null : Float.valueOf(Float.parseFloat(getString(path)));
}

public Long getLong(String path)
{
  return getString(path) == null ? null : Long.valueOf(Long.parseLong(getString(path)));
}

public Short getShort(String path)
{
  return getString(path) == null ? null : Short.valueOf(Short.parseShort(getString(path)));
}

public INode getNode(String path)
{
  org.dom4j.Node node = this.rootNode.selectSingleNode(path);
  if (node == null) return null;
  return new Node(node);
}

public List<INode> getNodes(String path)
{
  List<org.dom4j.Node> nodes = this.rootNode.selectNodes(path);
  if ((nodes == null) || (nodes.size() == 0)) return null;
  List cfgNodes = new ArrayList();
  for (org.dom4j.Node node : nodes) {
    cfgNodes.add(new Node(node));
  }
  return cfgNodes;
}
}