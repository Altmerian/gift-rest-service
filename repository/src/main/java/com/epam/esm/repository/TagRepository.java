package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import com.epam.esm.specification.tag.TagSpecification;

import java.util.List;
import java.util.Optional;

public interface TagRepository {

  List<Tag> getAll();

  Optional<Tag> get(long id);

  long create(Tag tag);

  void delete(Tag tag);

  List<Tag> query(TagSpecification specification);

  boolean contains(Tag tag);
}
