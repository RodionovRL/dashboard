package ru.aston.service;

import ru.aston.OrderStatus;
import ru.aston.dto.UserDto;
import ru.aston.kafka.consumers.user.UserGetConsumer;
import ru.aston.kafka.consumers.user.UsersListGetConsumer;
import ru.aston.kafka.producers.user.UserGetProducer;
import ru.aston.kafka.producers.user.UsersListGetProducer;
import ru.aston.mapper.OrderMapper;
import ru.aston.model.Order;
import ru.aston.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.aston.dto.NewOrderDto;
import ru.aston.dto.OrderDto;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private UserGetProducer userGetProducer;
    private UserGetConsumer userGetConsumer;
    private UsersListGetProducer usersListGetProducer;
    private UsersListGetConsumer usersListGetConsumer;

    @Override
    public OrderDto postOrder(Long userId, NewOrderDto order) {

        Order newOrder = OrderMapper.INSTANCE.newOrderDtoToOrder(order);
        newOrder.setCustomerId(userId);

        Order createdOrder = orderRepository.save(newOrder);
        OrderDto orderDto = setCustomerAndExecutorToOrder(createdOrder);

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
            if (order.getStatus() == OrderStatus.NEW || order.getCustomerId().equals(userId)
                    || order.getExecutorId().equals(userId)) {

                OrderDto orderDto = setCustomerAndExecutorToOrder(order);


                // ADD HERE KAFKA WORKS with payment


                return orderDto;
            } else {
                throw new RuntimeException("User does not have access to this order");
            }
        } else {
            throw new RuntimeException("Order with ID " + orderId + " not found");
        }
    }

    @Override
    public List<OrderDto> getAllOrders(Long userId) {
        List<Order> ordersList = orderRepository.findByStatusOrCustomerIdOrExecutorId(
                OrderStatus.NEW, userId, userId);

        List<OrderDto> orderDtoList = setCustomersAndExecutorsToOrdersList(ordersList);

        // KAFKA part
        // fetch executor and customer data for each order.

        return orderDtoList;
    }

    @Override
    public OrderDto updateOrder(Long userId, Long orderId, OrderDto orderDto) {
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
        List<Order> ordersList = orderRepository.findAll();

        List<OrderDto> dtoList = setCustomersAndExecutorsToOrdersList(ordersList);

        // KAFKA part
        // fetch executor and customer data for each order.

        return dtoList;
    }

    @Override
    public OrderDto getOrderByIdAdmin(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            OrderDto orderDto = OrderMapper.INSTANCE.orderToOrderDto(order);
            return orderDto;
        } else {
            throw new RuntimeException("Order with ID " + orderId + " not found");
        }
    }

    private UserDto fetchUser(Long userId) {
        userGetProducer.sendMessage(userId);
        try {
            Thread.sleep(2222);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while waiting for response from Kafka");
        }
        UserDto userDto = userGetConsumer.getReceivedUserDto();
        return userDto;
    }

    private OrderDto setCustomerAndExecutorToOrder(Order order) {
        OrderDto orderDto = OrderMapper.INSTANCE.orderToOrderDto(order);
        if (order.getCustomerId() != null) {
            Optional<UserDto> customer = Optional.ofNullable(fetchUser(order.getCustomerId()));
            if (customer.isPresent()) {
                orderDto.setCustomer(customer.get());
            }
        } else if (order.getExecutorId() != null) {
            Optional<UserDto> executor = Optional.ofNullable(fetchUser(order.getCustomerId()));
            if (executor.isPresent()) {
                orderDto.setExecutor(executor.get());
            }
        }
        return orderDto;
    }

    private List<UserDto> fetchUsersList(List<Long> usersIds) {
        usersListGetProducer.sendMessage(usersIds);

        try {
            Thread.sleep(2222);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while waiting for response from Kafka");
        }
        List<UserDto> usersList = usersListGetConsumer.getReceivedUserDtoList();
        return usersList;
    }

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

        return List.copyOf(orderDtoMap.values());
    }
}