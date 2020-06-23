package com.epam.esm.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class QueryHelper {
  private static final List<String> ALLOWED_SORTABLE_COLUMNS =
      Arrays.asList(
          "name", "description", "price", "duration_in_days", "creation_date", "modification_date");

  public static String getSortQuery(String queryString) {
    StringBuilder stringBuilder = new StringBuilder();
    String[] strings = queryString.trim().split("[;,.|]+");
    for (String string : strings) {
      string = string.trim().toLowerCase();
      if (string.charAt(0) == '-') {
        if (ALLOWED_SORTABLE_COLUMNS.contains(string.substring(1).trim())) {
          stringBuilder.append(string.substring(1)).append(" DESC, ");
        }
      } else {
        if (ALLOWED_SORTABLE_COLUMNS.contains(string.trim())) {
          stringBuilder.append(string).append(", ");
        }
      }
    }
    return stringBuilder.length() == 0
        ? StringUtils.EMPTY
        : stringBuilder.substring(0, stringBuilder.length() - 2);
  }

  public static String getQueryString(String tagName) {
    return "(" + tagName.trim().replaceAll(",", "|").toLowerCase() + ")";
  }
}
