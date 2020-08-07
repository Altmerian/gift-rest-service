package com.epam.esm.specification;

import com.epam.esm.entity.Tag;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/** Specifies the widest used tag of a user with the highest cost of all orders */
public class ValuableUserTagsSpecification implements Specification<Tag> {
  private final long userId;

  public ValuableUserTagsSpecification(long userId) {
    this.userId = userId;
  }

  @Override
  public String toSqlQuery() {
    return "SELECT id, name FROM user_tags_function(?) WHERE tag_cost = (SELECT MAX(tag_cost) FROM user_tags_function(?))";
  }

  @Override
  public Object[] getParameters() {
    throw new UnsupportedOperationException();
  }

  @Override
  public TypedQuery<Tag> toJPAQuery(EntityManager entityManager) {
    TypedQuery<Tag> namedQuery = entityManager.createNamedQuery("getWidelyUsedTagsOfUser", Tag.class);
    namedQuery.setParameter(1, userId);
    namedQuery.setParameter(2, userId);
    return namedQuery;
  }
}
