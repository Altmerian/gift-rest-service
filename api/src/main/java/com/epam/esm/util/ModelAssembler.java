package com.epam.esm.util;

import com.epam.esm.controller.CertificateRestController;
import com.epam.esm.controller.TagRestController;
import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.CertificatePatchDTO;
import com.epam.esm.dto.TagDTO;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ModelAssembler {
  public static void addTagSelfLink(TagDTO tagDTO, HttpServletResponse resp) {
    tagDTO.add(
        linkTo(methodOn(TagRestController.class).getById(tagDTO.getId(), resp)).withSelfRel());
  }

  public static void addTagLinks(TagDTO tagDTO, HttpServletResponse resp) {
    tagDTO.add(
        linkTo(methodOn(TagRestController.class).getById(tagDTO.getId(), resp)).withSelfRel());
    tagDTO.add(linkTo(methodOn(TagRestController.class).delete(tagDTO.getId())).withRel("delete"));
  }

  public static void addCertificateSelfLink(
      CertificateDTO certificateDTO, HttpServletResponse resp) {
    certificateDTO.add(
        linkTo(methodOn(CertificateRestController.class).getById(certificateDTO.getId(), resp))
            .withSelfRel());
    certificateDTO.getTags().forEach(tagDTO -> addTagSelfLink(tagDTO, resp));
  }

  public static void addCertificateLinks(CertificateDTO certificateDTO, HttpServletResponse resp) {
    certificateDTO.add(
        linkTo(methodOn(CertificateRestController.class).getById(certificateDTO.getId(), resp))
            .withSelfRel());
    certificateDTO.add(
        linkTo(
                methodOn(CertificateRestController.class)
                    .update(certificateDTO.getId(), new CertificateDTO()))
            .withRel("update"));
    certificateDTO.add(
        linkTo(
                methodOn(CertificateRestController.class)
                    .patch(certificateDTO.getId(), new CertificatePatchDTO()))
            .withRel("patch"));
    certificateDTO.add(
        linkTo(methodOn(CertificateRestController.class).delete(certificateDTO.getId()))
            .withRel("delete"));
    certificateDTO.add(
        linkTo(methodOn(CertificateRestController.class).getAll(null, null, null, "1", "10", resp))
            .withRel("getAll"));
    certificateDTO.getTags().forEach(tagDTO -> addTagSelfLink(tagDTO, resp));
  }
}
