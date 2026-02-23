package com.bank.core.application.usecase;

import com.bank.core.application.port.input.CreateAccountUseCase;
import com.bank.core.application.port.output.AccountNumberGenerator;
import com.bank.core.application.port.output.AccountRepository;
import com.bank.core.domain.model.Account;
import com.bank.core.domain.model.AccountStatus;
import com.bank.core.domain.model.Money;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Caso de uso: crear cuenta bancaria.
 */
public class CreateAccountService implements CreateAccountUseCase {

    private final AccountRepository accountRepository;
    private final AccountNumberGenerator accountNumberGenerator;

    public CreateAccountService(AccountRepository accountRepository,
                               AccountNumberGenerator accountNumberGenerator) {
        this.accountRepository = accountRepository;
        this.accountNumberGenerator = accountNumberGenerator;
    }

    @Override
    public Account create(UUID ownerId, BigDecimal initialBalance, String currency) {
        UUID accountId = UUID.randomUUID();
        String accountNumber = accountNumberGenerator.generate();
        Money balance = new Money(initialBalance, currency);
        Account account = new Account(accountId, accountNumber, balance, ownerId, AccountStatus.ACTIVE);
        return accountRepository.save(account);
    }
}
