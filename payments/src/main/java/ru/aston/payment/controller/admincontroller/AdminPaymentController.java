package ru.aston.payment.controller.admincontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.aston.payment.PaymentMapper;
import ru.aston.payment.dto.PaymentDto;
import ru.aston.payment.service.PaymentService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/payments")
@RequiredArgsConstructor
public class AdminPaymentController {
    private final PaymentService service;
    private final PaymentMapper mapper;

    @GetMapping()
    public List<PaymentDto> getAllPayments() {
        log.info("getAllPayments");

        List<PaymentDto> resultPaymentsDto = service.getAllPayments();

        log.info("getAllPayments, return {} payments", resultPaymentsDto.size());
        return resultPaymentsDto;
    }


    @DeleteMapping("/byId")
    public HttpStatus deletePaymentById(
            @RequestParam("paymentId") Long paymentId
    ) {
        log.info("deletePaymentById , paymentId={}", paymentId);

        int result = service.deletePaymentById(paymentId);

        log.info("deletePaymentById, result={}", result);
        if (result > 0) {
            return HttpStatus.NO_CONTENT;
        }

        return HttpStatus.BAD_REQUEST;
    }
}
