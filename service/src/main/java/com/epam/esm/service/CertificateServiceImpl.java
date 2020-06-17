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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
  public void update(long id, CertificateDTO certificateDTO) {
    Precondition.checkExistence(repository.get(id));
    Certificate certificate = convertToEntity(certificateDTO);
    certificate.setId(id);
    repository.update(certificate);
    if (certificate.getTags() == null) {
      return;
    }
    repository.clearCertificateTags(id);
    Set<Tag> tags = certificate.getTags();
    tags.forEach(tag -> addCertificateTag(id, tag));
  }

  @Override
  public void delete(long id) {
    Certificate certificate = Precondition.checkExistence(repository.get(id));
    repository.delete(certificate);
  }

  @Override
  public boolean foundDuplicate(
      String name, int durationInDays, BigDecimal price, Set<TagDTO> tags) {
    NamePriceDurationCertificateSQLSpecification sqlSpecification =
        new NamePriceDurationCertificateSQLSpecification(name, price, durationInDays);
    List<Certificate> certificateList = repository.query(sqlSpecification);
    if (!certificateList.isEmpty()) {
      long certificateId = certificateList.get(0).getId();
      CertificateIdTagSQLSpecification tagSQLSpecification =
          new CertificateIdTagSQLSpecification(certificateId);
      List<Tag> tagsList = tagRepository.query(tagSQLSpecification);
      Set<TagDTO> tagSet = tagsList.stream().map(this::convertTagToDto).collect(Collectors.toSet());
      return tags.equals(tagSet);
    } else {
      return false;
    }
  }

  private void addCertificateTag(long certificateId, Tag tag) {
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
    repository.addCertificateTag(certificateId, tagId);
  }

  private CertificateDTO convertToDto(Certificate certificate) {
    return modelMapper.map(certificate, CertificateDTO.class);
  }

  private Certificate convertToEntity(CertificateDTO certificateDTO) {
    return modelMapper.map(certificateDTO, Certificate.class);
  }

  private TagDTO convertTagToDto(Tag tag) {
    return modelMapper.map(tag, TagDTO.class);
  }
}
