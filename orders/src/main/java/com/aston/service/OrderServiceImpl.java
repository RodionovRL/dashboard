package com.aston.service;

import com.aston.OrderStatus;
import com.aston.dto.OrderDto;
import com.aston.dto.NewOrderDto;
import com.aston.mapper.OrderMapper;
import com.aston.model.Order;
import com.aston.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    @Override
    public OrderDto postOrder(Long userId, NewOrderDto order) {
        Order newOrder = OrderMapper.INSTANCE.newOrderDtoToOrder(order);
        newOrder.setCustomerId(userId);

        Order createdOrder = orderRepository.save(newOrder);
        OrderDto orderDto = OrderMapper.INSTANCE.orderToOrderDto(createdOrder);


        String orderJson;
        try {
            orderJson = objectMapper.writeValueAsString(orderDto);
        } catch(JsonProcessingException e) {
            throw new RuntimeException("Failed to create json from orderDto");
        }

        kafkaTemplate.send("order-post", orderJson);

        return orderDto;
    }

    @Override
    public void delete(Long userId, Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            if (order.getCustomerId().equals(userId) && order.getStatus().equals(OrderStatus.NEW)) {
                orderRepository.delete(order);
            } else {
                throw new RuntimeException("You are not authorized to delete this order.");
            }
        } else {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }
    }

    @Override
    public OrderDto getOrderById(Long userId, Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            if (order.getCustomerId().equals(userId) || order.getExecutorId().equals(userId)) {

                // ADD HERE KAFKA WORKS with payment

                return OrderMapper.INSTANCE.orderToOrderDto(order);
            } else {
                throw new RuntimeException("User does not have access to this order");
            }
        } else {
            throw new RuntimeException("Order with ID " + orderId + " not found");
        }
    }

    @Override
    public List<OrderDto> getAllOrders(Long userId) {

        List<Order> ordersList;
        ordersList = orderRepository.findByStatusOrCustomerIdOrExecutorId(
                OrderStatus.NEW, userId, userId);

        List<OrderDto> orderDtoList = ordersList.stream()
                .map(order -> OrderMapper.INSTANCE.orderToOrderDto(order))
                .toList();


        // KAFKA part
        // fetch executor and customer data for each order.



        return orderDtoList;
    }

    @Override
    public OrderDto updateOrder(Long userId, Long orderId, OrderDto orderDto) {
        // Fetch the order from the repository
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Validate that the order belongs to the user or the user is authorized to update it
        validateUserAuthorization(order, userId);

        updateOrderFields(order, userId, orderDto);


        // Kafka operations


        Order updatedOrder = orderRepository.save(order);
        return OrderMapper.INSTANCE.orderToOrderDto(updatedOrder);
    }

    private void validateUserAuthorization(Order order, Long userId) {
        if (!(userId.equals(order.getCustomerId()) || userId.equals(order.getExecutorId()))) {
            throw new RuntimeException("User is not authorized to update the order");
        }
    }

    private void updateOrderFields(Order order, Long userId, OrderDto orderDto) {
        if (userId.equals(order.getCustomerId())) {
            if (order.getStatus() == OrderStatus.NEW) {
                order.setName(orderDto.getName());
                order.setDescription(orderDto.getDescription());
            } else if (order.getStatus() == OrderStatus.DONE
                    && orderDto.getStatus() == OrderStatus.PAID) {
                order.setStatus(OrderStatus.PAID);
            } else {
                throw new RuntimeException("Customer is not authorized to update the order");
            }
        } else if (userId.equals(order.getExecutorId())) {
            if (order.getStatus() == OrderStatus.NEW
                    && orderDto.getStatus() == OrderStatus.IN_PROCESS) {
                order.setStatus(OrderStatus.IN_PROCESS);
            } else if (order.getStatus() == OrderStatus.IN_PROCESS
                    && orderDto.getStatus() == OrderStatus.DONE) {
                order.setStatus(OrderStatus.DONE);
            } else {
                throw new RuntimeException("Executor is not authorized to update the order");
            }
        }
    }

    @Override
    public List<OrderDto> getAllOrdersAdmin() {

        List<Order> orderList = orderRepository.findAll();

        List<OrderDto> dtoList = orderList.stream()
                .map(order -> OrderMapper.INSTANCE.orderToOrderDto(order))
                .toList();

        // KAFKA part
        // fetch executor and customer data for each order.

        return dtoList;
    }

    @Override
    public OrderDto getOrderByIdAdmin(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            return OrderMapper.INSTANCE.orderToOrderDto(order);
        } else {
            throw new RuntimeException("Order with ID " + orderId + " not found");
        }
    }

}

