package ru.aston.payment.controller.privatecontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.aston.payment.dto.PaymentDto;
import ru.aston.payment.service.PaymentService;

@Slf4j
@RestController
@RequestMapping(path = "/payments")
@RequiredArgsConstructor
public class PrivatePaymentController {
    private final PaymentService service;
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public PaymentDto addPayment(
//            @RequestBody @Validated NewPaymentDto newPaymentDto
//    ) {
//        log.info("addPayment, NewPayment={}", newPaymentDto);
//
//        PaymentDto resultPaymentDto = service.addPayment(newPaymentDto);
//
//        log.info("addPayment, resultPaymentDto={}", resultPaymentDto);
//        return resultPaymentDto;
//    }

    @GetMapping("/byId")
    public PaymentDto getPaymentById(
            @RequestParam("paymentId") Long paymentId
    ) {
        log.info("getPaymentById , paymentId={}", paymentId);

        PaymentDto resultPaymentDto = service.getPaymentById(paymentId);

        log.info("getPaymentById, resultPaymentDto={}", resultPaymentDto);
        return resultPaymentDto;
    }

    @GetMapping
    public PaymentDto getPaymentByCustomerId(
            @RequestParam("customerId") Long customerId
    ) {
        log.info("getPaymentByCustomerId , userId={}", customerId);

        PaymentDto resultPaymentDto = service.getPaymentByUserId(customerId);

        log.info("getPaymentByCustomerId, resultPaymentDto={}", resultPaymentDto);

        return resultPaymentDto;
    }
}
