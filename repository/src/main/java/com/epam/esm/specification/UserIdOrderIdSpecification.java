package com.epam.esm.specification;

import com.epam.esm.entity.Order;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class UserIdOrderIdSpecification implements Specification<Order> {
  private final long userId;
  private final long orderId;

  public UserIdOrderIdSpecification(long userId, long orderId) {
    this.userId = userId;
    this.orderId = orderId;
  }

  @Override
  public String toSqlQuery() {
    return "SELECT id, cost, creation_date, user_id, deleted FROM orders WHERE userId = ? AND orderId = ?";
  }

  @Override
  public Object[] getParameters() {
    return new Object[]{userId, orderId};
  }

  @Override
  public Query toJPAQuery(EntityManager entityManager) {
    throw new UnsupportedOperationException();
  }

  @Override
  public TypedQuery<Order> typedJPAQuery(EntityManager entityManager) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Order> cq = cb.createQuery(Order.class);
    Root<Order> order = cq.from(Order.class);
    Predicate predicate1 = cb.equal(order.get("user").get("id"), userId);
    Predicate predicate2 = cb.equal(order.get("id"), orderId);
    cq.where(cb.and(predicate1, predicate2));
    return entityManager.createQuery(cq);
  }
}
