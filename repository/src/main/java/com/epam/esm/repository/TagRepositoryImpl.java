package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagMapper;
import com.epam.esm.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TagRepositoryImpl implements TagRepository {

  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert simpleJdbcInsert;

  @Autowired
  public TagRepositoryImpl(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.simpleJdbcInsert =
        new SimpleJdbcInsert(dataSource).withTableName("tags").usingGeneratedKeyColumns("id");
  }

  @Override
  public List<Tag> getAll() {
    String SQL_GET_ALL = "SELECT * FROM tags";
    return jdbcTemplate.query(SQL_GET_ALL, new TagMapper());
  }

  @Override
  public Tag getById(long id) {
    String SQL_GET_BY_ID = "SELECT * FROM tags WHERE id = ?";
    Tag tag;
    try {
      tag = jdbcTemplate.queryForObject(SQL_GET_BY_ID, new Object[] {id}, new TagMapper());
    } catch (IncorrectResultSizeDataAccessException e) {
      throw new ResourceNotFoundException("Can't find a tag with id = " + id);
    }
    return tag;
  }

  @Override
  public List<Tag> getByCertificateId(long id) {
    String SQL_GET_BY_CERTIFICATE_ID =
        "SELECT id, name FROM tags "
            + "LEFT JOIN certificates_tags ON id = tag_id WHERE certificate_id = ?";
    return jdbcTemplate.query(SQL_GET_BY_CERTIFICATE_ID, new Object[] {id}, new TagMapper());
  }

  @Override
  public Optional<Tag> getByName(String name) {
    String SQL_GET_BY_NAME = "SELECT id, name FROM tags WHERE name = ?";
    Tag tag;
    try {
      tag = jdbcTemplate.queryForObject(SQL_GET_BY_NAME, new Object[] {name}, new TagMapper());
    } catch (IncorrectResultSizeDataAccessException e) {
      return Optional.empty();
    }
    return Optional.ofNullable(tag);
  }

  @Override
  public long create(Tag tag) {
    Map<String, Object> parameters = new HashMap<>(1);
    parameters.put("name", tag.getName());
    Number tagId = simpleJdbcInsert.executeAndReturnKey(parameters);
    return tagId.longValue();
  }

  @Override
  public boolean delete(long id) {
    String SQL_DELETE_TAG = "delete from tags where id = ?";
    return jdbcTemplate.update(SQL_DELETE_TAG, id) > 0;
  }
}
