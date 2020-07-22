package com.epam.esm.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PageParseHelper {

  @Value("${spring.data.rest.default-page-size}")
  private int defaultPageSize;

  @Value("${spring.data.rest.max-page-size}")
  private int maxPageSize;

  private static final int DEFAULT_START_PAGE_NUMBER = 1;


  public int parsePage(String page) {
    try {
      int pageNumber = Integer.parseUnsignedInt(page);
      return pageNumber != 0 ? pageNumber : DEFAULT_START_PAGE_NUMBER;
    } catch (NumberFormatException exception) {
      return DEFAULT_START_PAGE_NUMBER;
    }
  }

  public int parseSize(String size) {
    try {
      int pageSize = Integer.parseUnsignedInt(size);
      return Math.min(pageSize, maxPageSize);
    } catch (NumberFormatException exception) {
      return defaultPageSize;
    }
  }
}
