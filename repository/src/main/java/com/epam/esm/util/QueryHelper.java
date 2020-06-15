package com.epam.esm.util;

public class QueryHelper {
  public static String getSortQuery(String queryString) {
    StringBuilder stringBuilder = new StringBuilder();
    String[] strings = queryString.trim().split("[;,.\\s|]+");
    for (String string : strings) {
      if (string.charAt(0) == '-') {
        stringBuilder.append(string.substring(1)).append(" DESC, ");
      } else {
        stringBuilder.append(string).append(", ");
      }
    }
    return stringBuilder.substring(0, stringBuilder.length() - 2);
  }
}
