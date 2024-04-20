package com.aston.model;

import com.aston.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "order_name")
    private String name;

    @Column(name = "order_description")
    private String description;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "executor_id")
    private Long executorId;

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "order_date")
    private LocalDateTime date;
}
