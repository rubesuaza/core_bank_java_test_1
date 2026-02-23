package com.bank.core.domain.model;

import com.bank.core.domain.exception.InactiveAccountException;
import com.bank.core.domain.exception.InvalidAmountException;
import com.bank.core.domain.exception.InsufficientFundsException;

import java.util.UUID;

/**
 * Entidad de dominio que representa una cuenta bancaria.
 */
public final class Account {

    private UUID accountId;
    private String accountNumber;
    private Money balance;
    private final UUID ownerId;
    private AccountStatus status;

    public Account(UUID accountId, String accountNumber, Money balance, UUID ownerId, AccountStatus status) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.ownerId = ownerId;
        this.status = status;
    }

    public UUID accountId() {
        return accountId;
    }

    public String accountNumber() {
        return accountNumber;
    }

    public Money balance() {
        return balance;
    }

    public UUID ownerId() {
        return ownerId;
    }

    public AccountStatus status() {
        return status;
    }

    public void deposit(Money amount) {
        ensureActive();
        validateAmount(amount);
        ensureSameCurrency(amount);
        this.balance = this.balance.add(amount);
    }

    public void withdraw(Money amount) {
        ensureActive();
        validateAmount(amount);
        ensureSameCurrency(amount);
        if (!balance.isGreaterThanOrEqual(amount)) {
            throw new InsufficientFundsException(
                    String.format("Insufficient funds: balance %s is less than requested %s", balance, amount));
        }
        this.balance = this.balance.subtract(amount);
    }

    public Transaction recordDeposit(Money amount, UUID transactionId) {
        deposit(amount);
        return new Transaction(
                transactionId,
                null,
                accountId,
                amount,
                TransactionType.DEPOSIT,
                java.time.LocalDateTime.now()
        );
    }

    public Transaction recordWithdrawal(Money amount, UUID transactionId) {
        withdraw(amount);
        return new Transaction(
                transactionId,
                accountId,
                null,
                amount,
                TransactionType.WITHDRAWAL,
                java.time.LocalDateTime.now()
        );
    }

    private void ensureActive() {
        if (status != AccountStatus.ACTIVE) {
            throw new InactiveAccountException(
                    String.format("Account %s is not active (status: %s)", accountId, status));
        }
    }

    private void validateAmount(Money amount) {
        if (amount == null || amount.amount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }
    }

    private void ensureSameCurrency(Money amount) {
        if (!balance.hasSameCurrency(amount)) {
            throw new com.bank.core.domain.exception.CurrencyMismatchException(
                    String.format("Currency mismatch: account currency %s, transaction currency %s",
                            balance.currency(), amount.currency()));
        }
    }
}
