package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import com.epam.esm.specification.Specification;

import java.util.List;

/** Represents base order repository interface for common operations with data storage. */
public interface OrderRepository extends BaseRepository<Order> {

  /**
   * Counts overall quantity of user's orders
   *
   * @param userId user Id
   * @return total amount of orders
   */
  long countAll(long userId);

  /**
   * Makes a query for orders that match given criteria through {@code Specification}
   *
   * @param page number of page to view
   * @param size number of orders per page
   * @param specification order specification with necessary parameters
   * @return list of orders that match the specification
   */
  List<Order> query(Specification<Order> specification, int page, int size);
}
