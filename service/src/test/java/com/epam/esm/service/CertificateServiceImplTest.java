package com.epam.esm.service;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.specification.certificate.CertificateSQLSpecification;
import com.epam.esm.specification.tag.CertificateIdTagSQLSpecification;
import com.epam.esm.specification.tag.TagSQLSpecification;
import com.sun.tools.javac.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Certificate service")
// todo refactor given when then
// todo add verify interaction with mock
public class CertificateServiceImplTest {

  @InjectMocks private CertificateServiceImpl certificateService;

  @Mock private CertificateRepository certificateRepository;
  @Mock private TagRepository tagRepository;
  @Mock private ModelMapper modelMapper;
  @Mock private Certificate mockCertificate;
  @Mock private CertificateDTO mockCertificateDTO;
  @Mock private Tag mockTag;
  @Mock private TagDTO mockTagDTO;

  @BeforeEach
  public void setUp() throws Exception {
    when(certificateService.convertToDto(mockCertificate)).thenReturn(mockCertificateDTO);
    when(certificateService.convertToEntity(mockCertificateDTO)).thenReturn(mockCertificate);
    when(certificateService.convertTagToDto(mockTag)).thenReturn(mockTagDTO);
  }

  @Test
  public void getAll() {}

  @Test
  public void sendQuery() {}

  @Test
  public void getById_givenCertificateId_expectedCertificateDTO() {
    // given
    CertificateIdTagSQLSpecification tagSQLSpecification = new CertificateIdTagSQLSpecification(1L);
    when(certificateRepository.get(anyLong())).thenReturn(Optional.of(mockCertificate));
    when(tagRepository.query(tagSQLSpecification)).thenReturn(List.of(new Tag()));
    // when
    CertificateDTO certificateDTO = certificateService.getById(1L);
    // then
    assertThat(certificateDTO, is(equalTo(mockCertificateDTO)));
  }

  @Test
  public void getById_nonexistentCertificateId_thenExceptionThrows() {
    // given
    long certificateId = 666L;
    CertificateIdTagSQLSpecification tagSQLSpecification =
        new CertificateIdTagSQLSpecification(certificateId);
    // when
    when(certificateRepository.get(certificateId)).thenReturn(Optional.empty());
    when(tagRepository.query(tagSQLSpecification)).thenReturn(List.of(new Tag()));
    // then
    assertThrows(ResourceNotFoundException.class, () -> certificateService.getById(certificateId));
  }

  @Test
  public void create() {}

  @Test
  public void update() {}

  @Test
  public void delete() {}

  @Test
  public void foundDuplicate_givenNamePriceDurationTags_expectedTrue() {
    // given
    when(certificateRepository.query(any(CertificateSQLSpecification.class)))
        .thenReturn(List.of(mockCertificate));
    when(mockCertificate.getId()).thenReturn(1L);
    when(mockCertificateDTO.getTags()).thenReturn(Collections.singleton(mockTagDTO));
    when(tagRepository.query(any(TagSQLSpecification.class))).thenReturn(List.of(mockTag));
    when(tagRepository.get(anyLong())).thenReturn(Optional.of(mockTag));
    // when
    boolean actual = certificateService.foundDuplicate(mockCertificateDTO);
    // then
    assertTrue(actual);
  }

  @Test
  public void foundDuplicate_uniqueNamePriceDurationTags_expectedFalse() {
    // given
    when(certificateRepository.query(any(CertificateSQLSpecification.class)))
        .thenReturn(new ArrayList<>());
    // when
    boolean actual = certificateService.foundDuplicate(mockCertificateDTO);
    // then
    assertFalse(actual);
  }

  @Test
  void addCertificateTag() {}

  @Test
  void convertToDto_certificate_certificateDTO() {
    // given
    when(modelMapper.map(mockCertificate, CertificateDTO.class)).thenReturn(mockCertificateDTO);
    // when
    // then
    assertEquals(mockCertificateDTO, certificateService.convertToDto(mockCertificate));
  }

  @Test
  void convertToEntity_certificateDTO_certificate() {
    // given
    when(modelMapper.map(mockCertificateDTO, Certificate.class)).thenReturn(mockCertificate);
    // when
    Certificate certificate = certificateService.convertToEntity(mockCertificateDTO);
    // then
    assertThat(certificate, is(equalTo(mockCertificate)));
  }
}
