package com.sierotech.alarmsys.common.utils;

import java.util.Iterator;

public class StringUtils
{
  public static final String EMPTY = "";

  public static boolean isEmpty(CharSequence cs)
  {
    return (cs == null) || (cs.length() == 0);
  }

  public static <T> boolean isEmpty(T t) {
    return (t == null) || (isEmpty(t.toString()));
  }

  public static boolean isTrimEmpty(CharSequence cs) {
    return (cs == null) || (cs.length() == 0) || 
      (cs.toString().trim().length() == 0);
  }

  public static <T> boolean isTrimEmpty(T t) {
    return (t == null) || (isEmpty(t.toString()));
  }

  public static boolean isNotEmpty(CharSequence cs)
  {
    return !isEmpty(cs);
  }

  public static <T> boolean isNotEmpty(T t) {
    return (t != null) && (isNotEmpty(t.toString()));
  }

  public static String trimToEmpty(String str)
  {
    return str == null ? "" : str.trim();
  }

  public static <T> String parseToString(T t) {
    if ((t == null) || ((t instanceof String)))
      return trimToEmpty((String)t);
    if ((t instanceof byte[])) {
      return trimToEmpty(new String((byte[])t));
    }
    return trimToEmpty(String.valueOf(t));
  }

  public static <T extends CharSequence> T defaultIfEmpty(T str, T defaultStr)
  {
    return isEmpty(str) ? defaultStr : str;
  }

  public static boolean isBlank(CharSequence cs)
  {
    int strLen;
    if ((cs == null) || ((strLen = cs.length()) == 0))
      return true;
    for (int i = 0; i < strLen; i++) {
      if (!Character.isWhitespace(cs.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  public static boolean isNotBlank(CharSequence cs)
  {
    return !isBlank(cs);
  }

  public static String changeFirstCharacterCase(String str, boolean[] capitalizes)
  {
    if ((str == null) || (str.length() == 0)) {
      return str;
    }
    StringBuilder sb = new StringBuilder(str.length());
    char c = str.charAt(0);
    boolean capitalize = true;
    if ((capitalizes == null) || (capitalizes.length == 0))
      capitalize = Character.isLowerCase(c);
    else {
      capitalize = capitalizes[0];
    }
    if (capitalize)
      sb.append(Character.toUpperCase(str.charAt(0)));
    else {
      sb.append(Character.toLowerCase(str.charAt(0)));
    }
    sb.append(str.substring(1));
    return sb.toString();
  }

  public static String initialLetter(String fildeName)
  {
    byte[] items = fildeName.getBytes();
    if (items.length > 0) {
      byte item = items[0];
      items[0] = 
        (byte)((char)items[0] + (item >= 97 ? -32 : 
        ' '));
      return new String(items);
    }
    return null;
  }

  public static String capitalize(String str)
  {
    int strLen;
    if ((str == null) || ((strLen = str.length()) == 0))
      return str;
    return strLen + 
      Character.toTitleCase(str.charAt(0)) + 
      str.substring(1);
  }

  public static <T> String join(T[] elements)
  {
    return join(new Object[][] { elements, null });
  }

  public static String join(Iterator<?> iterator, char separator)
  {
    if (iterator == null) {
      return null;
    }
    if (!iterator.hasNext()) {
      return "";
    }
    Object first = iterator.next();
    if (!iterator.hasNext()) {
      String result = first == null ? "" : first.toString();
      return result;
    }

    StringBuilder buf = new StringBuilder(256);
    if (first != null) {
      buf.append(first);
    }

    while (iterator.hasNext()) {
      buf.append(separator);
      Object obj = iterator.next();
      if (obj != null) {
        buf.append(obj);
      }
    }

    return buf.toString();
  }

  public static String join(Iterator<?> iterator, String separator)
  {
    if (iterator == null) {
      return null;
    }
    if (!iterator.hasNext()) {
      return "";
    }
    Object first = iterator.next();
    if (!iterator.hasNext()) {
      String result = first == null ? "" : first.toString();
      return result;
    }

    StringBuilder buf = new StringBuilder(256);
    if (first != null) {
      buf.append(first);
    }

    while (iterator.hasNext()) {
      if (separator != null) {
        buf.append(separator);
      }
      Object obj = iterator.next();
      if (obj != null) {
        buf.append(obj);
      }
    }
    return buf.toString();
  }

  public static String join(Iterable<?> iterable, char separator)
  {
    if (iterable == null) {
      return null;
    }
    return join(iterable.iterator(), separator);
  }
}