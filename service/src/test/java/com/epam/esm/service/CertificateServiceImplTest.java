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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Certificate service")
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
    // when
    when(certificateRepository.get(anyLong())).thenReturn(Optional.of(mockCertificate));
    when(tagRepository.query(tagSQLSpecification)).thenReturn(List.of(new Tag()));
    // then
    assertEquals(mockCertificateDTO, certificateService.getById(1L));
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
    Set<TagDTO> tagDTOSet = new HashSet<>(List.of(mockTagDTO));
    // when
    when(certificateRepository.query(any(CertificateSQLSpecification.class))).thenReturn(List.of(mockCertificate));
    when(tagRepository.query(any(TagSQLSpecification.class))).thenReturn(List.of(mockTag));
    when(tagRepository.get(anyLong())).thenReturn(Optional.of(mockTag));
    // then
    assertTrue(certificateService.foundDuplicate("name", 30, BigDecimal.valueOf(100.00), tagDTOSet));
  }

  @Test
  public void foundDuplicate_uniqueNamePriceDurationTags_expectedFalse() {
    // given
    Set<TagDTO> tagDTOSet = new HashSet<>(List.of(mockTagDTO));
    // when
    when(certificateRepository.query(any(CertificateSQLSpecification.class))).thenReturn(new ArrayList<>());
    // then
    assertFalse(certificateService.foundDuplicate("name", 30, BigDecimal.valueOf(100.00), tagDTOSet));
  }

  @Test
  void addCertificateTag() {}

  @Test
  void convertToDto_certificate_certificateDTO() {
    // given certificate
    // when
    when(modelMapper.map(mockCertificate, CertificateDTO.class)).thenReturn(mockCertificateDTO);
    // then
    assertEquals(mockCertificateDTO, certificateService.convertToDto(mockCertificate));
  }

  @Test
  void convertToEntity_certificateDTO_certificate() {
    // given certificateDTO
    // when
    when(modelMapper.map(mockCertificateDTO, Certificate.class)).thenReturn(mockCertificate);
    // then
    assertEquals(mockCertificate, certificateService.convertToEntity(mockCertificateDTO));
  }

  @Nested
  @DisplayName("ModelMapper converting methods")
  class ModelMapperTest {
    private Certificate certificate;
    private CertificateDTO certificateDTO;
    private final ModelMapper modelMapper = new ModelMapper();

    @BeforeEach
    void setUp() {
      certificate = new Certificate();
      certificate.setId(1L);
      certificate.setName("Test certificate");
      certificate.setDescription("Text");
      certificate.setCreationDate(LocalDateTime.now());
      certificate.setPrice(BigDecimal.valueOf(100.00));
      Tag tag = new Tag();
      tag.setId(1L);
      tag.setName("TestTag");
      certificate.setTags(new HashSet<>(List.of(tag)));

      certificateDTO = new CertificateDTO();
      certificateDTO.setId(1L);
      certificateDTO.setName("Test certificate");
      certificateDTO.setDescription("Text");
      certificateDTO.setCreationDate(certificate.getCreationDate());
      certificateDTO.setPrice(BigDecimal.valueOf(100.00));
      TagDTO tagDTO = new TagDTO();
      tagDTO.setId(1L);
      tagDTO.setName("TestTag");
      certificateDTO.setTags(new HashSet<>(List.of(tagDTO)));
    }

    @Test
    void map_certificate_certificateDTO() {
      assertEquals(certificateDTO, modelMapper.map(certificate, CertificateDTO.class));
    }

    @Test
    void map_certificateDTO_certificate() {
      assertEquals(certificate, modelMapper.map(certificateDTO, Certificate.class));
    }
  }
}
