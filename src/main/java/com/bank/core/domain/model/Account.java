package com.bank.core.domain.model;

import com.bank.core.domain.exception.InactiveAccountException;
import com.bank.core.domain.exception.InsufficientFundsException;
import com.bank.core.domain.exception.InvalidAmountException;

import java.util.Objects;
import java.util.UUID;

/**
 * Agregado raíz que representa una cuenta bancaria.
 * Identidad: accountId (UUID).
 */
public class Account {

    private final UUID accountId;
    private final String accountNumber;
    private Money balance;
    private final UUID ownerId;
    private AccountStatus status;

    public Account(UUID accountId, String accountNumber, Money balance, UUID ownerId, AccountStatus status) {
        if (accountId == null || accountNumber == null || accountNumber.isBlank() ||
                balance == null || ownerId == null || status == null) {
            throw new IllegalArgumentException("All account fields must be non-null");
        }
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.ownerId = ownerId;
        this.status = status;
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

    /**
     * Deposita dinero en la cuenta.
     * Regla: monto debe ser estrictamente mayor a cero.
     * Regla: cuenta debe estar ACTIVE.
     */
    public void deposit(Money amount) {
        validateActiveStatus();
        validatePositiveAmount(amount);
        validateCurrencyMatch(amount);
        this.balance = this.balance.add(amount);
    }

    /**
     * Retira dinero de la cuenta.
     * Regla: fondos insuficientes si el balance resultante sería negativo.
     * Regla: cuenta debe estar ACTIVE.
     * Regla: monto debe ser estrictamente mayor a cero.
     */
    public void withdraw(Money amount) {
        validateActiveStatus();
        validatePositiveAmount(amount);
        validateCurrencyMatch(amount);
        if (!balance.isGreaterThanOrEqual(amount)) {
            throw new InsufficientFundsException(
                    "Insufficient funds. Balance: " + balance.getAmount() + " " + balance.getCurrency() +
                            ", requested: " + amount.getAmount() + " " + amount.getCurrency());
        }
        this.balance = this.balance.subtract(amount);
    }

    /**
     * Transfiere dinero desde esta cuenta a otra.
     * Regla: no se puede transferir a la misma cuenta.
     */
    public void transferTo(Account destination, Money amount) {
        if (this.accountId.equals(destination.getAccountId())) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        this.withdraw(amount);
        destination.deposit(amount);
    }

    private void validateActiveStatus() {
        if (status != AccountStatus.ACTIVE) {
            throw new InactiveAccountException(
                    "Account " + accountId + " is not active. Current status: " + status);
        }
    }

    private void validatePositiveAmount(Money amount) {
        if (amount.getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be strictly greater than zero: " + amount.getAmount());
        }
    }

    private void validateCurrencyMatch(Money amount) {
        if (!this.balance.getCurrency().equals(amount.getCurrency())) {
            throw new com.bank.core.domain.exception.CurrencyMismatchException(
                    "Transaction currency " + amount.getCurrency() +
                            " does not match account currency " + this.balance.getCurrency());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountId, account.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }
}
