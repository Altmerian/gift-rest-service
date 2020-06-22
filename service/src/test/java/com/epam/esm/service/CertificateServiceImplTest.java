package com.epam.esm.service;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.specification.certificate.CertificateSQLSpecification;
import com.epam.esm.specification.certificate.CertificateSpecification;
import com.epam.esm.specification.tag.TagSQLSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
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
  public void setUp() {
    when(certificateService.convertToDto(mockCertificate)).thenReturn(mockCertificateDTO);
    when(certificateService.convertToEntity(mockCertificateDTO)).thenReturn(mockCertificate);
    when(certificateService.convertTagToDto(mockTag)).thenReturn(mockTagDTO);
  }

  @Test
  public void getAll_queryForAll_expectedListOfAllCertificates() {
    // given
    when(certificateRepository.getAll()).thenReturn(Collections.singletonList(mockCertificate));
    // when
    List<CertificateDTO> certificateDTOList = certificateService.getAll();
    // then
    verify(certificateRepository).getAll();
    assertThat(certificateDTOList, hasSize(1));
  }

  @Test
  public void sendQuery_whenQueryWithParameters_expectedCertificateDTOList() {
    // given
    when(certificateRepository.query(any(CertificateSpecification.class)))
        .thenReturn(Collections.singletonList(mockCertificate));
    // when
    List<CertificateDTO> certificateDTOList =
        certificateService.sendQuery("tagName", "searchFor", "sortBy");
    // then
    verify(certificateRepository).query(any(CertificateSpecification.class));
    assertThat(certificateDTOList, hasSize(1));
  }

  @Test
  public void getById_givenCertificateId_expectedCertificateDTO() {
    // given
    when(certificateRepository.get(anyLong())).thenReturn(Optional.of(mockCertificate));
    // when
    CertificateDTO certificateDTO = certificateService.getById(1L);
    // then
    verify(certificateRepository).get(anyLong());
    assertThat(certificateDTO, is(equalTo(mockCertificateDTO)));
  }

  @Test
  public void getById_nonexistentCertificateId_thenExceptionThrows() {
    // given
    long nonexistentCertificateId = 666L;
    // when
    when(certificateRepository.get(nonexistentCertificateId)).thenReturn(Optional.empty());
    Executable retrievingAttempt = () -> certificateService.getById(nonexistentCertificateId);
    // then
    assertThrows(ResourceNotFoundException.class, retrievingAttempt);
  }

  @Test
  public void create_givenCertificateDTO_expectedPersistedCertificateId() {
    // given
    when(certificateRepository.create(any(Certificate.class))).thenReturn(1L);
    // when
    long certificateId = certificateService.create(mockCertificateDTO);
    // then
    verify(certificateRepository).create(any(Certificate.class));
    assertThat(certificateId, is(equalTo(1L)));
  }

  @Test
  public void update_givenCertificateDTO_shouldInvokeRepositoryUpdateMethods() {
    // given
    CertificateDTO certificateDTO = new CertificateDTO();
    Certificate certificate = new Certificate();
    Tag[] tags = {new Tag("Tag1"), new Tag("Tag2")};
    certificate.setTags(new HashSet<>(Arrays.asList(tags)));
    when(certificateRepository.get(anyLong())).thenReturn(Optional.of(mockCertificate));
    when(certificateRepository.update(any(Certificate.class))).thenReturn(true);
    when(certificateRepository.clearCertificateTags(anyLong())).thenReturn(true);
    when(certificateService.convertToEntity(certificateDTO)).thenReturn(certificate);
    // when
    boolean actualResult = certificateService.update(1L, certificateDTO);
    // then
    verify(certificateRepository).get(1L);
    verify(certificateRepository).update(certificate);
    verify(certificateRepository).clearCertificateTags(1L);
    verify(certificateRepository, Mockito.times(2)).addCertificateTag(1L, 0);
    assertTrue(actualResult);
  }

  @Test
  public void delete_givenCertificateDTOId_shouldInvokeRepositoryDeleteMethod() {
    // given
    long certificateDTOId = 1L;
    when(certificateRepository.get(certificateDTOId)).thenReturn(Optional.of(mockCertificate));
    when(certificateRepository.delete(any(Certificate.class))).thenReturn(true);
    // when
    boolean actualResult = certificateService.delete(certificateDTOId);
    // then
    verify(certificateRepository).get(certificateDTOId);
    verify(certificateRepository).delete(mockCertificate);
    assertTrue(actualResult);
  }

  @Test
  public void foundDuplicate_givenNamePriceDurationTags_expectedTrue() {
    // given
    when(certificateRepository.query(any(CertificateSQLSpecification.class)))
        .thenReturn(Collections.singletonList(mockCertificate));
    when(mockCertificate.getId()).thenReturn(1L);
    when(mockCertificateDTO.getTags()).thenReturn(Collections.singleton(mockTagDTO));
    when(tagRepository.query(any(TagSQLSpecification.class)))
        .thenReturn(Collections.singletonList(mockTag));
    when(tagRepository.get(anyLong())).thenReturn(Optional.of(mockTag));
    // when
    boolean actualResult = certificateService.foundDuplicate(mockCertificateDTO);
    // then
    assertTrue(actualResult);
  }

  @Test
  public void foundDuplicate_uniqueNamePriceDurationTags_expectedFalse() {
    // given
    when(certificateRepository.query(any(CertificateSQLSpecification.class)))
        .thenReturn(new ArrayList<>());
    // when
    boolean actualResult = certificateService.foundDuplicate(mockCertificateDTO);
    // then
    assertFalse(actualResult);
  }

  @Test
  void addCertificateTag_tagWithUniqueName_registeredCertificateTag() {
    // given
    when(tagRepository.contains(mockTag)).thenReturn(false);
    when(certificateRepository.addCertificateTag(anyLong(), anyLong())).thenReturn(true);
    // when
    boolean isAddedCertificateTag = certificateService.addCertificateTag(1L, mockTag);
    // then
    verify(tagRepository).contains(mockTag);
    verify(certificateRepository).addCertificateTag(anyLong(), anyLong());
    assertTrue(isAddedCertificateTag);
  }

  @Test
  void convertToDto_certificate_certificateDTO() {
    // given
    when(modelMapper.map(mockCertificate, CertificateDTO.class)).thenReturn(mockCertificateDTO);
    // when
    CertificateDTO certificateDTO = certificateService.convertToDto(mockCertificate);
    // then
    assertThat(certificateDTO, is(equalTo(mockCertificateDTO)));
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
