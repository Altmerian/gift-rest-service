package com.epam.esm.service;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.repository.OrderRepository;

import java.util.List;

/**
 * Represents an interface of service which interacts with the underlying repository layer for
 * order-related actions. An instance of order repository {@link OrderRepository} should be
 * aggregated during implementation.
 */
public interface OrderService {

  /**
   * Gets data of all orders from the repository layer.
   *
   * @return list of orders in certain transfer format
   * @param page number of page to view
   * @param size number of orders per page
   */
  List<OrderDTO> getAll(int page, int size);

  /**
   * Gets data of all orders of the particular user from the repository layer.
   *
   * @return list of orders in certain transfer format
   * @param userId user id
   * @param page number of page to view
   * @param size number of orders per page
   */
  List<OrderDTO> getByUserId(long userId, int page, int size);

  /**
   * Gets order data with given id from the repository layer.
   *
   * @return order with given id in certain transfer format
   */
  OrderDTO getById(long orderId);

  /**
   * Gets data of user's order with given id from the repository layer.
   *
   * @return order with given id in certain transfer format
   */
  OrderDTO getByUserIdAndOrderId(long userId, long id);

  /**
   * Invokes repository method to persist user's order data in the system
   *
   * @param userId user id
   * @param orderDTO order data in transfer format
   * @return id of successfully persisted order
   */
  long create(long userId, OrderDTO orderDTO);

  /**
   * Invokes repository method to mark user's order as deleted in the system
   *
   * @param userId user id
   * @param id id of the order to delete
   */
  void delete(long userId, long id);

  /**
   * Admin version of method to mark an order as deleted in the system
   *
   * @param id id of the order to delete
   */
  void delete(long id);

  /**
   * Counts all orders in the system
   *
   * @return total amount of orders
   */
  long countAll();

  /**
   * Counts overall quantity of orders of the particular user in the system
   *
   * @return total amount of orders
   * @param userId user id
   */
  long countAll(long userId);

  /**
   * Gets data about the most widely used tag of a user with the highest cost of all orders
   *
   * @return List of widely used tags
   * @param userId user id
   */
  List<TagDTO> getWidelyUsedTagsOfUser(long userId);
}
