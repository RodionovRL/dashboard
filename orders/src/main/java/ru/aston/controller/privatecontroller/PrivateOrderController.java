package ru.aston.controller.privatecontroller;

import ru.aston.dto.OrderDto;
import ru.aston.dto.NewOrderDto;
import ru.aston.service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/orders")
@AllArgsConstructor
public class PrivateOrderController {
    private OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto postOrder(@PathVariable Long userId,
                              @RequestBody @Valid NewOrderDto orderShortDto) {
        return orderService.postOrder(userId, orderShortDto);
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long userId,
                            @PathVariable Long orderId){
        orderService.delete(userId, orderId);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> getAllOrders(@PathVariable Long userId) {
        return orderService.getAllOrders(userId);
    }

    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto getOrderById(@PathVariable Long userId,
                                 @PathVariable Long orderId) {
        return orderService.getOrderById(userId, orderId);
    }

    @PatchMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto updateOrder(@PathVariable Long userId,
                                @PathVariable Long orderId,
                                @RequestBody OrderDto orderDto) {
        return orderService.updateOrder(userId, orderId, orderDto);
    }
}