package ru.aston.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class PaymentRequestDto {
    private Long orderId;
    private Long customerId;
    private Long executorId;
    private BigDecimal sum;
}
