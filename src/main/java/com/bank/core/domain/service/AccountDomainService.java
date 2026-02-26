package com.bank.core.domain.service;

import com.bank.core.domain.exception.CurrencyMismatchException;
import com.bank.core.domain.exception.DomainException;
import com.bank.core.domain.exception.InactiveAccountException;
import com.bank.core.domain.exception.InvalidAmountException;
import com.bank.core.domain.model.Account;
import com.bank.core.domain.model.AccountStatus;
import com.bank.core.domain.model.Money;
import com.bank.core.domain.model.Transaction;
import com.bank.core.domain.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Servicio de dominio para operaciones sobre cuentas.
 */
public class AccountDomainService {

    public Transaction deposit(Account account, Money amount, LocalDateTime timestamp) {
        Objects.requireNonNull(account, "account must not be null");
        Objects.requireNonNull(amount, "amount must not be null");
        Objects.requireNonNull(timestamp, "timestamp must not be null");

        account.credit(amount);

        return new Transaction(
                UUID.randomUUID(),
                TransactionType.DEPOSIT,
                null,
                account.getAccountId(),
                amount,
                timestamp
        );
    }

    public Transaction withdraw(Account account, Money amount, LocalDateTime timestamp) {
        Objects.requireNonNull(account, "account must not be null");
        Objects.requireNonNull(amount, "amount must not be null");
        Objects.requireNonNull(timestamp, "timestamp must not be null");

        account.debit(amount);

        return new Transaction(
                UUID.randomUUID(),
                TransactionType.WITHDRAWAL,
                account.getAccountId(),
                null,
                amount,
                timestamp
        );
    }

    public Transaction transfer(Account source, Account destination, Money amount, LocalDateTime timestamp) {
        Objects.requireNonNull(source, "source must not be null");
        Objects.requireNonNull(destination, "destination must not be null");
        Objects.requireNonNull(amount, "amount must not be null");
        Objects.requireNonNull(timestamp, "timestamp must not be null");

        if (source.getAccountId().equals(destination.getAccountId())) {
            throw new DomainException("Source and destination accounts must be different");
        }

        if (!source.getBalance().getCurrency().equals(destination.getBalance().getCurrency())
                || !source.getBalance().getCurrency().equals(amount.getCurrency())) {
            throw new CurrencyMismatchException("All accounts and amount must share the same currency");
        }

        if (amount.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Transfer amount must be positive");
        }

        if (source.getStatus() != AccountStatus.ACTIVE || destination.getStatus() != AccountStatus.ACTIVE) {
            throw new InactiveAccountException("Both accounts must be active for transfer");
        }

        source.debit(amount);
        destination.credit(amount);

        return new Transaction(
                UUID.randomUUID(),
                TransactionType.TRANSFER,
                source.getAccountId(),
                destination.getAccountId(),
                amount,
                timestamp
        );
    }
}

