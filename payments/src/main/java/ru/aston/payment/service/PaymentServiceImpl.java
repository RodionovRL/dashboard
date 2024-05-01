package ru.aston.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aston.account.model.Account;
import ru.aston.account.repository.AccountRepository;
import ru.aston.exception.NotFoundException;
import ru.aston.payment.PaymentMapper;
import ru.aston.payment.PaymentRepository;
import ru.aston.payment.dto.NewPaymentDto;
import ru.aston.payment.dto.PaymentDto;
import ru.aston.payment.exceptions.NotEnoughFundsException;
import ru.aston.payment.model.Payment;
import ru.aston.payment.util.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;
    private final PaymentMapper paymentMapper;

    @Transactional
    @Override
    public PaymentDto executePayment(NewPaymentDto newPaymentDto) {
        Payment payment = paymentMapper.toPayment(newPaymentDto);
        Account customerAccount = findAccountById(payment.getCustomerId());
        Account executorAccount = findAccountById(payment.getExecutorId());
        BigDecimal startCustomerAccount = customerAccount.getAmount();
        BigDecimal startExecutorAccount = executorAccount.getAmount();

        payment.setPaymentStatus(PaymentStatus.II_PROGRESS);
        payment.setDateTime(LocalDateTime.now());

        try {
            BigDecimal payResult = customerAccount.getAmount().subtract(payment.getSum());
            if (payResult.compareTo(BigDecimal.ZERO) < 0) {
                throw new NotEnoughFundsException(String.format("Not Enough Funds in account=%s", customerAccount));
            }
            customerAccount.setAmount(startCustomerAccount.subtract(payment.getSum()));
            executorAccount.setAmount(startExecutorAccount.add(payment.getSum()));

            accountRepository.save(customerAccount);
            accountRepository.save(executorAccount);

            payment.setPaymentStatus(PaymentStatus.DONE);

        } catch (RuntimeException e) {
            log.error("error execute Payment");
            payment.setPaymentStatus(PaymentStatus.REJECTED);
        }

        Payment savedPayment = paymentRepository.save(payment);


        return paymentMapper.toPaymentDto(savedPayment);
    }

    @Override
    public PaymentDto getPaymentByUserId(Long userId) {
        Payment payment = findPaymentByUserId(userId);
        log.info("getPaymentByUserId={}", userId);
        return paymentMapper.toPaymentDto(payment);
    }

    @Override
    public PaymentDto getPaymentById(Long paymentId) {
        Payment payment = findPaymentById(paymentId);
        log.info("getPaymentById={}", paymentId);
        return paymentMapper.toPaymentDto(payment);
    }

    @Override
    public List<PaymentDto> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return paymentMapper.toPaymentDtoList(payments);
    }

    @Override
    public int deletePaymentById(Long paymentId) {
        findPaymentById(paymentId);
        int numDeleted = paymentRepository.deletePaymentById(paymentId);
        log.info("deleted {} payments by id={}", numDeleted, paymentId);
        return numDeleted;
    }

    private Payment findPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException(String.format("Payment with id=%d not found", paymentId)));
    }

    private Payment findPaymentByUserId(Long userId) {
        return paymentRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Payment with userId=%d not found", userId)));
    }

    private Account findAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException(String.format("Account with id=%d not found", accountId)));
    }
}
