package com.epam.esm.controller;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.OrderListDTO;
import com.epam.esm.dto.View;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.ModelAssembler;
import com.epam.esm.util.PageParseHelper;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Controller to handle all orders related requests. Then requests depending on requests parameters
 * are handled with the appropriate method.
 */
@RestController
@RequestMapping("/api/v1/orders")
@PreAuthorize("hasRole('ADMIN')")
public class OrderRestController {

  /** Represents service layer to implement a domain logic and interaction with repository layer. */
  private final OrderService orderService;

  private final PageParseHelper pageParseHelper;

  @Autowired
  public OrderRestController(OrderService orderService, PageParseHelper pageParseHelper) {
    this.orderService = orderService;
    this.pageParseHelper = pageParseHelper;
  }

  /**
   * Handles requests that aim to get data about all orders in the system
   *
   * @return response with body filled by requested data.
   */
  @RequestMapping(method = GET)
  @JsonView(View.Public.class)
  public OrderListDTO getAllOrders(
      @RequestParam(value = "page", required = false) String page,
      @RequestParam(value = "size", required = false) String size,
      HttpServletResponse resp) {
    long totalCount = orderService.countAll();
    resp.setHeader("X-Total-Count", String.valueOf(totalCount));
    int intPage = pageParseHelper.parsePage(page);
    int intSize = pageParseHelper.parseSize(size);
    List<OrderDTO> orders = orderService.getAll(intPage, intSize);
    orders.forEach(orderDTO -> ModelAssembler.addOrderSelfLink(orderDTO, resp));
    OrderListDTO orderListDTO = new OrderListDTO(orders);
    ModelAssembler.addOrderListLinks(orderListDTO, resp);
    return orderListDTO;
  }

  /**
   * Handles requests to get data about order with a specific id
   *
   * @param orderId order id
   * @return response with payload filled by data of the searched order
   */
  @GetMapping("/{orderId:\\d+}")
  @JsonView(View.ExtendedPublic.class)
  public OrderDTO getOrderById(@PathVariable long orderId, HttpServletResponse resp) {
    OrderDTO orderDTO = orderService.getById(orderId);
    ModelAssembler.addOrderLinks(orderDTO, resp);
    return orderDTO;
  }

  /**
   * Handles requests which use DELETE HTTP method to mark an order as deleted in the system
   *
   * @param id order id
   */
  @DeleteMapping(value = "/{id:\\d+}")
  public ResponseEntity<?> delete(@PathVariable("id") long id) {
    orderService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
