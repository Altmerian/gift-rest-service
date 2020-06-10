package com.epam.esm.service;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

  private final TagRepository tagRepository;

  @Autowired
  public TagServiceImpl(TagRepository tagRepository) {
    this.tagRepository = tagRepository;
  }

  @Override
  public List<Tag> getAll() {
    return tagRepository.getAll();
  }

  @Override
  public Tag getById(long id) {
    return tagRepository.getById(id);
  }

  @Override
  public long create(Tag tag) {
    return tagRepository.create(tag);
  }

  @Override
  public boolean delete(long id) {
    return tagRepository.delete(id);
  }

  @Override
  public boolean foundDuplicate(String name) {
    return tagRepository.getByName(name).isPresent();
  }
}
