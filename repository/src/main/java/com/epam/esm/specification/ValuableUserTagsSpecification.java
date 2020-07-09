package com.epam.esm.specification;

import com.epam.esm.entity.Tag;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.persistence.EntityManager;
import javax.persistence.Query;

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
    throw new NotImplementedException();
  }

  @Override
  public Query toJPAQuery(EntityManager entityManager) {
    Query nativeQuery = entityManager.createNativeQuery(toSqlQuery(), Tag.class);
    nativeQuery.setParameter(1, userId);
    nativeQuery.setParameter(2, userId);
    return nativeQuery;
  }
}
