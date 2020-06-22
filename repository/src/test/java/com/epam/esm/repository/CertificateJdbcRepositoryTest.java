package com.epam.esm.repository;

import com.epam.esm.config.TestDataConfig;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.TestingDatasourceException;
import com.epam.esm.specification.certificate.NamePriceDurationCertificateSQLSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SqlGroup({
  @Sql("/test-schema.sql"),
  @Sql("/test-certificates.sql"),
})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDataConfig.class)
class CertificateJdbcRepositoryTest {

  @Autowired private CertificateJdbcRepository repository;

  @Test
  void create_givenCertificate_shouldBePersistedInDatasource() {
    // given
    Certificate certificate = new Certificate();
    certificate.setName("Test certificate");
    certificate.setDescription("Text");
    certificate.setPrice(BigDecimal.valueOf(100.00));
    certificate.setDurationInDays(90);
    certificate.setCreationDate(LocalDateTime.now());
    Tag tag = new Tag();
    tag.setName("TestTag");
    certificate.setTags(Collections.singleton(tag));
    // when
    long certificateId = repository.create(certificate);
    // then
    assertThat(certificateId, is(equalTo(3L)));
  }

  @Test
  void getAll_queryForAll_expectedAllCertificatesList() {
    // when
    List<Certificate> certificateList = repository.getAll();
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
    NamePriceDurationCertificateSQLSpecification namePriceDurationCertificateSQLSpecification =
        new NamePriceDurationCertificateSQLSpecification("Adidas", new BigDecimal("100.00"), 90);
    // when
    List<Certificate> certificates = repository.query(namePriceDurationCertificateSQLSpecification);
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
    boolean result = repository.update(certificateToUpdate);
    String actualName = repository.get(1L).orElse(new Certificate()).getName();
    // then
    assertTrue(result);
    assertThat(actualName, is(equalTo(newName)));
  }

  @Test
  void delete_whenDelete_shouldBeDeleted() {
    //given
    Certificate certificate = repository.get(1L).orElseThrow(() -> new TestingDatasourceException("There is no data in testing database"));
    //when
    boolean result = repository.delete(certificate);
    Executable retrievingAttempt =() -> repository.get(1L).orElseThrow(NoSuchElementException::new);
    //then
    assertTrue(result);
    assertThrows(NoSuchElementException.class, retrievingAttempt);
  }

  @Test
  void addCertificateTag_givenCertificateIdTagId_ShouldBePersisted() {
    //given
    long certificateId = 1L;
    long tagId = 2L;
    //when
    boolean actualResult = repository.addCertificateTag(certificateId, tagId);
    //then
    assertTrue(actualResult);
  }

  @Test
  void clearCertificateTags_whenDeleteTags_shouldBeDeleted() {
    //given
    long certificateId = 2L;
    //when
    boolean actualResult = repository.clearCertificateTags(certificateId);
    //then
    assertTrue(actualResult);
  }
}
