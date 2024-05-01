package ru.aston.account.controller.admincontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.aston.account.dto.AccountDto;
import ru.aston.account.dto.NewAccountDto;
import ru.aston.account.mapper.AccountMapper;
import ru.aston.account.model.Account;
import ru.aston.account.service.AccountService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/accounts")
@RequiredArgsConstructor
public class AdminAccountController {
    private final AccountService service;
    private final AccountMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto addAccount(
            @RequestBody @Validated NewAccountDto newAccountDto
    ) {
        log.info("addAccount, NewAccount={}", newAccountDto);
        Account newAccount = mapper.toAccount(newAccountDto);
        Account resultAccount = service.addAccount(newAccount);
        AccountDto resultAccountDto = mapper.toAccountDto(resultAccount);
        log.info("addAccount, resultAccountDto={}", resultAccountDto);
        return resultAccountDto;
    }

    @GetMapping()
    public List<AccountDto> getAllAccounts() {
        log.info("getAllAccounts");

        List<AccountDto> resultAccountsDto = service.getAllAccounts();

        log.info("getAllAccounts, return {} accounts", resultAccountsDto.size());
        return resultAccountsDto;
    }


    @DeleteMapping("/byId")
    public HttpStatus deleteAccountById(
            @RequestParam("accountId") Long accountId
    ) {
        log.info("deleteAccountById , accountId={}", accountId);

        int result = service.deleteAccountById(accountId);

        log.info("deleteAccountById, result={}", result);
        if (result > 0) {
            return HttpStatus.NO_CONTENT;
        }

        return HttpStatus.BAD_REQUEST;
    }

    @DeleteMapping
    public HttpStatus deleteAccountByUserId(
            @RequestParam("userId") Long userId
    ) {
        log.info("deleteAccountByUserId , userId={}", userId);

        int result = service.deleteAccountByUserId(userId);

        log.info("deleteAccountByUserId, result={}", result);
        if (result > 0) {
            return HttpStatus.NO_CONTENT;
        }

        return HttpStatus.BAD_REQUEST;
    }
}
