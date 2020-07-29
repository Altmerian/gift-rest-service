package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import com.epam.esm.specification.NameTagSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class TagJPARepository extends AbstractRepository<Tag> implements TagRepository {

  @PersistenceContext private EntityManager entityManager;

  @Autowired
  public TagJPARepository(EntityManager entityManager) {
    super(entityManager, Tag.class);
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
