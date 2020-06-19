package com.epam.esm.repository;

import com.epam.esm.config.TestDataConfig;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SqlGroup({
  @Sql("/test-schema.sql"),
  @Sql("/test-certificates.sql"),
})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDataConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CertificateJdbcRepositoryTest {

  @Autowired private JdbcTemplate jdbcTemplate;
  @Autowired private CertificateJdbcRepository repository;

  @Test
  void create_givenCertificate_persistedInDatasource() {
    //given
    Certificate certificate = new Certificate();
    certificate.setName("Test certificate");
    certificate.setDescription("Text");
    certificate.setPrice(BigDecimal.valueOf(100.00));
    certificate.setDurationInDays(90);
    certificate.setCreationDate(LocalDateTime.now());
    Tag tag = new Tag();
    tag.setName("TestTag");
    certificate.setTags(Collections.singleton(tag));
    //when
    long certificateId = repository.create(certificate);
    //then
    assertThat(certificateId, is(equalTo(3L)));
  }

  @Test
  void getAll_queryForAll_expectedAllCertificatesList() {
    //when
    List<Certificate> certificateList = repository.getAll();
    //then
    assertThat(certificateList, hasSize(2));
  }

  @Test
  void get_givenCertificateId_expectedCertificate() {
    //given
    long certificateId = 1L;
    //when
    Certificate actual = repository.get(certificateId).orElse(new Certificate());
    //then
      assertAll(
        "Certificate",
        () -> assertEquals("Adidas", actual.getName()),
        () -> assertEquals(new BigDecimal("100.00"), actual.getPrice()),
        () -> assertEquals(90, actual.getDurationInDays()));
  }

  @Test
  void query() {}

  @Test
  void update() {}

  @Test
  void delete() {}
}
