package ru.aston.repository;

import ru.aston.OrderStatus;
import ru.aston.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This interface extends JpaRepository to gain access to basic CRUD operations for orders.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Finds orders in the database based on status, customer ID, or executor ID.
     * @param orderStatus The status of the orders to find.
     * @param customerId The ID of the customer associated with the orders to find.
     * @param executorId The ID of the executor associated with the orders to find.
     * @return A list of orders matching the specified criteria.
     */
    List<Order> findByStatusOrCustomerIdOrExecutorId(
            OrderStatus orderStatus, Long customerId, Long executorId);
}