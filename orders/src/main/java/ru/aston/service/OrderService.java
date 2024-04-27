package ru.aston.service;

import ru.aston.dto.OrderDto;
import ru.aston.dto.NewOrderDto;

import java.util.List;

public interface OrderService {
    OrderDto postOrder(Long userId, NewOrderDto order);

    void delete(Long userId, Long orderId);

    OrderDto getOrderById(Long userId, Long orderId);

    List<OrderDto> getAllOrders(Long userId);

    OrderDto updateOrder(Long userId, Long orderId, OrderDto orderDto);

    List<OrderDto> getAllOrdersAdmin();

    OrderDto getOrderByIdAdmin(Long orderId);
}