package com.epam.esm.service;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.sun.tools.javac.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

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
    certificate.setCreationDate(ZonedDateTime.now());
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
    assertThat(modelMapper.map(certificate, CertificateDTO.class), is(equalTo(certificateDTO)));
  }

  @Test
  void map_certificateDTO_certificate() {
    assertThat(modelMapper.map(certificateDTO, Certificate.class), is(equalTo(certificate)));
  }
}
