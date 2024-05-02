package ru.aston.account.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.aston.account.dto.UpdateAccountDto;
import ru.aston.account.dto.UserDto;
import ru.aston.payment.kafka.consumers.UserGetConsumer;
import ru.aston.payment.kafka.consumers.UsersListGetConsumer;
import ru.aston.payment.kafka.produsers.UserGetProducer;
import ru.aston.payment.kafka.produsers.UsersListGetProducer;
import ru.aston.account.mapper.AccountMapper;
import ru.aston.account.dto.AccountDto;
import ru.aston.account.model.Account;
import ru.aston.account.repository.AccountRepository;
import ru.aston.exception.NotFoundException;

import java.util.List;
import java.util.Map;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository repository;
    private final AccountMapper mapper;
    private final UserGetProducer userGetProducer;
    private final UserGetConsumer userGetConsumer;
    private UsersListGetProducer usersListGetProducer;
    private UsersListGetConsumer usersListGetConsumer;

    @Override
    public Account addAccount(Account newAccount) {
        fetchUser(newAccount.getUserId());

        log.info("{}, addAccount, newAccount={}", this.getClass(), newAccount);

        Account savedAccount = repository.save(newAccount);

        log.info("{}, addAccount, savedAccount={}", this.getClass(), savedAccount);
        return savedAccount;
    }

    @Override
    public AccountDto getAccountById(Long accountId) {
        log.info("{}, getAccountById={}", this.getClass(), accountId);
        Account account = findAccountById(accountId);
        UserDto userDto = fetchUser(account.getUserId());
        AccountDto accountDto = mapper.toAccountDto(account);
        accountDto.setUserDto(userDto);
        log.info("{}, getAccountById={}", this.getClass(), accountDto);
        return accountDto;
    }

    @Override
    public AccountDto getAccountByUserId(Long userId) {
        log.info("{}, getAccountByUserId={}", this.getClass(), userId);
        Account account = findAccountByUserId(userId);
        UserDto userDto = fetchUser(account.getUserId());
        AccountDto accountDto = mapper.toAccountDto(account);
        accountDto.setUserDto(userDto);
        log.info("{}, getAccountByUserId={}", this.getClass(), accountDto);
        return accountDto;
    }

    @Override
    public AccountDto updateAccountById(Long accountId, UpdateAccountDto updateAccountDto) {
        log.info("{}, updateAccountById={}, updateAccountDto={}", this.getClass(), accountId, updateAccountDto);
        Account account = findAccountById(accountId);
        if (updateAccountDto.getAmount() != null) {
            account.setAmount(updateAccountDto.getAmount());
        }
        UserDto userDto = fetchUser(account.getUserId());
        AccountDto accountDto = mapper.toAccountDto(account);
        accountDto.setUserDto(userDto);
        log.info("{}, updateAccountById={}", this.getClass(), accountDto);
        return accountDto;
    }

    @Override
    public AccountDto updateAccountByUserId(Long userId, UpdateAccountDto updateAccountDto) {
        log.info("{}, updateAccountByUserId={}, updateAccountDto={}", this.getClass(), userId, updateAccountDto);
        Account account = findAccountByUserId(userId);
        if (updateAccountDto.getAmount() != null) {
            account.setAmount(updateAccountDto.getAmount());
        }
        UserDto userDto = fetchUser(account.getUserId());
        AccountDto accountDto = mapper.toAccountDto(account);
        accountDto.setUserDto(userDto);
        log.info("{}, getAccountByUserId={}", this.getClass(), accountDto);
        return accountDto;
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> allAccounts = repository.findAll();
        List<Long> allAccountsUsersId = allAccounts.stream().map(Account::getUserId).toList();
        List<UserDto> usersDto = fetchUsersList(allAccountsUsersId);
        Map<Long, UserDto> userDtoMap = usersDto.stream().collect(toMap(UserDto::getId, userDto -> userDto));
        return allAccounts.stream()
                .map(account -> {
                    AccountDto accountDto = mapper.toAccountDto(account);
                    accountDto.setUserDto(userDtoMap.get(account.getUserId()));
                    return accountDto;
                })
                .toList();
    }

    @Override
    public int deleteAccountById(Long accountId) {
        findAccountById(accountId);
        int numDeleted = repository.deleteAccountById(accountId);
        log.info("deleted {} accounts by id={}", numDeleted, accountId);
        return numDeleted;
    }

    @Override
    public int deleteAccountByUserId(Long userId) {
        findAccountByUserId(userId);
        int numDeleted = repository.deleteAccountByUserId(userId);
        log.info("deleted {} accounts by userId={}", numDeleted, userId);
        return numDeleted;
    }

    private Account findAccountById(Long accountId) {
        return repository.findById(accountId)
                .orElseThrow(() -> new NotFoundException(String.format("Account with id=%d not found", accountId)));
    }

    private Account findAccountByUserId(Long accountId) {
        return repository.findByUserId(accountId)
                .orElseThrow(() -> new NotFoundException(String.format("Account with id=%d not found", accountId)));
    }

    private UserDto fetchUser(Long userId) {
        userGetProducer.sendMessage(userId);
        try {
            Thread.sleep(2222);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Thread interrupted while waiting for response from Kafka");
            throw new RuntimeException("Thread interrupted while waiting for response from Kafka");
        }
        return userGetConsumer.getReceivedUserDto();
    }

    private List<UserDto> fetchUsersList(List<Long> usersIds) {
        usersListGetProducer.sendMessage(usersIds);
        try {
            Thread.sleep(2222);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Thread interrupted while waiting for response from Kafka");
            throw new RuntimeException("Thread interrupted while waiting for response from Kafka");
        }
        return usersListGetConsumer.getReceivedUserDtoList();
    }
}
