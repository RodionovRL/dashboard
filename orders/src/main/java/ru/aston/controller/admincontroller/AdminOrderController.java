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

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long userId,
                            @PathVariable Long orderId){
        orderService.delete(userId, orderId);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> getAllOrders() {
        return orderService.getAllOrdersAdmin();
    }

    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto getOrderById(@PathVariable Long userId,
                                 @PathVariable Long orderId) {
        return orderService.getOrderById(userId, orderId);
    }
}