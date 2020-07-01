package com.epam.esm.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/** Represents selection parameters will be applied to query from the repositories */
public interface Specification<T> {
  /**
   * Constructs SQL-query according to given parameters
   *
   * @return proper SQL-query
   */
  String toSqlQuery();

  /**
   * Constructs JPQL-query according to given parameters
   *
   * @return proper JPQL-query
   */
  String toJPQLQuery();

  /**
   * Creates new {@code Object[]} from specification parameters and returns it.
   *
   * @return array of specification parameters
   */
  Object[] getParameters();

  /**
   * Creates new {@code Predicate} from specification parameters and returns it.
   *
   * @return {@code Predicate} according to specification parameters
   */
  Predicate toPredicate(Root<T> root, CriteriaBuilder cb);
}
