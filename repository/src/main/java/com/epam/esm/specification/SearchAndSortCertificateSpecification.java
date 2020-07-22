package com.epam.esm.specification;

import com.epam.esm.entity.Certificate;
import com.epam.esm.util.QueryHelper;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class SearchAndSortCertificateSpecification implements Specification<Certificate> {
  private final String tagName;
  private final String searchFor;
  private final String sortQuery;
  private final int tagsCount;

  public SearchAndSortCertificateSpecification(String tagName, String searchFor, String sortBy) {
    this.tagName = StringUtils.isBlank(tagName) ? "%" : QueryHelper.getQueryString(tagName);
    this.searchFor = StringUtils.isBlank(searchFor) ? "%" : QueryHelper.getQueryString(searchFor);
    this.sortQuery = QueryHelper.getSortQuery(sortBy);
    this.tagsCount = StringUtils.isBlank(tagName) ? 1 : QueryHelper.getTagsCount(tagName);
  }

  @Override
  public String toSqlQuery() {
    return "SELECT id, name, description, price, creation_date, modification_date, duration_in_days, deleted FROM certificates_function(?, ?, ?) "
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
    nativeQuery.setParameter(3,tagsCount);
    return nativeQuery;
  }

  @Override
  public TypedQuery<Certificate> typedJPAQuery(EntityManager entityManager) {
    throw new UnsupportedOperationException();
  }
}
