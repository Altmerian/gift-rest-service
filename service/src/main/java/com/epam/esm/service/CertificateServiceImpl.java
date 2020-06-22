package com.epam.esm.service;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.specification.certificate.NamePriceDurationCertificateSQLSpecification;
import com.epam.esm.specification.certificate.SearchAndSortCertificateSQLSpecification;
import com.epam.esm.specification.tag.CertificateIdTagSQLSpecification;
import com.epam.esm.specification.tag.NameTagSQLSpecification;
import com.epam.esm.util.Precondition;
import com.google.common.annotations.VisibleForTesting;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
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
      CertificateRepository repository, TagRepository tagRepository, ModelMapper modelMapper) {
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
    SearchAndSortCertificateSQLSpecification sqlSpecification =
        new SearchAndSortCertificateSQLSpecification(tagName, searchFor, sortBy);
    List<Certificate> certificates = repository.query(sqlSpecification);
    return certificates.stream().map(this::convertToDto).collect(Collectors.toList());
  }

  @Override
  public CertificateDTO getById(long id) {
    Optional<Certificate> certificateOptional = repository.get(id);
    if (!certificateOptional.isPresent()) {
      throw new ResourceNotFoundException("Can't find a certificate with id = " + id);
    }
    Certificate certificate = certificateOptional.get();
    CertificateIdTagSQLSpecification tagSQLSpecification =
        new CertificateIdTagSQLSpecification(certificate.getId());
    certificate.setTags(new HashSet<>(tagRepository.query(tagSQLSpecification)));
    return convertToDto(certificate);
  }

  @Override
  @Transactional
  public long create(CertificateDTO certificateDTO) {
    Certificate certificate = convertToEntity(certificateDTO);
    long certificateId = repository.create(certificate);
    certificate.setId(certificateId);
    for (Tag tag : certificate.getTags()) {
      addCertificateTag(certificateId, tag);
    }
    return certificateId;
  }

  @Override
  @Transactional
  public boolean update(long id, CertificateDTO certificateDTO) {
    Precondition.checkExistence(repository.get(id));
    Certificate certificate = convertToEntity(certificateDTO);
    certificate.setId(id);
    boolean result = repository.update(certificate);
    if (certificate.getTags() == null) {
      return result;
    }
    boolean clearResult = repository.clearCertificateTags(id);
    Set<Tag> tags = certificate.getTags();
    tags.forEach(tag -> addCertificateTag(id, tag));
    return result && clearResult;
  }

  @Override
  public boolean delete(long id) {
    Certificate certificate = Precondition.checkExistence(repository.get(id));
    return repository.delete(certificate);
  }

  @Override
  public boolean foundDuplicate(CertificateDTO certificateDTO) {
    NamePriceDurationCertificateSQLSpecification sqlSpecification =
        new NamePriceDurationCertificateSQLSpecification(
            certificateDTO.getName(),
            certificateDTO.getPrice(),
            certificateDTO.getDurationInDays());
    List<Certificate> certificateList = repository.query(sqlSpecification);
    long certificateId = certificateList.stream().findFirst().map(Certificate::getId).orElse(0L);
    if (certificateId == 0L) {
      return false;
    }
    CertificateIdTagSQLSpecification tagSQLSpecification =
        new CertificateIdTagSQLSpecification(certificateId);
    List<Tag> tagsList = tagRepository.query(tagSQLSpecification);
    Set<TagDTO> persistedSet =
        tagsList.stream().map(this::convertTagToDto).collect(Collectors.toSet());
    return compareTagDTOSets(certificateDTO.getTags(), persistedSet);
  }

  private boolean compareTagDTOSets(Set<TagDTO> incomingTagSet, Set<TagDTO> persistedTagSet) {
        incomingTagSet.stream()
            .filter(tagDTO -> tagDTO.getName() == null)
            .forEach(tagDTO -> tagDTO.setName(
                        tagRepository.get(tagDTO.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Tag hasn't been found"))
                            .getName()));
    Set<TagDTO> tagDTOs = new HashSet<>(incomingTagSet);
    return persistedTagSet.equals(tagDTOs);
  }

  @VisibleForTesting
  boolean addCertificateTag(long certificateId, Tag tag) {
    long tagId;
    if (tag.getId() == 0) {
      NameTagSQLSpecification tagSQLSpecification = new NameTagSQLSpecification(tag.getName());
      boolean tagExists = tagRepository.contains(tag);
      tagId =
          tagExists
              ? tagRepository.query(tagSQLSpecification).get(0).getId()
              : tagRepository.create(tag);
    } else {
      tagId = tag.getId();
      if (!tagRepository.get(tagId).isPresent()) {
        throw new ResourceNotFoundException("Can't find a tag with id = " + tagId);
      }
    }
    return repository.addCertificateTag(certificateId, tagId);
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
