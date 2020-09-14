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
import com.epam.esm.util.PageParseHelper;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
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

/**
 * Controller to handle all users related requests. Then requests depending on requests parameters
 * are handled with the appropriate method.
 */
@RestController
@RequestMapping("/api/v1/users")
@ApiResponses(value = {@ApiResponse(code = 401, message = "Unauthorized") })
@Api(tags = "Resource: User", description = "Users API for operations with users including registration and methods to operate users orders", authorizations = @Authorization(value = "Bearer"))
public class UserRestController {

  /** Represents service layer to implement a domain logic and interaction with repository layer. */
  private final UserService userService;

  private final OrderService orderService;
  private final PageParseHelper pageParseHelper;

  @Autowired
  public UserRestController(
      UserService userService, OrderService orderService, PageParseHelper pageParseHelper) {
    this.userService = userService;
    this.orderService = orderService;
    this.pageParseHelper = pageParseHelper;
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
    int intPage = pageParseHelper.parsePage(page);
    int intSize = pageParseHelper.parseSize(size);
    List<UserDTO> users = userService.getAll(intPage, intSize);
    users.forEach(userDTO -> ModelAssembler.addUserSelfLink(userDTO, resp));
    UserListDTO userListDTO = new UserListDTO(users);
    ModelAssembler.addUserListLinks(userListDTO);
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
  @ApiResponses(value = {@ApiResponse(code = 404, message = "User not found") })
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
  @ApiResponses(value = {@ApiResponse(code = 404, message = "User not found") })
  public OrderListDTO getUserOrders(
      @PathVariable long userId,
      @RequestParam(value = "page", required = false) String page,
      @RequestParam(value = "size", required = false) String size,
      HttpServletResponse resp) {
    long totalCount = orderService.countAll(userId);
    resp.setHeader("X-Total-Count", String.valueOf(totalCount));
    int intPage = pageParseHelper.parsePage(page);
    int intSize = pageParseHelper.parseSize(size);
    List<OrderDTO> orders = orderService.getByUserId(userId, intPage, intSize);
    orders.forEach(orderDTO -> ModelAssembler.addUsersOrderLinks(userId, orderDTO, resp));
    OrderListDTO orderListDTO = new OrderListDTO(orders);
    ModelAssembler.addOrderListLinks(orderListDTO, resp);
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
  @ApiResponses(value = {@ApiResponse(code = 404, message = "Order not found") })
  public OrderDTO getUserOrderById(
      @PathVariable long userId, @PathVariable long orderId, HttpServletResponse resp) {
    OrderDTO orderDTO = orderService.getByUserIdAndOrderId(userId, orderId);
    ModelAssembler.addUsersOrderLinks(userId, orderDTO, resp);
    ModelAssembler.addGetAllOrdersLink(orderDTO, resp);
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
  @ApiResponses(value = {@ApiResponse(code = 404, message = "User not found") })
  public TagListDTO getWidelyUsedTagsOfUser(@PathVariable long userId, HttpServletResponse resp) {
    List<TagDTO> tags = orderService.getWidelyUsedTagsOfUser(userId);
    tags.forEach(tagDTO -> ModelAssembler.addTagLinks(tagDTO, resp));
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
  @ApiResponses(value = {@ApiResponse(code = 201, message = "Created") })
  public ResponseEntity<Object> createUser(@Valid @RequestBody UserDTO userDTO) {
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
  @ApiResponses(value = {@ApiResponse(code = 201, message = "Created")})
  public ResponseEntity<Object> createOrder(
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
  @ApiResponses(value = {@ApiResponse(code = 204, message = "No content"),@ApiResponse(code = 404, message = "User not found") })
  public ResponseEntity<Object> deleteUser(@PathVariable("userId") long userId) {
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
  @ApiResponses(value = {@ApiResponse(code = 204, message = "No content"),@ApiResponse(code = 404, message = "Order not found") })
  public ResponseEntity<Object> deleteOrder(
      @PathVariable long userId, @PathVariable("id") long orderId) {
    orderService.delete(userId, orderId);
    return ResponseEntity.noContent().build();
  }
}
