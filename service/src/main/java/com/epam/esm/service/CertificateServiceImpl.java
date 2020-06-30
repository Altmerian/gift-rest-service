package com.epam.esm.service;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.MinorResourceNotFoundException;
import com.epam.esm.exception.ResourceConflictException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.specification.CertificateIdTagSpecification;
import com.epam.esm.specification.NamePriceDurationCertificateSpecification;
import com.epam.esm.specification.NameTagSpecification;
import com.epam.esm.specification.SearchAndSortCertificateSpecification;
import com.google.common.annotations.VisibleForTesting;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl implements CertificateService {

  private final CertificateRepository repository;
  private final TagRepository tagRepository;
  private final ModelMapper modelMapper;

  @Autowired
  public CertificateServiceImpl(
      @Qualifier("certificateJPARepository")CertificateRepository repository,
      @Qualifier("tagJPARepository") TagRepository tagRepository,
      ModelMapper modelMapper) {
    this.repository = repository;
    this.tagRepository = tagRepository;
    this.modelMapper = modelMapper;
  }

  @Override
  public List<CertificateDTO> getAll() {
    return repository.getAll().stream().map(this::convertToDto).collect(Collectors.toList());
  }

  @Override
  public List<CertificateDTO> sendQuery(String tagName, String searchFor, String sortBy) {
    SearchAndSortCertificateSpecification Specification =
        new SearchAndSortCertificateSpecification(tagName, searchFor, sortBy);
    List<Certificate> certificates = repository.query(Specification);
    return certificates.stream().map(this::convertToDto).collect(Collectors.toList());
  }

  @Override
  public CertificateDTO getById(long id) {
    Certificate certificate =
        repository.get(id).orElseThrow(() -> new ResourceNotFoundException(id));
    CertificateIdTagSpecification tagSpecification =
        new CertificateIdTagSpecification(certificate.getId());
    certificate.setTags(new HashSet<>(tagRepository.query(tagSpecification)));
    return convertToDto(certificate);
  }

  @Override
  @Transactional
  public long create(CertificateDTO certificateDTO) {
    checkForDuplicate(certificateDTO);
    Certificate certificate = convertToEntity(certificateDTO);
    long certificateId = repository.create(certificate);
    certificate.setId(certificateId);
    certificate.getTags().forEach(tag -> addCertificateTag(certificateId, tag));
    return certificateId;
  }

  @Override
  @Transactional
  public void update(long id, CertificateDTO certificateDTO) {
    repository.get(id).orElseThrow(() -> new ResourceNotFoundException(id));
    checkForDuplicate(certificateDTO);
    Certificate certificate = convertToEntity(certificateDTO);
    certificate.setId(id);
    repository.update(certificate);
    repository.clearCertificateTags(id);
    certificate.getTags().forEach(tag -> addCertificateTag(id, tag));
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

  @VisibleForTesting
  @Transactional
  void addCertificateTag(long certificateId, Tag tag) {
    long tagId;
    if (tag.getId() == null || tag.getId() == 0) {
      NameTagSpecification tagSpecification = new NameTagSpecification(tag.getName());
      boolean tagExists = tagRepository.contains(tag);
      tagId =
          tagExists
              ? tagRepository.query(tagSpecification).get(0).getId()
              : tagRepository.create(tag);
    } else {
      tagId = tag.getId();
      if (!tagRepository.get(tagId).isPresent()) {
        throw new MinorResourceNotFoundException(tag.getClass(), tagId);
      }
    }
    repository.addCertificateTag(certificateId, tagId);
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
