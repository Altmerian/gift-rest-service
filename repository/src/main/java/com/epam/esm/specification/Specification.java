package com.epam.esm.specification;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/** Represents selection parameters will be applied to query from the repositories */
public interface Specification<T> {
  /**
   * Constructs SQL-query according to given parameters
   *
   * @return proper SQL-query
   */
  String toSqlQuery();

  /**
   * Creates new {@code Object[]} from specification parameters and returns it.
   *
   * @return array of specification parameters
   */
  Object[] getParameters();

  /**
   * Creates new {@code Query} from specification parameters and returns it.
   *
   * @return {@code Query} according to specification parameters
   */
  Query toJPAQuery(EntityManager entityManager);
}
