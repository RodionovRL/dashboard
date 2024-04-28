package ru.aston.repository;

import ru.aston.OrderStatus;
import ru.aston.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatusOrCustomerIdOrExecutorId(
            OrderStatus orderStatus, Long customerId, Long executorId);
}