package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import com.epam.esm.specification.NameTagSpecification;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TagJPARepository extends AbstractRepository<Tag> implements TagRepository {

  @PersistenceContext private EntityManager entityManager;

  @Autowired
  public TagJPARepository(EntityManager entityManager) {
    super(entityManager, Tag.class);
  }

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
  @Transactional
  public void delete(Tag tag) {
    entityManager.remove(tag);
  }

  @Override
  public boolean contains(Tag tag) {
    return !query(new NameTagSpecification(tag.getName())).isEmpty();
  }
}
