package com.epam.esm.repository;

import com.epam.esm.config.TestDataConfig;
import com.epam.esm.entity.Certificate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SqlGroup({
    @Sql("/test-schema.sql"),
    @Sql("/test-certificates.sql"),
})
@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDataConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CertificateJdbcRepositoryTest {

  @Autowired private JdbcTemplate jdbcTemplate;
  @Autowired private CertificateJdbcRepository repository;

  @Test
  void create() {}

  @Test
  void getAll() {}

  @Test
  void get() {
    Certificate actual = repository.get(1L).get();
    assertEquals(actual.getName(), "Adidas");
  }

  @Test
  void query() {}

  @Test
  void update() {}

  @Test
  void delete() {}
}

