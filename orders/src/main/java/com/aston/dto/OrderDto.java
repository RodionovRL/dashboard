package com.aston.dto;

import com.aston.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
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
