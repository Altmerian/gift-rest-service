package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import com.epam.esm.specification.NameTagSpecification;
import com.epam.esm.specification.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class TagJPARepository implements TagRepository {

  @PersistenceContext private EntityManager entityManager;

  @Override
  public List<Tag> getAll(int page, int size) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tag> cq = cb.createQuery(Tag.class);
    Root<Tag> rootEntry = cq.from(Tag.class);
    cq.orderBy(cb.asc(rootEntry.get("id")));
    CriteriaQuery<Tag> all = cq.select(rootEntry);

    TypedQuery<Tag> allQuery = entityManager.createQuery(all);
    allQuery.setFirstResult((page - 1) * size);
    allQuery.setMaxResults(size);
    return allQuery.getResultList();
  }

  @Override
  public long countAll() {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    cq.select(cb.count(cq.from(Tag.class)));
    return entityManager.createQuery(cq).getSingleResult();
  }

  @Override
  public Optional<Tag> get(long id) {
    Tag tag = entityManager.find(Tag.class, id);
    return Optional.ofNullable(tag);
  }

  @Override
  public long create(Tag tag) {
    entityManager.persist(tag);
    return tag.getId();
  }

  @Override
  public void delete(Tag tag) {
    entityManager.remove(tag);
  }

  @Override
  public List<Tag> query(Specification<Tag> specification) {
    @SuppressWarnings("unchecked")
    TypedQuery<Tag> query = (TypedQuery<Tag>) specification.toJPAQuery(entityManager);
    return query.getResultList();
  }

  @Override
  public boolean contains(Tag tag) {
    return !query(new NameTagSpecification(tag.getName())).isEmpty();
  }
}
