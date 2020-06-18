package com.epam.esm.specification.certificate;

import com.epam.esm.util.QueryHelper;

public class SearchAndSortCertificateSQLSpecification
    implements CertificateSQLSpecification {
  private final String tagName;
  private final String searchFor;
  private final String sortBy;
  private String sortQuery;

  public SearchAndSortCertificateSQLSpecification(
      String tagName, String price, String sortBy) {
    this.tagName = tagName;
    this.searchFor = price;
    this.sortBy = sortBy;
  }

  @Override
  public String toSqlQuery() {
    String tagQuery = tagName == null ? "%" : QueryHelper.getQueryString(tagName);
    String searchQuery = searchFor == null ? "%" : QueryHelper.getQueryString(searchFor);
    sortQuery = QueryHelper.getSortQuery(sortBy);
    return String.format("SELECT * FROM certificates_function('%s', '%s') ORDER BY ?",
        tagQuery, searchQuery);
  }

  @Override
  public Object[] getParameters() {
    return new Object[] {sortQuery};
  }
}