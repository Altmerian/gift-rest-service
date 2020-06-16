package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class TagJdbcRepository implements TagRepository {

  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert simpleJdbcInsert;

  @Autowired
  public TagJdbcRepository(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.simpleJdbcInsert =
        new SimpleJdbcInsert(dataSource).withTableName("tags").usingGeneratedKeyColumns("id");
  }

  @Override
  public List<Tag> getAll() {
    final String SQL_GET_ALL = "SELECT * FROM tags";
    return jdbcTemplate.query(SQL_GET_ALL, new TagMapper());
  }

  @Override
  public Optional<Tag> getById(long id) {
    final String SQL_GET_BY_ID = "SELECT * FROM tags WHERE id = ?";
    Tag tag;
    try {
      tag = jdbcTemplate.queryForObject(SQL_GET_BY_ID, new Object[] {id}, new TagMapper());
    } catch (IncorrectResultSizeDataAccessException e) {
      return Optional.empty();
    }
    return Optional.ofNullable(tag);
  }

  @Override
  public Set<Tag> getByCertificateId(long id) {
    final String SQL_GET_BY_CERTIFICATE_ID =
        "SELECT id, name FROM tags "
            + "LEFT JOIN certificates_tags ON id = tag_id WHERE certificate_id = ?";
    List<Tag> tags =
        jdbcTemplate.query(SQL_GET_BY_CERTIFICATE_ID, new Object[] {id}, new TagMapper());
    return new HashSet<Tag>(tags);
  }

  @Override
  public Optional<Tag> getByName(String name) {
    final String SQL_GET_BY_NAME = "SELECT id, name FROM tags WHERE name = ?";
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
  public void delete(Tag tag) {
    final String SQL_DELETE_TAG = "delete from tags where id = ?";
    jdbcTemplate.update(SQL_DELETE_TAG, tag.getId());
  }
}
