package com.epam.esm.specification;

import com.epam.esm.entity.Order;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class UserIdOrderSpecification implements Specification<Order> {
  private final long userId;

  public UserIdOrderSpecification(long userId) {
    this.userId = userId;
  }

  @Override
  public String toSqlQuery() {
    return "SELECT id, cost, creation_date, user_id, deleted FROM orders WHERE userId = ?";
  }

  @Override
  public Object[] getParameters() {
    return new Object[] {userId};
  }

  @Override
  public TypedQuery<Order> toJPAQuery(EntityManager entityManager) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Order> cq = cb.createQuery(Order.class);
    Root<Order> order = cq.from(Order.class);
    Predicate predicate = cb.equal(order.get("user").get("id"), userId);
    Predicate predicate1 = cb.notEqual(order.get("deleted"), true);
    cq.where(cb.and(predicate, predicate1));
    return entityManager.createQuery(cq);
  }
}
