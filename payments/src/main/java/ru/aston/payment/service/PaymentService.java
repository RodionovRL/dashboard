package ru.aston.payment.service;

import ru.aston.payment.dto.NewPaymentDto;
import ru.aston.payment.dto.PaymentDto;

import java.util.List;

public interface PaymentService {
    PaymentDto executePayment(NewPaymentDto newPaymentDto);

    PaymentDto getPaymentByUserId(Long userId);

    PaymentDto getPaymentById(Long paymentId);

    List<PaymentDto> getAllPayments();

    int deletePaymentById(Long paymentId);
}
