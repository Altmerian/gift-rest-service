package com.epam.esm.specification;

import com.epam.esm.util.QueryHelper;

public class SearchAndSortCertificateSQLSpecification
    implements SQLSpecification {
  private final String tagName;
  private final String searchFor;
  private final String sortQuery;

  public SearchAndSortCertificateSQLSpecification(
      String tagName, String searchFor, String sortBy) {
    this.tagName = tagName == null ? "%" : QueryHelper.getQueryString(tagName);
    this.searchFor = searchFor == null ? "%" : QueryHelper.getQueryString(searchFor);
    this.sortQuery = QueryHelper.getSortQuery(sortBy);
  }

  @Override
  public String toSqlQuery() {
    return "SELECT * FROM certificates_function(?, ?) ORDER BY " + sortQuery;
  }

  @Override
  public Object[] getParameters() {


    return new Object[] {tagName, searchFor};
  }
}
