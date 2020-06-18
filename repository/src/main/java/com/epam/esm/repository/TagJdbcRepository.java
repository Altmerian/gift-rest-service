package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagMapper;
import com.epam.esm.specification.tag.TagSQLSpecification;
import com.epam.esm.specification.tag.TagSpecification;
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
    String sqlGetAll = "SELECT * FROM tags";
    return jdbcTemplate.query(sqlGetAll, new TagMapper());
  }

  @Override
  public Optional<Tag> get(long id) {
    String sqlGetById = "SELECT * FROM tags WHERE id = ?";
    Tag tag;
    try {
      tag = jdbcTemplate.queryForObject(sqlGetById, new Object[] {id}, new TagMapper());
    } catch (IncorrectResultSizeDataAccessException e) {
      return Optional.empty();
    }
    return Optional.ofNullable(tag);
  }

  @Override
  public List<Tag> query(TagSpecification specification) {
    TagSQLSpecification sqlSpecification = (TagSQLSpecification) specification;
    return jdbcTemplate.query(
        sqlSpecification.toSqlQuery(),
        sqlSpecification.getParameters(),
        new TagMapper());
  }

  @Override
  public boolean contains(Tag tag) {
    String sqlGetByName = "SELECT id, name FROM tags WHERE name = ?";
    try {
      jdbcTemplate.queryForObject(sqlGetByName, new Object[] {tag.getName()}, new TagMapper());
    } catch (IncorrectResultSizeDataAccessException e) {
      return false;
    }
    return true;
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
    String sqlDeleteTag = "delete from tags where id = ?";
    jdbcTemplate.update(sqlDeleteTag, tag.getId());
  }
}
