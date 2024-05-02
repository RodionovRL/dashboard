package ru.aston.payment.exceptions;

public class NotEnoughFundsException extends RuntimeException {
    public NotEnoughFundsException(String message) {
        super(message);
    }
}
