package ru.aston.model;

import lombok.AllArgsConstructor;
import ru.aston.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entity class representing an order.
 * This class is mapped to the "orders" table in the database.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    /**
     * The unique identifier of the order.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    /**
     * The name of the order.
     */
    @Column(name = "order_name")
    private String name;

    /**
     * The description of the order.
     */
    @Column(name = "order_description")
    private String description;

    /**
     * The ID of the customer who placed the order.
     */
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    /**
     * The ID of the executor assigned to the order.
     */
    @Column(name = "executor_id")
    private Long executorId;

    /**
     * The ID of the payment associated with the order.
     */
    @Column(name = "payment_id")
    private Long paymentId;

    /**
     * The status of the order.
     */
    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    /**
     * The date and time when the order was created.
     */
    @Column(name = "order_date")
    private LocalDateTime date;
}