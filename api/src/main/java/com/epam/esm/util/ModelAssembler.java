package com.epam.esm.util;

import com.epam.esm.controller.CertificateRestController;
import com.epam.esm.controller.OrderRestController;
import com.epam.esm.controller.TagRestController;
import com.epam.esm.controller.UserRestController;
import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.CertificatePatchDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.UserDTO;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ModelAssembler {
  public static void addTagSelfLink(TagDTO tagDTO, HttpServletResponse resp) {
    if (!tagDTO.hasLink("self")) {
      tagDTO.add(
          linkTo(methodOn(TagRestController.class).getById(tagDTO.getId(), resp)).withSelfRel());
    }
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
    addCertificateSelfLink(certificateDTO, resp);
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
    if (!certificateDTO.isDeleted()) {
      certificateDTO.add(
          linkTo(methodOn(CertificateRestController.class).delete(certificateDTO.getId()))
              .withRel("delete"));
    }
    certificateDTO.add(
        linkTo(methodOn(CertificateRestController.class).getAll(null, null, null, "1", "10", resp))
            .withRel("getAll"));
  }

  public static void addOrderSelfLink(OrderDTO orderDTO, HttpServletResponse resp) {
    orderDTO.add(
        linkTo(methodOn(OrderRestController.class).getOrderById(orderDTO.getId(), resp))
            .withSelfRel());
    addUserSelfLink(orderDTO.getUser(), resp);
    orderDTO
        .getCertificates()
        .forEach(certificateDTO -> addCertificateSelfLink(certificateDTO, resp));
  }

  public static void addOrderLinks(OrderDTO orderDTO, HttpServletResponse resp) {
    addOrderSelfLink(orderDTO, resp);
    if (!orderDTO.isDeleted()) {
      orderDTO.add(
          linkTo(methodOn(OrderRestController.class).delete(orderDTO.getId())).withRel("delete"));
    }
    orderDTO.add(
        linkTo(methodOn(OrderRestController.class).getAllOrders("1", "10", resp))
            .withRel("getAll"));
  }

  public static void addUserSelfLink(UserDTO userDTO, HttpServletResponse resp) {
    userDTO.add(
        linkTo(methodOn(UserRestController.class).getUserById(userDTO.getId(), resp))
            .withSelfRel());
  }

  public static void addUserLinks(UserDTO userDTO, HttpServletResponse resp) {
    addUserSelfLink(userDTO, resp);
    if (!userDTO.isDeleted()) {
      userDTO.add(
          linkTo(methodOn(UserRestController.class).deleteUser(userDTO.getId())).withRel("delete"));
    }
    userDTO.add(
        linkTo(methodOn(UserRestController.class).getAllUsers("1", "10", resp)).withRel("getAll"));
  }

  public static void addUsersOrderSelfLink(
      long userId, OrderDTO orderDTO, HttpServletResponse resp) {
    orderDTO.add(
        linkTo(methodOn(UserRestController.class).getUserOrderById(userId, orderDTO.getId(), resp))
            .withSelfRel());
    addUserSelfLink(orderDTO.getUser(), resp);
    orderDTO
        .getCertificates()
        .forEach(certificateDTO -> addCertificateSelfLink(certificateDTO, resp));
  }

  public static void addUsersOrderLinks(long userId, OrderDTO orderDTO, HttpServletResponse resp) {
    addUsersOrderSelfLink(userId, orderDTO, resp);
    if (!orderDTO.isDeleted()) {
      orderDTO.add(
          linkTo(methodOn(UserRestController.class).deleteOrder(userId, orderDTO.getId()))
              .withRel("delete"));
    }
    orderDTO.add(
        linkTo(methodOn(UserRestController.class).getUserOrders(userId, "1", "10", resp))
            .withRel("getAll"));
  }
}
