package ru.aston.dto;

import ru.aston.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private String name;
    private String description;
    private UserDto customer;
    private UserDto executor;
    private PaymentDto payment;
    private OrderStatus status;
    private LocalDateTime date;
}
