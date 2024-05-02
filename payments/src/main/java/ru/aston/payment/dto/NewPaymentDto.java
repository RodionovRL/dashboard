package ru.aston.payment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class NewPaymentDto {
    private Long orderId;
    private Long customerId;
    private Long executorId;
    private BigDecimal sum;
}
