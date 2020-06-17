package com.epam.esm.util;

public class QueryHelper {
  public static String getSortQuery(String queryString) {
    StringBuilder stringBuilder = new StringBuilder();
    String[] strings = queryString.trim().split("[;,.|]+");
    for (String string : strings) {
      string = string.trim().toLowerCase();
      if (string.charAt(0) == '-') {
        stringBuilder.append(string.substring(1)).append(" DESC, ");
      } else {
        stringBuilder.append(string).append(", ");
      }
    }
    return stringBuilder.substring(0, stringBuilder.length() - 2);
  }

  public static String getQueryString(String tagName) {
    return "(" + tagName.trim().replaceAll(",", "|").toLowerCase() + ")";
  }
}
