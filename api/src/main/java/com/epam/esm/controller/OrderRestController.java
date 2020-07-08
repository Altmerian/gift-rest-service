package com.epam.esm.controller;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.View;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.ParseHelper;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("api/v1/orders")
class OrderRestController {

  /** Represents service layer to implement a domain logic and interaction with repository layer. */
  private final OrderService orderService;

  private final ParseHelper parseHelper;

  @Autowired
  public OrderRestController(OrderService orderService, ParseHelper parseHelper) {
    this.orderService = orderService;
    this.parseHelper = parseHelper;
  }

  /**
   * Handles requests that aim to get data about all orders in the system
   *
   * @return response with body filled by requested data.
   */
  @RequestMapping(method = GET)
  @JsonView(View.ExtendedPublic.class)
  public List<OrderDTO> getAllOrders(
      @RequestParam(value = "page", required = false) String page,
      @RequestParam(value = "size", required = false) String size,
      HttpServletResponse resp) {
    resp.setHeader("X-Total-Count", String.valueOf(orderService.countAll()));
    int intPage = parseHelper.parsePage(page);
    int intSize = parseHelper.parseSize(size);
    return orderService.getAll(intPage, intSize);
  }

  /**
   * Handles requests to get data about order with a specific id
   *
   * @param orderId order id
   * @return response with payload filled by data of the searched order
   */
  @GetMapping("/{orderId:\\d+}")
  @JsonView(View.ExtendedPublic.class)
  public OrderDTO getOrderById(@PathVariable long orderId) {
    return orderService.getById(orderId);
  }

  /**
   * Handles requests to get data about the most widely used tag of a user with the highest cost of
   * all orders
   *
   * @return response with payload filled by data of the searched order
   */
  @GetMapping("/tags")
  public List<TagDTO> getTagsOfUserWithValuableOrders() {
    return orderService.getTagsOfUserWithValuableOrders();
  }
}
