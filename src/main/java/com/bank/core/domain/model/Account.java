package com.bank.core.domain.model;

import com.bank.core.domain.exception.CurrencyMismatchException;
import com.bank.core.domain.exception.InactiveAccountException;
import com.bank.core.domain.exception.InsufficientFundsException;
import com.bank.core.domain.exception.InvalidAmountException;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidad de cuenta bancaria.
 */
public class Account {

    private final UUID accountId;
    private final String accountNumber;
    private Money balance;
    private final UUID ownerId;
    private AccountStatus status;

    public Account(UUID accountId, String accountNumber, Money balance, UUID ownerId, AccountStatus status) {
        this.accountId = Objects.requireNonNull(accountId, "accountId must not be null");
        this.accountNumber = Objects.requireNonNull(accountNumber, "accountNumber must not be null");
        this.balance = Objects.requireNonNull(balance, "balance must not be null");
        this.ownerId = Objects.requireNonNull(ownerId, "ownerId must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
    }

    public UUID getAccountId() {
        return accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Money getBalance() {
        return balance;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = Objects.requireNonNull(status, "status must not be null");
    }

    public void credit(Money amount) {
        ensureActive();
        ensureValidAmount(amount);
        ensureSameCurrency(amount);
        this.balance = this.balance.add(amount);
    }

    public void debit(Money amount) {
        ensureActive();
        ensureValidAmount(amount);
        ensureSameCurrency(amount);

        if (balance.getAmount().compareTo(amount.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds for debit operation");
        }

        this.balance = this.balance.subtract(amount);
    }

    private void ensureActive() {
        if (this.status != AccountStatus.ACTIVE) {
            throw new InactiveAccountException("Account is not active");
        }
    }

    private void ensureValidAmount(Money amount) {
        Objects.requireNonNull(amount, "amount must not be null");
        if (amount.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be positive");
        }
    }

    private void ensureSameCurrency(Money money) {
        if (!Objects.equals(this.balance.getCurrency(), money.getCurrency())) {
            throw new CurrencyMismatchException("Currency mismatch between account balance and operation amount");
        }
    }
}

