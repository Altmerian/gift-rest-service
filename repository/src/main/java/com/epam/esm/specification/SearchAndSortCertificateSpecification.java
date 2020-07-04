package com.epam.esm.specification;

import com.epam.esm.entity.Certificate;
import com.epam.esm.util.QueryHelper;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class SearchAndSortCertificateSpecification implements Specification<Certificate> {
  private final String tagName;
  private final String searchFor;
  private final String sortQuery;
  private final int page;
  private final int size;

  public SearchAndSortCertificateSpecification(String tagName, String searchFor, String sortBy, int page, int size) {
    this.tagName = tagName == null ? "%" : QueryHelper.getQueryString(tagName);
    this.searchFor = searchFor == null ? "%" : QueryHelper.getQueryString(searchFor);
    this.sortQuery = QueryHelper.getSortQuery(sortBy);
    this.page = page;
    this.size = size;
  }

  @Override
  public String toSqlQuery() {
    return "SELECT id, name, description, price, creation_date, modification_date, duration_in_days FROM certificates_function(?, ?) "
        + sortQuery;
  }

  @Override
  public Object[] getParameters() {
    return new Object[] {tagName, searchFor};
  }

  @Override
  public Query toJPAQuery(EntityManager entityManager) {
    Query nativeQuery = entityManager.createNativeQuery(toSqlQuery(), Certificate.class);
    nativeQuery.setParameter(1, tagName);
    nativeQuery.setParameter(2, searchFor);
    nativeQuery.setFirstResult((page - 1) * size);
    nativeQuery.setMaxResults(size);
    return nativeQuery;
  }
}
