package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import com.epam.esm.specification.Specification;

import java.util.List;
import java.util.Optional;

/** Represents base order repository interface for common operations with data storage. */
public interface OrderRepository {

  /**
   * Retrieves all persisted orders
   *
   * @param page number of page to view
   * @param size number of orders per page
   * @return list of orders
   */
  List<Order> getAll(int page, int size);

  /**
   * Counts all orders
   *
   * @return total amount of orders
   */
  long countAll();

  /**
   * Counts overall quantity of user's orders
   *
   * @param userId user Id
   * @return total amount of orders
   */
  long countAll(long userId);

  /**
   * Retrieves user's order with given id.
   *
   * @param id order id
   * @return {@code Optional} containing order with corresponding id and other parameters or {@code
   *     Optional.empty()} if order with given id doesn't exist
   */
  Optional<Order> get(long id);

  /**
   * Persists given order in the repository.
   *
   * @param order order to persist
   * @return id of the successfully saved order
   */
  long create(Order order);

  /**
   * Makes a query for orders that match given criteria through {@code Specification}
   *
   * @param page number of page to view
   * @param size number of orders per page
   * @param specification order specification with necessary parameters
   * @return list of orders that match the specification
   */
  List<Order> query(Specification<Order> specification, int page, int size);

  /**
   * Delete specified order from the repository.
   *
   * @param order order to be removed from repository
   */
  void delete(Order order);
}
