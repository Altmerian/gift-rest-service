package com.epam.esm.service;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CertificateServiceImpl implements CertificateService {

  private final CertificateRepository certificateRepository;
  private final TagRepository tagRepository;

  @Autowired
  public CertificateServiceImpl(
      CertificateRepository certificateRepository, TagRepository tagRepository) {
    this.certificateRepository = certificateRepository;
    this.tagRepository = tagRepository;
  }

  @Override
  public List<Certificate> getAll() {
    List<Certificate> certificates = certificateRepository.getAll();
    for (Certificate certificate : certificates) {
      certificate.setTags(tagRepository.getByCertificateId(certificate.getId()));
    }
    return certificates;
  }

  @Override
  public Certificate getById(long id) {
    Certificate certificate = certificateRepository.getById(id);
    certificate.setTags(tagRepository.getByCertificateId(certificate.getId()));
    return certificate;
  }

  @Override
  public long create(Certificate certificate) {
    long certificateId = certificateRepository.create(certificate);
    certificate.setId(certificateId);
    for (Tag tag : certificate.getTags()) {
      Optional<Tag> tagOptional = tagRepository.getByName(tag.getName());
      long tagId = tagOptional.map(Tag::getId).orElseGet(() -> tagRepository.create(tag));
      certificateRepository.addCertificateTag(certificateId, tagId);
    }
    return certificateId;
  }

  @Override
  public long update(Certificate certificate) {
    return certificateRepository.update(certificate);
  }

  @Override
  public boolean delete(long id) {
    return certificateRepository.delete(id);
  }

  @Override
  public boolean foundDuplicate(String name, int durationInDays, BigDecimal price, List<Tag> tags) {
    Optional<Certificate> certificate =
        certificateRepository.getByNameDurationPrice(name, durationInDays, price);
    if (certificate.isPresent()) {
      long certificateId = certificate.get().getId();
      List<Tag> tagsList = tagRepository.getByCertificateId(certificateId);
      return tagsList.equals(tags);
    } else {
      return false;
    }
  }
}
