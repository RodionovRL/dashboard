package ru.aston.account.controller.privatecontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.aston.account.dto.AccountDto;
import ru.aston.account.dto.NewAccountDto;
import ru.aston.account.dto.UpdateAccountDto;
import ru.aston.account.service.AccountService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/accounts")
@RequiredArgsConstructor
public class PrivateAccountController {
    private final AccountService service;
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public AccountDto addAccount(
//            @RequestBody @Validated NewAccountDto newAccountDto
//    ) {
//        log.info("addAccount, NewAccount={}", newAccountDto);
//
//        AccountDto resultAccountDto = service.addAccount(newAccountDto);
//
//        log.info("addAccount, resultAccountDto={}", resultAccountDto);
//        return resultAccountDto;
//    }

    @GetMapping("/byId")
    public AccountDto getAccountById(
            @RequestParam("accountId") Long accountId
    ) {
        log.info("getAccountById , accountId={}", accountId);

        AccountDto resultAccountDto = service.getAccountById(accountId);

        log.info("getAccountById, resultAccountDto={}", resultAccountDto);
        return resultAccountDto;
    }

    @GetMapping
    public AccountDto getAccountByUserId(
            @RequestParam("userId") Long userId
    ) {
        log.info("getAccountByUserId , userId={}", userId);

        AccountDto resultAccountDto = service.getAccountByUserId(userId);

        log.info("getAccountByUserId, resultAccountDto={}", resultAccountDto);

        return resultAccountDto;
    }

    @PatchMapping("/byId")
    public AccountDto updateAccountById(
            @RequestParam("accountId") Long accountId,
            @RequestBody @Valid UpdateAccountDto updateAccountDto
    ) {
        log.info("updateAccountById , accountId={}, updateAccountDto={}", accountId, updateAccountDto);

        AccountDto resultAccountDto = service.updateAccountById(accountId, updateAccountDto);

        log.info("updateAccountById, resultAccountDto={}", resultAccountDto);

        return resultAccountDto;
    }

    @PatchMapping
    public AccountDto updateAccountByUserId(
            @RequestParam("userId") Long userId,
            @RequestBody @Valid UpdateAccountDto updateAccountDto
    ) {
        log.info("updateAccountByUserId , userId={}, updateAccountDto={}", userId, updateAccountDto);

        AccountDto resultAccountDto = service.updateAccountByUserId(userId, updateAccountDto);

        log.info("updateAccountByUserId, resultAccountDto={}", resultAccountDto);

        return resultAccountDto;
    }
}
