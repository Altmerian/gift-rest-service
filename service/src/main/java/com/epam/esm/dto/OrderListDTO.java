package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof OrderListDTO)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    OrderListDTO that = (OrderListDTO) o;
    return Objects.equals(getOrders(), that.getOrders());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getOrders());
  }
}
