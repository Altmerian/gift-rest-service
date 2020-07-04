package com.epam.esm.service;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.CertificatePatchDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.MinorResourceNotFoundException;
import com.epam.esm.exception.ResourceConflictException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.specification.NamePriceDurationCertificateSpecification;
import com.epam.esm.specification.NameTagSpecification;
import com.epam.esm.specification.SearchAndSortCertificateSpecification;
import com.google.common.annotations.VisibleForTesting;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl implements CertificateService {

  private final CertificateRepository repository;
  private final TagRepository tagRepository;
  private final ModelMapper modelMapper;

  @Autowired
  public CertificateServiceImpl(
      @Qualifier("certificateJPARepository") CertificateRepository repository,
      @Qualifier("tagJPARepository") TagRepository tagRepository,
      ModelMapper modelMapper) {
    this.repository = repository;
    this.tagRepository = tagRepository;
    this.modelMapper = modelMapper;
  }

  @Override
  public List<CertificateDTO> getAll(int page, int size) {
    return repository.getAll(page, size).stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<CertificateDTO> sendQuery(
      String tagName, String searchFor, String sortBy, int page, int size) {
    SearchAndSortCertificateSpecification Specification =
        new SearchAndSortCertificateSpecification(tagName, searchFor, sortBy, page, size);
    List<Certificate> certificates = repository.query(Specification);
    return certificates.stream().map(this::convertToDto).collect(Collectors.toList());
  }

  @Override
  public CertificateDTO getById(long id) {
    Certificate certificate =
        repository.get(id).orElseThrow(() -> new ResourceNotFoundException(id));
    return convertToDto(certificate);
  }

  @Override
  @Transactional
  public long create(CertificateDTO certificateDTO) {
    checkForDuplicate(certificateDTO);
    Certificate certificate = convertToEntity(certificateDTO);
    fetchCertificateTags(certificate.getTags());
    return repository.create(certificate);
  }

  @Override
  @Transactional
  public void update(long id, CertificateDTO certificateDTO) {
    Certificate storedCertificate =
        repository.get(id).orElseThrow(() -> new ResourceNotFoundException(id));
    checkForDuplicate(certificateDTO);
    Certificate certificate = convertToEntity(certificateDTO);
    certificate.setCreationDate(storedCertificate.getCreationDate());
    certificate.setId(id);
    fetchCertificateTags(certificate.getTags());
    repository.update(certificate);
  }

  @Override
  public void modify(long id, CertificatePatchDTO certificatePatchDTO) {
    CertificateDTO certificateDTO = modelMapper.map(certificatePatchDTO, CertificateDTO.class);
    checkForDuplicate(certificateDTO);
    Certificate certificate =
        repository.get(id).orElseThrow(() -> new ResourceNotFoundException(id));
    setCertificateFields(certificate, certificatePatchDTO);
    repository.update(certificate);
  }

  @Override
  public void delete(long id) {
    Certificate certificate =
        repository.get(id).orElseThrow(() -> new ResourceNotFoundException(id));
    repository.delete(certificate);
  }

  @Override
  public void checkForDuplicate(CertificateDTO certificateDTO) throws ResourceConflictException {
    NamePriceDurationCertificateSpecification specification =
        new NamePriceDurationCertificateSpecification(
            certificateDTO.getName(),
            certificateDTO.getPrice(),
            certificateDTO.getDurationInDays());
    List<Certificate> certificateList = repository.query(specification);
    long certificateId = certificateList.stream().findFirst().map(Certificate::getId).orElse(0L);
    if (certificateId != 0L) {
      throw new ResourceConflictException(
          "Your data conflicts with existing resources. "
              + "Certificate with given name, price and duration already exists");
    }
  }

  private void setCertificateFields(
      Certificate certificate, CertificatePatchDTO certificatePatchDTO) {
    for (Field field : certificatePatchDTO.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      try {
        Object value = field.get(certificatePatchDTO);
        if (value != null) {
          Field targetField = certificate.getClass().getDeclaredField(field.getName());
          targetField.setAccessible(true);
          targetField.set(certificate, value);
        }
      } catch (IllegalAccessException | NoSuchFieldException e) {
        throw new RuntimeException("Internal server error.");
      }
    }
  }

  @VisibleForTesting
  @Transactional
  void fetchCertificateTags(Set<Tag> tags) {
    for (Tag tag : tags) {
      long tagId;
      if (tag.getId() == null || tag.getId() == 0) {
        NameTagSpecification tagSpecification = new NameTagSpecification(tag.getName());
        boolean tagExists = tagRepository.contains(tag);
        tagId =
            tagExists
                ? tagRepository.query(tagSpecification).get(0).getId()
                : tagRepository.create(tag);
        tag.setId(tagId);
      } else {
        tagId = tag.getId();
        Optional<Tag> tagOptional = tagRepository.get(tagId);
        if (!tagOptional.isPresent()) {
          throw new MinorResourceNotFoundException(tag.getClass(), tagId);
        }
        tag.setName(tagOptional.get().getName());
      }
    }
  }

  @VisibleForTesting
  CertificateDTO convertToDto(Certificate certificate) {
    return modelMapper.map(certificate, CertificateDTO.class);
  }

  @VisibleForTesting
  Certificate convertToEntity(CertificateDTO certificateDTO) {
    return modelMapper.map(certificateDTO, Certificate.class);
  }

  @VisibleForTesting
  TagDTO convertTagToDto(Tag tag) {
    return modelMapper.map(tag, TagDTO.class);
  }
}
