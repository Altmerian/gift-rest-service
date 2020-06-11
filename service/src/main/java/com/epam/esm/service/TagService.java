package com.epam.esm.service;

import com.epam.esm.dto.TagDTO;

import java.util.List;

public interface TagService {

  public List<TagDTO> getAll();

  public TagDTO getById(long id);

  public long create(TagDTO tag);

  public boolean delete(long id);

  public boolean foundDuplicate(String name);
}
