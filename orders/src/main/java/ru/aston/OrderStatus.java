package ru.aston;

/**
 * Enumeration representing the status of an order.
 * This enumeration defines different states an order can be in.
 */
public enum OrderStatus {

    /**
     * Indicates that the order is just created.
     */
    NEW,

    /**
     * Indicates that the order is currently being processed.
     */
    IN_PROCESS,

    /**
     * Indicates that the ordered task has been completed.
     */
    DONE,

    /**
     * Indicates that the order has been paid.
     */
    PAID
}