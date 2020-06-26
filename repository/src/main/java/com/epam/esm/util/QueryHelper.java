package com.epam.esm.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class QueryHelper {
  private static final List<String> ALLOWED_SORTABLE_COLUMNS =
      Arrays.asList(
          "name", "description", "price", "duration_in_days", "creation_date", "modification_date");

  public static String getSortQuery(String queryString) {
    StringBuilder stringBuilder = new StringBuilder("ORDER BY ");
    String[] strings = queryString.trim().split("[;,.|]+");
    for (String string : strings) {
      String field = string.trim().toLowerCase();
      boolean desc = field.charAt(0) == '-';
      field = desc ? field.substring(1) : field;
      if (ALLOWED_SORTABLE_COLUMNS.contains(field.trim())) {
        stringBuilder.append(field.trim()).append(desc ? " DESC, " : ", ");
      }
    }
    return stringBuilder.toString().equals("ORDER BY ")
        ? StringUtils.EMPTY
        : stringBuilder.substring(0, stringBuilder.length() - 2);
  }

  public static String getQueryString(String tagName) {
    return "(" + tagName.trim().replaceAll(",", "|").toLowerCase() + ")";
  }
}
