package com.epam.esm.util;

import com.epam.esm.controller.CertificateRestController;
import com.epam.esm.controller.OrderRestController;
import com.epam.esm.controller.TagRestController;
import com.epam.esm.controller.UserRestController;
import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.CertificateListDTO;
import com.epam.esm.dto.CertificatePatchDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.OrderListDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.TagListDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.dto.UserListDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * All methods of this class add links to representation models for HATEOAS purpose
 */
public class ModelAssembler {

  private static final String DEFAULT_PAGE_NUMBER = "1";
  private static final String DEFAULT_PAGE_SIZE = "10";
  private static final String ROLE_GUEST = "ROLE_GUEST";
  private static final String ROLE_ADMIN = "ROLE_ADMIN";
  private static final String SELF_RELATION = "self";
  private static final String GET_ALL_RELATION = "getAll";
  private static final String CREATE_RELATION = "create";
  private static final String UPDATE_RELATION = "update";
  private static final String PATCH_RELATION = "patch";
  private static final String DELETE_RELATION = "delete";

  public static void addTagSelfLink(TagDTO tagDTO, HttpServletResponse resp) {
    if (!tagDTO.hasLink(SELF_RELATION)) {
      tagDTO.add(
          linkTo(methodOn(TagRestController.class).getById(tagDTO.getId(), resp)).withSelfRel());
    }
  }

  public static void addTagLinks(TagDTO tagDTO, HttpServletResponse resp) {
    addTagSelfLink(tagDTO, resp);
    if (appUserIsAdmin()) {
      tagDTO.add(
          linkTo(methodOn(TagRestController.class).delete(tagDTO.getId()))
              .withRel(DELETE_RELATION));
    }
  }

  public static void addTagListLinks(TagListDTO tagListDTO) {
    if (appUserIsAdmin()) {
      tagListDTO.add(
          linkTo(methodOn(TagRestController.class).create(new TagDTO())).withRel(CREATE_RELATION));
    }
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
    if (appUserIsAdmin()) {
      certificateDTO.add(
          linkTo(methodOn(CertificateRestController.class)
                      .update(certificateDTO.getId(), new CertificateDTO()))
              .withRel(UPDATE_RELATION));
      certificateDTO.add(
          linkTo(methodOn(CertificateRestController.class)
                      .patch(certificateDTO.getId(), new CertificatePatchDTO()))
              .withRel(PATCH_RELATION));
      if (!certificateDTO.isDeleted()) {
        certificateDTO.add(
            linkTo(methodOn(CertificateRestController.class).delete(certificateDTO.getId()))
                .withRel(DELETE_RELATION));
      }
    }
    certificateDTO.add(linkTo(CertificateRestController.class).withRel(GET_ALL_RELATION));
  }

  public static void addCertificateListLinks(CertificateListDTO certificateListDTO) {
    if (appUserIsAdmin()) {
      certificateListDTO.add(
          linkTo(methodOn(CertificateRestController.class).create(new CertificateDTO()))
              .withRel(CREATE_RELATION));
    }
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
        linkTo(
                methodOn(OrderRestController.class)
                    .getAllOrders(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, resp))
            .withRel(GET_ALL_RELATION));
  }

  public static void addOrderListLinks(OrderListDTO orderListDTO, HttpServletResponse resp) {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof Integer) {
      int appUserId = (Integer) principal;
      orderListDTO.add(
          linkTo(methodOn(UserRestController.class).createOrder(appUserId, new OrderDTO(), resp))
              .withRel(CREATE_RELATION));
    }
  }

  public static void addUserSelfLink(UserDTO userDTO, HttpServletResponse resp) {
    userDTO.add(
        linkTo(methodOn(UserRestController.class).getUserById(userDTO.getId(), resp))
            .withSelfRel());
  }

  public static void addUserLinks(UserDTO userDTO, HttpServletResponse resp) {
    addUserSelfLink(userDTO, resp);
    if (appUserIsAdmin()) {
      if (!userDTO.isDeleted()) {
        userDTO.add(
            linkTo(methodOn(UserRestController.class).deleteUser(userDTO.getId()))
                .withRel(DELETE_RELATION));
      }
      userDTO.add(
          linkTo(methodOn(UserRestController.class)
                      .getAllUsers(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, resp))
              .withRel(GET_ALL_RELATION));
    }
  }

  public static void addUserListLinks(UserListDTO userListDTO) {
    userListDTO.add(
        linkTo(methodOn(UserRestController.class).createUser(new UserDTO()))
            .withRel(CREATE_RELATION));
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
              .withRel(DELETE_RELATION));
    }
    orderDTO.add(
        linkTo(methodOn(UserRestController.class)
                    .getUserOrders(userId, DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, resp))
            .withRel(GET_ALL_RELATION));
  }

  private static boolean appUserIsAdmin() {
    String appUserRole =
        SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
            .findFirst()
            .map(GrantedAuthority::getAuthority)
            .orElse(ROLE_GUEST);
    return appUserRole.equals(ROLE_ADMIN);
  }
}
