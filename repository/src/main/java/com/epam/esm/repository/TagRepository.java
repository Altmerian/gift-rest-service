package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {

  List<Tag> getAll();

  Tag getById(long id);

  long create(Tag tag);

  boolean delete(long id);

  List<Tag> getByCertificateId(long id);

  Optional<Tag> getByName(String name);
}
