package com.sierotech.alarmsys.common.utils;


import java.util.ArrayList;
import java.util.List;
import org.dom4j.Attribute;
import org.dom4j.Element;

public class Node
  implements INode
{
  private org.dom4j.Node node;

  public Node(org.dom4j.Node node)
  {
    this.node = node;
  }

  public String getValue()
  {
    return this.node.getText();
  }

  public String getAttrValue(String key)
  {
    Element elm = (Element)this.node;
    Attribute attr = elm.attribute(key);
    return attr == null ? null : attr.getValue();
  }

  public INode getSubNode(String path)
  {
    return new Node(this.node.selectSingleNode(path));
  }

  public List<INode> getSubNodes(String path)
  {
    List<org.dom4j.Node> nodes = this.node.selectNodes(path);
    List newNodes = new ArrayList();
    for (org.dom4j.Node n : nodes) {
      INode newNode = new Node(n);
      newNodes.add(newNode);
    }
    return newNodes;
  }
}