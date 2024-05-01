package ru.aston.payment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.aston.payment.util.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    private Long customerId;
    private Long executorId;
    private BigDecimal sum;
    private PaymentStatus paymentStatus;
    private LocalDateTime dateTime;
}
