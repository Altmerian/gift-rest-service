package com.epam.esm.service;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceConflictException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
    when(certificateRepository.getAll(1, 1)).thenReturn(Collections.singletonList(mockCertificate));
    // when
    List<CertificateDTO> certificateDTOList = certificateService.getAll(1, 1);
    // then
    verify(certificateRepository).getAll(1, 1);
    assertThat(certificateDTOList, hasSize(1));
  }

  @Test
  public void sendQuery_whenQueryWithParameters_expectedCertificateDTOList() {
    // given
    when(certificateRepository.query(ArgumentMatchers.any(), anyInt(), anyInt()))
        .thenReturn(Collections.singletonList(mockCertificate));
    // when
    List<CertificateDTO> certificateDTOList =
        certificateService.sendQuery("tagName", "searchFor", "sortBy", 1, 20);
    // then
    verify(certificateRepository).query(ArgumentMatchers.any(), anyInt(), anyInt());
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
    when(certificateRepository.get(nonexistentCertificateId)).thenReturn(Optional.empty());
    // when
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
    when(certificateService.convertToEntity(certificateDTO)).thenReturn(certificate);
    // when
    certificateService.update(1L, certificateDTO);
    // then
    verify(certificateRepository).get(1L);
    verify(certificateRepository).update(certificate);
  }

  @Test
  public void delete_givenCertificateDTOId_shouldInvokeRepositoryDeleteMethod() {
    // given
    long certificateDTOId = 1L;
    when(certificateRepository.get(certificateDTOId)).thenReturn(Optional.of(mockCertificate));
    // when
    certificateService.delete(certificateDTOId);
    // then
    verify(certificateRepository).get(certificateDTOId);
    verify(certificateRepository).delete(mockCertificate);
  }

  @Test
  public void checkDuplicate_givenNamePriceDurationTags_expectedException() {
    // given
    when(certificateRepository.query(ArgumentMatchers.any(), anyInt(), anyInt()))
        .thenReturn(Collections.singletonList(mockCertificate));
    when(mockCertificate.getId()).thenReturn(1L);
    // when
    Executable checkingAttempt = () -> certificateService.checkForDuplicate(mockCertificateDTO);
    // then
    assertThrows(ResourceConflictException.class, checkingAttempt);
    verify(certificateRepository).query(ArgumentMatchers.any(), anyInt(), anyInt());
  }

  @Test
  public void checkDuplicate_uniqueNamePriceDurationTags_expectedNotToThrow() {
    // given
    when(certificateRepository.query(ArgumentMatchers.any(), anyInt(), anyInt())).thenReturn(Collections.emptyList());
    // when
    Executable checkingAttempt = () -> certificateService.checkForDuplicate(mockCertificateDTO);
    // then
    assertDoesNotThrow(checkingAttempt);
    verify(certificateRepository).query(ArgumentMatchers.any(), anyInt(), anyInt());
  }

  @Test
  void fetchCertificateTags_tagWithUniqueName_registeredCertificateTag() {
    // given
    when(tagRepository.contains(mockTag)).thenReturn(false);
    // when
    certificateService.fetchCertificateTags(Collections.singleton(mockTag));
    // then
    verify(tagRepository).contains(mockTag);
    verify(tagRepository).create(mockTag);
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
