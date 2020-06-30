package com.epam.esm.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;

/** Represents selection parameters will be applied to query from the repositories */
public interface Specification<T> {
  /**
   * Constructs SQL-query according given parameters
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
   * Checks if an object satisfies the specification
   *
   * @return {@code true} if specified
   */
  boolean isSatisfiedBy(T t);

  /**
   * Creates new {@code Predicate} from specification parameters and returns it.
   *
   * @return array of specification parameters
   */
  Predicate toPredicate(Root<T> root, CriteriaBuilder cb);

  /**
   * Gets the type of entity that given specification used for
   *
   * @return entity type
   */
  @SuppressWarnings("unchecked")
  default Class<T> getType() {
    ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
    return (Class<T>) type.getActualTypeArguments()[0];
  }
}
