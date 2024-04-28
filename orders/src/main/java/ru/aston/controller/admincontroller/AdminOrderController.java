package ru.aston.controller.admincontroller;

import ru.aston.dto.OrderDto;
import ru.aston.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/orders")
@AllArgsConstructor
public class AdminOrderController {
    private OrderService orderService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> getAllOrders() {
        return orderService.getAllOrdersAdmin();
    }

    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderByIdAdmin(orderId);
    }
}