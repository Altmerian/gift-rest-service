package com.epam.esm.service;

import com.epam.esm.entity.Tag;

import java.util.List;

public interface TagService {

	public List<Tag> getAll();

	public Tag getById(long id);

	public long create(Tag tag);

	public boolean delete(long id);

	public boolean foundDuplicate(String name);
}
