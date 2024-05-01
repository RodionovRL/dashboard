package ru.aston.controller.admincontroller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aston.dto.OrderDto;
import ru.aston.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing order-related HTTP requests for admin url.
 * This controller handles getting all orders and getting order by ID list.
 */
@RestController
@RequestMapping(path = "/admin/orders")
@AllArgsConstructor
public class AdminOrderController {

    /**
     * Logger for the {@link AdminOrderController} class.
     */
    private static final Logger log = LoggerFactory.getLogger(AdminOrderController.class);

    /**
     * The OrderService instance associated with this AdminOrderController.
     */
    private OrderService orderService;

    /**
     * Handles HTTP GET request to retrieve all orders for admin with all statuses.
     * @return List of OrderDto containing all orders.
     */
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> getAllOrders() {
        log.debug("Received admin request to retrieve all orders");
        return orderService.getAllOrdersAdmin();
    }

    /**
     * Handles HTTP GET request to retrieve an order by ID for admin with any status.
     * @param orderId ID of the order to retrieve.
     * @return OrderDto containing the order details.
     */
    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto getOrderById(@PathVariable Long orderId) {
        log.debug("Received admin request to retrieve order with id = " + orderId);
        return orderService.getOrderByIdAdmin(orderId);
    }
}