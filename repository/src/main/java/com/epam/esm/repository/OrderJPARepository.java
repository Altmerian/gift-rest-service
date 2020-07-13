package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import com.epam.esm.specification.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class OrderJPARepository implements OrderRepository {

  @PersistenceContext private EntityManager entityManager;

  @Override
  public List<Order> getAll(int page, int size) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Order> cq = cb.createQuery(Order.class);
    Root<Order> rootEntry = cq.from(Order.class);
    cq.orderBy(cb.asc(rootEntry.get("id")));
    CriteriaQuery<Order> all = cq.select(rootEntry);
    cq.where(cb.notEqual(rootEntry.get("deleted"), true));

    TypedQuery<Order> allQuery = entityManager.createQuery(all);
    allQuery.setFirstResult((page - 1) * size);
    allQuery.setMaxResults(size);
    return allQuery.getResultList();
  }

  @Override
  public long countAll() {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    Root<Order> order = cq.from(Order.class);
    cq.select(cb.count(order));
    cq.where(cb.notEqual(order.get("deleted"), true));
    return entityManager.createQuery(cq).getSingleResult();
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
  public Optional<Order> get(long id) {
    Order order = entityManager.find(Order.class, id);
    return Optional.ofNullable(order);
  }

  @Override
  public long create(Order order) {
    entityManager.persist(order);
    entityManager.flush();
    return order.getId();
  }

  @Override
  public void delete(Order order) {
    order.setDeleted(true);
    entityManager.merge(order);
  }

  @Override
  public List<Order> query(Specification<Order> specification, int page, int size) {
    Query query = specification.toJPAQuery(entityManager);
    query.setFirstResult((page - 1) * size);
    query.setMaxResults(size);
    @SuppressWarnings("unchecked")
    List<Order> resultList = (List<Order>) query.getResultList();
    return resultList;
  }

  @Override
  public List<Order> query(Specification<Order> specification) {
    @SuppressWarnings("unchecked")
    TypedQuery<Order> query = (TypedQuery<Order>) specification.toJPAQuery(entityManager);
    return query.getResultList();
  }
}
