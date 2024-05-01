package ru.aston.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.aston.payment.model.Payment;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    int deletePaymentById(Long paymentId);

    Optional<Payment> findByUserId(Long paymentId);
}
