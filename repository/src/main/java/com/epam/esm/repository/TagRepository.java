package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TagRepository {

  List<Tag> getAll();

  Optional<Tag> getById(long id);

  long create(Tag tag);

  void delete(Tag tag);

  Set<Tag> getByCertificateId(long id);

  Optional<Tag> getByName(String name);
}
