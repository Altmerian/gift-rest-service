package com.epam.esm.specification;

import com.epam.esm.entity.Certificate;
import com.epam.esm.util.QueryHelper;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class SearchAndSortCertificateSpecification
    implements Specification<Certificate> {
  private final String tagName;
  private final String searchFor;
  private final String sortQuery;

  public SearchAndSortCertificateSpecification(
      String tagName, String searchFor, String sortBy) {
    this.tagName = tagName == null ? "%" : QueryHelper.getQueryString(tagName);
    this.searchFor = searchFor == null ? "%" : QueryHelper.getQueryString(searchFor);
    this.sortQuery = QueryHelper.getSortQuery(sortBy);
  }

  @Override
  public String toSqlQuery() {
    return "SELECT id, name, description, price, creation_date, modification_date, duration_in_days FROM certificates_function(?, ?) " + sortQuery;
  }

  @Override
  public String toJPQLQuery() {
    return "SELECT id, name, description, price, creation_date, modification_date, duration_in_days FROM FUNCTION('certificates_function', :tag, :search) " + sortQuery;
  }

  @Override
  public Object[] getParameters() {
    return new Object[] {tagName, searchFor};
  }

  @Override
  public Predicate toPredicate(Root<Certificate> root, CriteriaBuilder cb) {
    throw new UnsupportedOperationException();
  }
}
