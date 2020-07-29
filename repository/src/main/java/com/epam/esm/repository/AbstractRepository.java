package com.epam.esm.repository;

import com.epam.esm.entity.BaseEntity;
import com.epam.esm.entity.Tag;
import com.epam.esm.specification.Specification;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public abstract class AbstractRepository<E extends BaseEntity> implements BaseRepository<E> {

  private final EntityManager entityManager;
  private final Class<E> aClass;

  public AbstractRepository(EntityManager entityManager, Class<E> aClass) {
    this.entityManager = entityManager;
    this.aClass = aClass;
  }

  @Override
  public List<E> getAll(int page, int size) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<E> cq = cb.createQuery(aClass);
    Root<E> rootEntry = cq.from(aClass);
    cq.orderBy(cb.asc(rootEntry.get("id")));
    CriteriaQuery<E> all = cq.select(rootEntry);
    if (aClass != Tag.class) {
      cq.where(cb.notEqual(rootEntry.get("deleted"), true));
    }

    TypedQuery<E> allQuery = entityManager.createQuery(all);
    allQuery.setFirstResult((page - 1) * size);
    allQuery.setMaxResults(size);
    return allQuery.getResultList();
  }

  @Override
  public long countAll() {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    Root<E> rootEntry = cq.from(aClass);
    cq.select(cb.count(rootEntry));
    if (aClass != Tag.class) {
      cq.where(cb.notEqual(rootEntry.get("deleted"), true));
    }
    return entityManager.createQuery(cq).getSingleResult();
  }

  @Override
  @Transactional
  public long create(E entity) {
    entityManager.persist(entity);
    entityManager.flush();
    return entity.getId();
  }

  @Override
  public Optional<E> get(long id) {
    E entity = entityManager.find(aClass, id);
    return Optional.ofNullable(entity);
  }

  @Override
  @Transactional
  public void update(E entity) {
    entityManager.merge(entity);
  }

  @Override
  public List<E> query(Specification<E> specification) {
    TypedQuery<E> query = specification.toJPAQuery(entityManager);
    return query.getResultList();
  }
}
