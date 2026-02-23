package com.bank.core.application.usecase;

import com.bank.core.application.port.input.GetBalanceUseCase;
import com.bank.core.application.port.output.AccountRepository;
import com.bank.core.domain.exception.AccountNotFoundException;
import com.bank.core.domain.model.Account;
import com.bank.core.domain.model.Money;

import java.util.UUID;

/**
 * Caso de uso: consultar saldo de cuenta.
 */
public class GetBalanceService implements GetBalanceUseCase {

    private final AccountRepository accountRepository;

    public GetBalanceService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Money getBalance(UUID accountId) {
        return accountRepository.findById(accountId)
                .map(Account::getBalance)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }
}
