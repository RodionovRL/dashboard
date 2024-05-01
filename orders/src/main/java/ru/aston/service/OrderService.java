package ru.aston.service;

import ru.aston.dto.OrderDto;
import ru.aston.dto.NewOrderDto;

import java.util.List;

/**
 * Service interface for managing orders.
 * This interface defines methods for creating, retrieving, updating, and deleting orders.
 */
public interface OrderService {

    /**
     * Creates a new order.
     * @param userId The ID of the user placing the order.
     * @param order The details of the new order.
     * @return The created order DTO.
     */
    OrderDto postOrder(Long userId, NewOrderDto order);

    /**
     * Deletes an order.
     * @param userId The ID of the user who owns the order.
     * @param orderId The ID of the order to delete.
     */
    void delete(Long userId, Long orderId);

    /**
     * Retrieves an order by ID.
     * @param userId The ID of the user who retrieves the order.
     * @param orderId The ID of the order to retrieve.
     * @return The order DTO with the specified ID.
     */
    OrderDto getOrderById(Long userId, Long orderId);

    /**
     * Retrieves all orders for a user.
     * @param userId The ID of the user who retrieves orders.
     * @return A list of order DTOs retrieved by user.
     */
    List<OrderDto> getAllOrders(Long userId);

    /**
     * Updates an existing order.
     * @param userId The ID of the user who updates the order.
     * @param orderId The ID of the order to update.
     * @param orderDto The updated order details.
     * @return The updated order DTO.
     */
    OrderDto updateOrder(Long userId, Long orderId, OrderDto orderDto);

    /**
     * Retrieves all orders for administrative purposes.
     * @return A list of all order DTOs in the system.
     */
    List<OrderDto> getAllOrdersAdmin();

    /**
     * Retrieves an order by ID for administrative purposes.
     * @param orderId The ID of the order to retrieve.
     * @return The order DTO with the specified ID.
     */
    OrderDto getOrderByIdAdmin(Long orderId);
}