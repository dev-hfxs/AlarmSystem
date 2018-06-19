package com.sierotech.alarmsys.common.utils;

import java.io.Serializable;
import org.xml.sax.InputSource;

public class ConfigPojo
  implements Serializable
{
  private InputSource is;
  private String name;

  public ConfigPojo(InputSource is, String name)
  {
    this.is = is;
    if (name == null)
      this.name = "";
    else
      this.name = name;
  }

  public InputSource getIs()
  {
    return this.is;
  }

  public void setIs(InputSource is) {
    this.is = is;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean equals(Object obj)
  {
    if ((obj == null) || (!(obj instanceof ConfigPojo))) {
      return false;
    }
    ConfigPojo an = (ConfigPojo)obj;
    if (this.name.equals(an.name)) {
      return true;
    }
    return false;
  }

  public int hashCode()
  {
    return this.name.hashCode();
  }
}