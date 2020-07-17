package com.epam.esm.controller;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.OrderListDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.TagListDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.dto.UserListDTO;
import com.epam.esm.dto.View;
import com.epam.esm.exception.ResourceConflictException;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.util.ModelAssembler;
import com.epam.esm.util.ParseHelper;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controller to handle all users related requests. Then requests depending on requests parameters
 * are handled with the appropriate method.
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

  /** Represents service layer to implement a domain logic and interaction with repository layer. */
  private final UserService userService;

  private final OrderService orderService;
  private final ParseHelper parseHelper;

  @Autowired
  public UserRestController(
      UserService userService, OrderService orderService, ParseHelper parseHelper) {
    this.userService = userService;
    this.orderService = orderService;
    this.parseHelper = parseHelper;
  }

  /**
   * Handles requests that aim to get data about all users in the system
   *
   * @return response with body filled by requested data.
   */
  @GetMapping
  @JsonView(View.Internal.class)
  @PreAuthorize("hasRole('ADMIN')")
  public UserListDTO getAllUsers(
      @RequestParam(value = "page", required = false) String page,
      @RequestParam(value = "size", required = false) String size,
      HttpServletResponse resp) {
    long totalCount = userService.countAll();
    resp.setHeader("X-Total-Count", String.valueOf(totalCount));
    int intPage = parseHelper.parsePage(page);
    int intSize = parseHelper.parseSize(size);
    List<UserDTO> users = userService.getAll(intPage, intSize);
    users.forEach(userDTO -> ModelAssembler.addUserSelfLink(userDTO, resp));
    UserListDTO userListDTO = new UserListDTO(users);
    userListDTO.add(
        linkTo(methodOn(UserRestController.class).createUser(new UserDTO())).withRel("create"));
    return userListDTO;
  }

  /**
   * Handles requests to get data about a user with a specific id
   *
   * @param userId user id
   * @return response with payload filled by data of the searched user
   */
  @GetMapping("/{userId:\\d+}")
  @JsonView(View.Internal.class)
  @PreAuthorize("#userId == principal or hasRole('ADMIN')")
  public UserDTO getUserById(@PathVariable long userId, HttpServletResponse resp) {
    UserDTO userDTO = userService.getById(userId);
    ModelAssembler.addUserLinks(userDTO, resp);
    return userDTO;
  }

  /**
   * Handles requests that aim to get data about all orders of the particular user in the system
   *
   * @return response with body filled by requested data.
   */
  @GetMapping("/{userId:\\d+}/orders")
  @JsonView(View.Public.class)
  @PreAuthorize("#userId == principal or hasRole('ADMIN')")
  public OrderListDTO getUserOrders(
      @PathVariable long userId,
      @RequestParam(value = "page", required = false) String page,
      @RequestParam(value = "size", required = false) String size,
      HttpServletResponse resp) {
    long totalCount = orderService.countAll(userId);
    resp.setHeader("X-Total-Count", String.valueOf(totalCount));
    int intPage = parseHelper.parsePage(page);
    int intSize = parseHelper.parseSize(size);
    List<OrderDTO> orders = orderService.getByUserId(userId, intPage, intSize);
    orders.forEach(orderDTO -> ModelAssembler.addUsersOrderSelfLink(userId, orderDTO, resp));
    OrderListDTO orderListDTO = new OrderListDTO(orders);
    orderListDTO.add(
        linkTo(methodOn(UserRestController.class).createOrder(userId, new OrderDTO(), resp))
            .withRel("create"));
    return orderListDTO;
  }

  /**
   * Handles requests to get data about user's order with a specific id
   *
   * @param userId user id
   * @param orderId order id
   * @return response with payload filled by data of the searched order
   */
  @GetMapping("/{userId:\\d+}/orders/{orderId:\\d+}")
  @JsonView(View.ExtendedPublic.class)
  @PreAuthorize("#userId == principal or hasRole('ADMIN')")
  public OrderDTO getUserOrderById(
      @PathVariable long userId, @PathVariable long orderId, HttpServletResponse resp) {
    OrderDTO orderDTO = orderService.getByUserIdAndOrderId(userId, orderId);
    ModelAssembler.addUsersOrderLinks(userId, orderDTO, resp);
    return orderDTO;
  }

  /**
   * Handles requests to get data about the most widely used tag of a user with the highest cost of
   * all orders
   *
   * @return response with payload filled by data of the searched order
   */
  @GetMapping("/{userId:\\d+}/tags")
  @PreAuthorize("#userId == principal or hasRole('ADMIN')")
  public TagListDTO getWidelyUsedTagsOfUser(@PathVariable long userId, HttpServletResponse resp) {
    List<TagDTO> tags = orderService.getWidelyUsedTagsOfUser(userId);
    tags.forEach(tagDTO -> ModelAssembler.addTagSelfLink(tagDTO, resp));
    return new TagListDTO(tags);
  }

  /**
   * Handles requests which use POST HTTP method and request body with data of the order to be
   * persisted in the system
   *
   * @param userDTO user data in a certain format for transfer
   * @throws ResourceConflictException if user with given name already exists
   */
  @PostMapping
  public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO) {
    long userId = userService.create(userDTO);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(userId)
            .toUri();
    return ResponseEntity.created(location).build();
  }

  /**
   * Handles requests which use POST HTTP method and request body with data of the user's order to
   * be persisted in the system
   *
   * @param userId user id
   * @param orderDTO order data in a certain format for transfer
   * @param resp HTTP response
   * @throws ResourceConflictException if order with given name already exists
   */
  @PostMapping("/{userId:\\d+}/orders")
  @PreAuthorize("#userId == principal")
  public ResponseEntity<?> createOrder(
      @PathVariable long userId, @Valid @RequestBody OrderDTO orderDTO, HttpServletResponse resp) {
    long orderId = orderService.create(userId, orderDTO);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(orderId)
            .toUri();
    return ResponseEntity.created(location).build();
  }

  /**
   * Handles requests which use DELETE HTTP method to delete the certain user
   *
   * @param userId user id
   */
  @DeleteMapping("/{userId:\\d+}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> deleteUser(@PathVariable("userId") long userId) {
    userService.delete(userId);
    return ResponseEntity.noContent().build();
  }

  /**
   * Handles requests which use DELETE HTTP method to delete user's order
   *
   * @param userId user id
   * @param orderId order id
   */
  @DeleteMapping("/{userId:\\d+}/orders/{id:\\d+}")
  @PreAuthorize("#userId == principal or hasRole('ADMIN')")
  public ResponseEntity<?> deleteOrder(
      @PathVariable long userId, @PathVariable("id") long orderId) {
    orderService.delete(userId, orderId);
    return ResponseEntity.noContent().build();
  }
}
