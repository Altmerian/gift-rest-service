package com.epam.esm.repository;

import com.epam.esm.entity.User;
import com.epam.esm.specification.EmailUserSpecification;
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
import java.util.Optional;

@Repository
@Transactional
public class UserJPARepository implements UserRepository {

  @PersistenceContext private EntityManager entityManager;

  @Override
  public List<User> getAll(int page, int size) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<User> cq = cb.createQuery(User.class);
    Root<User> rootEntry = cq.from(User.class);
    cq.orderBy(cb.asc(rootEntry.get("id")));
    CriteriaQuery<User> all = cq.select(rootEntry);
    cq.where(cb.notEqual(rootEntry.get("deleted"), true));

    TypedQuery<User> allQuery = entityManager.createQuery(all);
    allQuery.setFirstResult((page - 1) * size);
    allQuery.setMaxResults(size);
    return allQuery.getResultList();
  }

  @Override
  public long countAll() {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    Root<User> user = cq.from(User.class);
    cq.select(cb.count(user));
    cq.where(cb.notEqual(user.get("deleted"), true));
    return entityManager.createQuery(cq).getSingleResult();
  }

  @Override
  public Optional<User> get(long id) {
    User user = entityManager.find(User.class, id);
    return Optional.ofNullable(user);
  }

  @Override
  public long create(User user) {
    entityManager.persist(user);
    entityManager.flush();
    return user.getId();
  }

  @Override
  public void delete(User user) {
    user.setDeleted(true);
    entityManager.merge(user);
  }

  @Override
  public List<User> query(Specification<User> specification) {
    @SuppressWarnings("unchecked")
    TypedQuery<User> query = (TypedQuery<User>) specification.toJPAQuery(entityManager);
    return query.getResultList();
  }

  @Override
  public boolean contains(User user) {
    return !query(new EmailUserSpecification(user.getEmail())).isEmpty();
  }
}
