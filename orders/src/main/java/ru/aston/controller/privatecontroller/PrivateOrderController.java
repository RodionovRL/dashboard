package ru.aston.controller.privatecontroller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aston.dto.OrderDto;
import ru.aston.dto.NewOrderDto;
import ru.aston.service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing private order-related HTTP requests.
 * This controller handles creating, retrieving, updating and deleting orders for a specific user.
 */
@RestController
@RequestMapping(path = "/orders/{userId}/orders")
@AllArgsConstructor
public class PrivateOrderController {

    /**
     * Logger for the {@link PrivateOrderController} class.
     */
    private static final Logger log = LoggerFactory.getLogger(PrivateOrderController.class);

    /**
     * The OrderService instance associated with this PrivateOrderController.
     */
    private OrderService orderService;

    /**
     * Handles HTTP POST request to create a new order for the specified user.
     * @param userId The ID of the user who creates the order.
     * @param orderShortDto The DTO containing the details of the new order.
     * @return OrderDto containing the details of the created order.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto postOrder(@PathVariable Long userId,
                              @RequestBody @Valid NewOrderDto orderShortDto) {
        log.debug("Received user request to create order. User ID: {}, Order details: {}", userId, orderShortDto);
        return orderService.postOrder(userId, orderShortDto);
    }

    /**
     * Handles HTTP DELETE request to delete an order for the specified user.
     * @param userId The ID of the user whose order is to be deleted.
     * @param orderId The ID of the order to be deleted.
     */
    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long userId,
                            @PathVariable Long orderId){
        log.debug("Received user request to delete order. User ID: {}, Order ID: {}", userId, orderId);
        orderService.delete(userId, orderId);
    }

    /**
     * Handles HTTP GET request to retrieve all orders for the specified user.
     * @param userId The ID of the user who retrieves list of all orders.
     * @return List of OrderDto containing list of all orders for the specified user.
     */
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> getAllOrders(@PathVariable Long userId) {
        log.debug("Received user request to retrieve list of all orders. User ID: {}", userId);
        return orderService.getAllOrders(userId);
    }

    /**
     * Handles HTTP GET request to retrieve an order by ID for the specified user.
     * @param userId The ID of the user who retrieves order by ID.
     * @param orderId The ID of the order to be retrieved.
     * @return OrderDto containing the details of the specified order.
     */
    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto getOrderById(@PathVariable Long userId,
                                 @PathVariable Long orderId) {
        log.debug("Received user request to retrieve order by id. User ID: {}, Order ID: {}",
                userId, orderId);
        return orderService.getOrderById(userId, orderId);
    }

    /**
     * Handles HTTP PATCH request to update an order for the specified user.
     * @param userId The ID of the user who updates order.
     * @param orderId The ID of the order to be updated.
     * @param orderDto The DTO containing the updated details of the order.
     * @return OrderDto containing the updated details of the order.
     */
    @PatchMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto updateOrder(@PathVariable Long userId,
                                @PathVariable Long orderId,
                                @RequestBody OrderDto orderDto) {
        log.debug("Received user request to update order. User ID: {}, Order ID: {}, Order: {}",
                userId, orderId, orderDto);
        return orderService.updateOrder(userId, orderId, orderDto);
    }
}