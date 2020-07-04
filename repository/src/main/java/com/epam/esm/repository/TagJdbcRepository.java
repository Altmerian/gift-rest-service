package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagMapper;
import com.epam.esm.specification.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Transactional
public class TagJdbcRepository implements TagRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public TagJdbcRepository(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public List<Tag> getAll(int page, int size) {
    String sqlGetAll = "SELECT id, name FROM tags";
    return jdbcTemplate.query(sqlGetAll, new TagMapper());
  }

  @Override
  public Optional<Tag> get(long id) {
    String sqlGetById = "SELECT id, name FROM tags WHERE id = ?";
    Tag tag;
    try {
      tag = jdbcTemplate.queryForObject(sqlGetById, new Object[] {id}, new TagMapper());
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
    return Optional.ofNullable(tag);
  }

  @Override
  public List<Tag> query(Specification<Tag> specification) {
    return jdbcTemplate.query(
        specification.toSqlQuery(), specification.getParameters(), new TagMapper());
  }

  @Override
  public boolean contains(Tag tag) {
    String sqlGetByName = "SELECT id, name FROM tags WHERE name = ?";
    try {
      return jdbcTemplate.queryForObject(
              sqlGetByName, new Object[] {tag.getName()}, new TagMapper())
          != null;
    } catch (EmptyResultDataAccessException e) {
      return false;
    }
  }

  @Override
  public long create(Tag tag) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    String sqlInsert = "INSERT INTO tags (name) VALUES (?)";
    jdbcTemplate.update(
        con -> {
          PreparedStatement ps = con.prepareStatement(sqlInsert, new String[] {"id"});
          ps.setString(1, tag.getName());
          return ps;
        },
        keyHolder);
    return Objects.requireNonNull(keyHolder.getKey()).longValue();
  }

  @Override
  public void delete(Tag tag) {
    String sqlDeleteTag = "delete from tags where id = ?";
    jdbcTemplate.update(sqlDeleteTag, tag.getId());
  }
}
