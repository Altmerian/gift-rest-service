package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

/** Data transfer object representing an orders list */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderListDTO extends RepresentationModel<OrderListDTO> {

  private List<OrderDTO> orders;

  public OrderListDTO() {}

  public OrderListDTO(List<OrderDTO> orders) {
    this.orders = orders;
  }

  public List<OrderDTO> getOrders() {
    return orders;
  }

  public void setOrders(List<OrderDTO> orders) {
    this.orders = orders;
  }
}
