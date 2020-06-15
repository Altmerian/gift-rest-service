package com.epam.esm.service;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
  public List<CertificateDTO> getAll(String tagName, String searchFor, String sortBy) {
    List<Certificate> certificates = repository.getAll(tagName, searchFor, sortBy);
    return certificates.stream().map(this::convertToDto).collect(Collectors.toList());
  }

  @Override
  public CertificateDTO getById(long id) {
    Optional<Certificate> certificateOptional = repository.getById(id);
    if (!certificateOptional.isPresent()) {
      throw new ResourceNotFoundException("Can't find a certificate with id = " + id);
    }
    Certificate certificate = certificateOptional.get();
    certificate.setTags(tagRepository.getByCertificateId(certificate.getId()));
    return convertToDto(certificate);
  }

  @Override
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
  public void update(long id, CertificateDTO certificateDTO) {
    Certificate certificate = convertToEntity(certificateDTO);
    repository.update(id, certificate);
    if (certificate.getTags() == null) {
      return;
    }
    Set<Tag> tags = certificate.getTags();
    Set<Tag> existingTags = tagRepository.getByCertificateId(id);
    tags.stream()
        .filter(tag -> !existingTags.contains(tag))
        .forEach(tag -> addCertificateTag(id, tag));
  }

  @Override
  public void delete(long id) {
    repository.delete(id);
  }

  @Override
  public boolean foundDuplicate(
      String name, int durationInDays, BigDecimal price, Set<TagDTO> tags) {
    Optional<Certificate> certificate =
        repository.getByNameDurationPrice(name, durationInDays, price);
    if (certificate.isPresent()) {
      long certificateId = certificate.get().getId();
      Set<Tag> tagsList = tagRepository.getByCertificateId(certificateId);
      Set<TagDTO> tagSet = tagsList.stream().map(this::convertTagToDto).collect(Collectors.toSet());
      return tags.equals(tagSet);
    } else {
      return false;
    }
  }

  private void addCertificateTag(long certificateId, Tag tag) {
    Optional<Tag> tagOptional = tagRepository.getByName(tag.getName());
    long tagId = tagOptional.map(Tag::getId).orElseGet(() -> tagRepository.create(tag));
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
