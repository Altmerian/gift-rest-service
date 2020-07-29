package com.epam.esm.repository;

import com.epam.esm.config.TestDataConfig;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.TestingDatasourceException;
import com.epam.esm.specification.NamePriceDurationCertificateSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SqlGroup({
  @Sql("/test-schema.sql"),
  @Sql("/test-certificates.sql"),
})
@SpringBootTest(classes = TestDataConfig.class)
class CertificateJdbcRepositoryTest {

  @Autowired private CertificateJdbcRepository repository;
  @Autowired private JdbcTemplate jdbcTemplate;

  @Test
  void create_givenCertificate_shouldBePersistedInDatasource() {
    // given
    Certificate certificate = new Certificate();
    certificate.setName("Test certificate");
    certificate.setDescription("Text");
    certificate.setPrice(BigDecimal.valueOf(100.00));
    certificate.setDurationInDays(90);
    certificate.setCreationDate(ZonedDateTime.now());
    Tag tag = new Tag();
    tag.setName("TestTag");
    tag.setId(3L);
    certificate.setTags(Collections.singleton(tag));
    // when
    long certificateId = repository.create(certificate);
    // then
    assertThat(certificateId, is(equalTo(3L)));
  }

  @Test
  void getAll_queryForAll_expectedAllCertificatesList() {
    // when
    List<Certificate> certificateList = repository.getAll(1, 10);
    // then
    assertThat(certificateList, hasSize(2));
  }

  @Test
  void get_givenCertificateId_expectedCertificate() {
    // given
    long certificateId = 1L;
    // when
    Certificate actual = repository.get(certificateId).orElse(new Certificate());
    // then
    assertAll(
        "Certificate by Id",
        () -> assertEquals("Adidas", actual.getName()),
        () -> assertEquals(new BigDecimal("100.00"), actual.getPrice()),
        () -> assertEquals(90, actual.getDurationInDays()));
  }

  @Test
  void query_givenNamePriceDuration_expectedCertificate() {
    // given
    NamePriceDurationCertificateSpecification namePriceDurationCertificateSpecification =
        new NamePriceDurationCertificateSpecification("Adidas", new BigDecimal("100.00"), 90);
    // when
    List<Certificate> certificates = repository.query(namePriceDurationCertificateSpecification,1 , 10);
    Certificate actual = certificates.get(0);
    // then
    assertAll(
        "Query for certificate",
        () -> assertEquals("Adidas", actual.getName()),
        () -> assertEquals(new BigDecimal("100.00"), actual.getPrice()),
        () -> assertEquals(90, actual.getDurationInDays()));
  }

  @Test
  void update_whenCertificateNameChanged_PersistedNameShouldBeChanged() {
    // given
    Certificate certificateToUpdate =
        repository.get(1L).orElseThrow(() -> new TestingDatasourceException("There is no data in testing database"));
    String newName = "NewName";
    certificateToUpdate.setName(newName);
    // when
    repository.update(certificateToUpdate);
    String actualName = repository.get(1L).orElse(new Certificate()).getName();
    // then
    assertThat(actualName, is(equalTo(newName)));
  }

  @Test
  void delete_whenDelete_shouldBeDeleted() {
    //given
    Certificate certificate = repository.get(1L).orElseThrow(() -> new TestingDatasourceException("There is no data in testing database"));
    //when
    repository.delete(certificate);
    Executable retrievingAttempt =() -> repository.get(1L).orElseThrow(NoSuchElementException::new);
    //then
    assertThrows(NoSuchElementException.class, retrievingAttempt);
  }

  @Test
  void addCertificateTag_givenCertificateIdTagId_ShouldBePersisted() {
    //given
    long certificateId = 1L;
    long tagId = 2L;
    //when
    repository.addCertificateTag(certificateId, tagId);
    int rowsCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "certificates_tags");
    //then
    assertThat(rowsCount, is(equalTo(5)));
  }

  @Test
  void clearCertificateTags_whenDeleteTags_shouldBeDeleted() {
    //given
    long certificateId = 2L;
    //when
    repository.clearCertificateTags(certificateId);
    int rowsCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "certificates_tags");
    //then
    assertThat(rowsCount, is(equalTo(2)));
  }
}
