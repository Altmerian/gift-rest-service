package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import com.epam.esm.specification.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class OrderJPARepository extends AbstractRepository<Order> implements OrderRepository {

  @PersistenceContext private EntityManager entityManager;

  public OrderJPARepository(EntityManager entityManager) {
    super(entityManager, Order.class);
  }

  @Override
  public long countAll(long userId) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    Root<Order> order = cq.from(Order.class);
    cq.select(cb.count(order));
    cq.where(
        cb.and(
            cb.equal(order.get("user").get("id"), userId),
            cb.notEqual(order.get("deleted"), true)));
    return entityManager.createQuery(cq).getSingleResult();
  }

  @Override
  public List<Order> query(Specification<Order> specification, int page, int size) {
    TypedQuery<Order> query = specification.toJPAQuery(entityManager);
    query.setFirstResult((page - 1) * size);
    query.setMaxResults(size);
    return query.getResultList();
  }

  @Override
  @Transactional
  public void delete(Order order) {
    order.setDeleted(true);
    entityManager.merge(order);
  }
}
