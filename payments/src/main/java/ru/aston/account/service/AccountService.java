package ru.aston.account.service;

import ru.aston.account.dto.AccountDto;
import ru.aston.account.dto.NewAccountDto;
import ru.aston.account.dto.UpdateAccountDto;
import ru.aston.account.model.Account;

import javax.validation.Valid;
import java.util.List;

public interface AccountService {
    Account addAccount(Account newAccountDto);

    AccountDto getAccountById(Long accountId);

    AccountDto getAccountByUserId(Long userId);

    AccountDto updateAccountById(Long accountId, @Valid UpdateAccountDto updateAccountDto);

    AccountDto updateAccountByUserId(Long userId, UpdateAccountDto updateAccountDto);

    List<AccountDto> getAllAccounts();

    int deleteAccountById(Long accountId);

    int deleteAccountByUserId(Long userId);
}
