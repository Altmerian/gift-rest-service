package com.epam.esm.specification;

import com.epam.esm.entity.Tag;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class NameTagSpecification implements Specification<Tag> {
  private final String name;

  public NameTagSpecification(String name) {
    this.name = name;
  }

  @Override
  public String toSqlQuery() {
    return "SELECT id, name FROM tags WHERE name = ?";
  }

  @Override
  public Object[] getParameters() {
    return new Object[] {name};
  }

  @Override
  public boolean isSatisfiedBy(Tag tag) {
    return tag.getName().equals(name);
  }

  @Override
  public Predicate toPredicate(Root<Tag> tag, CriteriaBuilder cb) {
    return cb.equal(tag.get("name"), name);
  }
}
