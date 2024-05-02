package ru.aston.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aston.OrderStatus;
import ru.aston.dto.*;
import ru.aston.kafka.consumers.user.PaymentRequestConsumer;
import ru.aston.kafka.consumers.user.UserGetConsumer;
import ru.aston.kafka.consumers.user.UsersListGetConsumer;
import ru.aston.kafka.producers.user.PaymentRequestProducer;
import ru.aston.kafka.producers.user.UserGetProducer;
import ru.aston.kafka.producers.user.UsersListGetProducer;
import ru.aston.mapper.OrderMapper;
import ru.aston.model.Order;
import ru.aston.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of OrderService interface.
 */
@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    /**
     * Logger for the {@link OrderServiceImpl} class.
     */
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private OrderRepository orderRepository;
    private UserGetProducer userGetProducer;
    private UserGetConsumer userGetConsumer;
    private UsersListGetProducer usersListGetProducer;
    private UsersListGetConsumer usersListGetConsumer;
    private PaymentRequestProducer paymentRequestProducer;
    private PaymentRequestConsumer paymentRequestConsumer;

    /**
     * {@inheritDoc}
     * Any user can post new order.
     */
    @Override
    public OrderDto postOrder(Long userId, NewOrderDto order) {
        Order newOrder = OrderMapper.INSTANCE.newOrderDtoToOrder(order);
        newOrder.setCustomerId(userId);
        Order createdOrder = orderRepository.save(newOrder);
        OrderDto orderDto = setCustomerAndExecutorToOrder(createdOrder);
        log.debug("Created new order: {}", orderDto);
        return orderDto;
    }

    /**
     * {@inheritDoc}
     * User can delete order just if he is customer and order has NEW status.
     */
    @Override
    public void delete(Long userId, Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            if (order.getCustomerId().equals(userId) && order.getStatus().equals(OrderStatus.NEW)) {
                orderRepository.delete(order);
                log.debug("Deleted order: {}", orderId);
            } else {
                log.error("You are not authorized to delete this order. " +
                                "User ID {}. Order ID {}. Order status {}", userId, orderId, order.getStatus());
                throw new RuntimeException("You are not authorized to delete this order.");
            }
        } else {
            log.error("Order not found with ID:  {}", orderId);
            throw new RuntimeException("Order not found with ID: " + orderId);
        }
    }

    /**
     * {@inheritDoc}
     * Any user can retrieve order assigned with him with any status and any NEW order.
     */
    @Override
    public OrderDto getOrderById(Long userId, Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            if (order.getStatus() == OrderStatus.NEW || order.getCustomerId().equals(userId)
                    || order.getExecutorId().equals(userId)) {
                OrderDto orderDto = setCustomerAndExecutorToOrder(order);

                // ADD HERE KAFKA WORKS with payment

                log.debug("User with ID: {} retrieved order by ID: {} - {}", userId, orderId, orderDto);
                return orderDto;
            } else {
                log.error("User with ID: {} does not have access to order with ID: {}", userId, orderId);
                throw new RuntimeException("User does not have access to this order");
            }
        } else {
            log.error("Order with ID: {} not found", orderId);
            throw new RuntimeException("Order with ID " + orderId + " not found");
        }
    }

    /**
     * {@inheritDoc}
     * Any user can get all orders assigned with him with any status and all NEW orders.
     */
    @Override
    public List<OrderDto> getAllOrders(Long userId) {
        List<Order> ordersList = orderRepository.findByStatusOrCustomerIdOrExecutorId(
                OrderStatus.NEW, userId, userId);
        List<OrderDto> orderDtoList = setCustomersAndExecutorsToOrdersList(ordersList);

        // KAFKA part
        // fetch PAYMENTS.

        log.debug("User with ID: {} retrieved list of orders", userId);
        return orderDtoList;
    }

    /**
     * {@inheritDoc}
     * If the user is a customer:
     *   - Can update the name and description of the order just if status is NEW.
     *   - Can update the status from DONE to PAID by sending a payment request to the Payments service.
     * If the user is an executor:
     *   - Can update the status from NEW to IN_PROCESS when taking the order for work.
     *   - Can update the status from IN_PROCESS to DONE when completing the task in the order.
     */
    @Override
    public OrderDto updateOrder(Long userId, Long orderId, OrderDto orderDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order = updateOrderFields(order, userId, orderDto);

        // Kafka operations with PAYMENT
        Order updatedOrder = orderRepository.save(order);
        log.debug("User with ID: {} updated order with ID: {} - {}", userId, orderId, updatedOrder);
        OrderDto updatedOrderDto = setCustomerAndExecutorToOrder(updatedOrder);
        updatedOrderDto.setPayment(orderDto.getPayment());
        return updatedOrderDto;
    }

    /**
     * {@inheritDoc}
     * Admin can retrieve all orders with any status.
     */
    @Override
    public List<OrderDto> getAllOrdersAdmin() {
        List<Order> ordersList = orderRepository.findAll();
        List<OrderDto> dtoList = setCustomersAndExecutorsToOrdersList(ordersList);

        // KAFKA part
        // fetch PAYMENT data for each order.

        log.debug("Admin retrieved list of all orders: {}", dtoList);
        return dtoList;
    }

    /**
     * {@inheritDoc}
     * Admin can retrieve order with any status.
     */
    @Override
    public OrderDto getOrderByIdAdmin(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            OrderDto orderDto = setCustomerAndExecutorToOrder(order);
            log.debug("Admin retrieved order with ID: {} - {}", orderId, orderDto);
            return orderDto;
        } else {
            log.error("Order with ID: {} not found", orderId);
            throw new RuntimeException("Order with ID " + orderId + " not found");
        }
    }

    /**
     * {@inheritDoc}
     * Private method to update fields of orders
     */
    private Order updateOrderFields(Order order, Long userId, OrderDto orderDto) {
        if(!(userId.equals(order.getCustomerId()) || userId.equals(order.getExecutorId()))) {
            if(order.getStatus() == OrderStatus.NEW
                    && orderDto.getStatus() == OrderStatus.IN_PROCESS) {
                order.setStatus(OrderStatus.IN_PROCESS);
                order.setExecutorId(userId);
                log.debug("User with ID: {} took order with ID: {} for execution. Order: {}",
                        userId, order.getId(), order);
            } else {
                log.error("User with ID: {} is not authorized to took order with ID: {} for execution.",
                        userId, order.getId());
                throw new RuntimeException("User is not authorized to update the order");
            }
        } else if(userId.equals(order.getCustomerId())) {
            updateCustomersFields(order, orderDto);
        } else if (userId.equals(order.getExecutorId())) {
            if (order.getStatus() == OrderStatus.IN_PROCESS
                    && orderDto.getStatus() == OrderStatus.DONE) {
                order.setStatus(OrderStatus.DONE);
                log.debug("User with ID: {} finished execution of order with ID: {}. Order: {}",
                        userId, order.getId(), order);
            } else {
                log.error("Executor with ID: {} is not authorized to update status of the order with ID: {} to {}",
                        userId, order.getId(), orderDto.getStatus());
                throw new RuntimeException("Executor is not authorized to update the orders status");
            }
        }
        return order;
    }

    /**
     * {@inheritDoc}
     * Private method for updating order fields for customers.
     */
    private void updateCustomersFields(Order order, OrderDto orderDto) {
        if (order.getStatus() == OrderStatus.NEW) {
            order.setName(orderDto.getName());
            order.setDescription(orderDto.getDescription());
            log.debug("Customer with ID: {} updated fields of order with ID: {} to order name: {} " +
                    "and order description: {}", order.getCustomerId(), order.getId(), order.getName(),
                    order.getDescription());
        } else if(order.getStatus() == OrderStatus.DONE
                && orderDto.getStatus() == OrderStatus.PAID) {

            paymentRequestProducer.sendMessage(new PaymentRequestDto(order.getId(),
                    order.getCustomerId(),
                    order.getExecutorId(), orderDto.getSum()));
            orderDto.setPayment(fetchPayment());

            order.setStatus(OrderStatus.PAID);
            log.debug("Customer with ID: {} set order with ID: {} status as: {}",
                    order.getCustomerId(), order.getId(), order.getStatus());
        } else {
            log.error("Customer with ID: {} is not authorized to update status of the order with ID: {} to: {}",
                    order.getCustomerId(), order.getId(), orderDto.getStatus());
            throw new RuntimeException("Customer is not authorized to update the order");
        }
    }

    /**
     * {@inheritDoc}
     * Private method for fetching user data from Users service using Kafka.
     */
    private UserDto fetchUser(Long userId) {
        userGetProducer.sendMessage(userId);
        try {
            Thread.sleep(2222);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Thread interrupted while waiting for response from Kafka");
            throw new RuntimeException("Thread interrupted while waiting for response from Kafka");
        }
        UserDto userDto = userGetConsumer.getReceivedUserDto();
        return userDto;
    }

    /**
     * {@inheritDoc}
     * Private method for setting customer and executor fields for order.
     */
    private OrderDto setCustomerAndExecutorToOrder(Order order) {
        OrderDto orderDto = OrderMapper.INSTANCE.orderToOrderDto(order);
        if (order.getCustomerId() != null) {
            Optional<UserDto> customer = Optional.ofNullable(fetchUser(order.getCustomerId()));
            if (customer.isPresent()) {
                orderDto.setCustomer(customer.get());
            }
            log.debug("Retrieved user data with ID: {} set as customer field to order with ID: {}",
                    customer.get().getId(), order.getId());
        }
        if (order.getExecutorId() != null) {
            Optional<UserDto> executor = Optional.ofNullable(fetchUser(order.getExecutorId()));
            if (executor.isPresent()) {
                orderDto.setExecutor(executor.get());
            }
            log.debug("Retrieved user data with ID: {} set as executor field to order with ID: {}",
                    executor.get().getId(), order.getId());
        }
        return orderDto;
    }

    /**
     * {@inheritDoc}
     * Private method for fetching users list data from Users service using Kafka.
     */
    private List<UserDto> fetchUsersList(List<Long> usersIds) {
        usersListGetProducer.sendMessage(usersIds);
        try {
            Thread.sleep(2222);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Thread interrupted while waiting for response from Kafka");
            throw new RuntimeException("Thread interrupted while waiting for response from Kafka");
        }
        List<UserDto> usersList = usersListGetConsumer.getReceivedUserDtoList();
        return usersList;
    }

    /**
     * {@inheritDoc}
     * Private method for setting customers and executors fields for orders list.
     */
    private List<OrderDto> setCustomersAndExecutorsToOrdersList(List<Order> ordersList) {
        Map<Long, OrderDto> orderDtoMap = ordersList.stream()
                .map(OrderMapper.INSTANCE::orderToOrderDto)
                .collect(Collectors.toMap(OrderDto::getId, orderDto -> orderDto));

        List<Long> usersIds = ordersList.stream()
                .flatMap(order -> Stream.of(order.getCustomerId(), order.getExecutorId()))
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, UserDto> usersMap = fetchUsersList(usersIds)
                .stream()
                .collect(Collectors.toMap(UserDto::getId, userDto -> userDto));

        for(Order order: ordersList) {
            if (order.getExecutorId() != null) {
                Long executorId = order.getExecutorId();
                orderDtoMap.get(order.getId()).setExecutor(usersMap.get(executorId));
            }
            if (order.getCustomerId() != null) {
                Long customerId = order.getCustomerId();
                orderDtoMap.get(order.getId()).setCustomer(usersMap.get(customerId));
            }
        }
        log.debug("Retrieved users data are set to orders customer and executor fields. Orders list: {}",
                orderDtoMap.values());
        return List.copyOf(orderDtoMap.values());
    }

    private PaymentDto fetchPayment() {
        try {
            Thread.sleep(2222);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Thread interrupted while waiting for response from Kafka");
            throw new RuntimeException("Thread interrupted while waiting for response from Kafka");
        }
        PaymentDto paymentDto = paymentRequestConsumer.getReceivedPaymentDto();
        return paymentDto;
    }
}