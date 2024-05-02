package ru.aston.account.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.aston.account.dto.AccountDto;
import ru.aston.account.dto.NewAccountDto;
import ru.aston.account.model.Account;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {
    Account toAccount (NewAccountDto newAccountDto);
    AccountDto toAccountDto(Account account);
    List<AccountDto> AccountsDto(List<Account> accounts);
}
