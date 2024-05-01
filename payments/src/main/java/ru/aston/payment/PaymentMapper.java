package ru.aston.payment;

import ru.aston.payment.dto.NewPaymentDto;
import ru.aston.payment.dto.PaymentDto;
import ru.aston.payment.model.Payment;

import java.util.List;

public interface PaymentMapper {
    Payment toPayment (NewPaymentDto newPaymentDto);
    PaymentDto toPaymentDto (Payment payment);

    List<PaymentDto> toPaymentDtoList(List<Payment> payments);
}
