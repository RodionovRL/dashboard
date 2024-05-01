package ru.aston.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.aston.account.model.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUserId(Long accountId);

    int deleteAccountById(Long accountId);

    int deleteAccountByUserId(Long userId);
}
