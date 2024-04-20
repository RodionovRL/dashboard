package com.aston.repository;

import com.aston.OrderStatus;
import com.aston.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatusOrCustomerIdOrExecutorId(
            OrderStatus orderStatus, Long customerId, Long executorId);

}
