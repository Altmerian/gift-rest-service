package com.epam.esm.util;

public class ParseHelper {
  public static int parsePage(String page) {
    try{
      return Integer.parseUnsignedInt(page);
    } catch (NumberFormatException exception) {
      return 1;
    }
  }

  public static int parseSize(String size) {
    try{
      int pageSize = Integer.parseUnsignedInt(size);
      return Math.min(pageSize, 100);
    } catch (NumberFormatException exception) {
      return 20;
    }
  }
}
