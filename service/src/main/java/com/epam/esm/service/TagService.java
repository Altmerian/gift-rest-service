package com.epam.esm.service;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.ResourceConflictException;
import com.epam.esm.repository.TagRepository;

import java.util.List;

/**
 * Represents an interface of service which interacts with the underlying repository layer for
 * tag-related actions. An instance of tag repository {@link TagRepository} should be aggregated
 * during implementation.
 */
public interface TagService {

  /**
   * Gets data of all tags from the repository layer.
   *
   * @return list of tags in certain transfer format
   * @param page number of page to view
   * @param size number of tags per page
   */
  List<TagDTO> getAll(int page, int size);

  /**
   * Gets data of tags with given id from the repository layer.
   *
   * @return tags with given id in certain transfer format
   */
  TagDTO getById(long id);

  /**
   * Invokes repository method to persist tag data in the system
   *
   * @param tagDTO tag data in transfer format
   * @return id of successfully persisted tag
   */
  long create(TagDTO tagDTO);

  /**
   * Invokes repository method to delete tag data from the system
   *
   * @param id id of the tag to delete
   */
  void delete(long id);

  /**
   * Checks tag data for duplicates in the system. Duplicate is a tag with the same name.
   *
   * @param tagDTO tag data to search
   * @throws ResourceConflictException if tag with given name already exists
   */
  void checkForDuplicate(TagDTO tagDTO) throws ResourceConflictException;

  /**
   * Counts overall quantity of tags in the system
   * @return total amount of tags
   */
  long countAll();
}
