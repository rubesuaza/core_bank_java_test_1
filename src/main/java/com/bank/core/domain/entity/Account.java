package com.bank.core.domain.entity;

import com.bank.core.domain.exception.*;
import com.bank.core.domain.model.AccountStatus;
import com.bank.core.domain.valueobject.Money;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * Agregado raíz que representa una cuenta bancaria.
 */
public class Account {

    private static final BigDecimal ZERO = BigDecimal.ZERO;

    private final UUID accountId;
    private final String accountNumber;
    private Money balance;
    private final UUID ownerId;
    private final AccountStatus status;

    public Account(UUID accountId, String accountNumber, Money balance, UUID ownerId, AccountStatus status) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.ownerId = ownerId;
        this.status = status;
    }

    public void deposit(Money amount) {
        requireActive("Solo se puede depositar en cuenta activa");
        validatePositiveAmount(amount);
        validateSameCurrency(amount, "La divisa del depósito debe coincidir con la de la cuenta");
        this.balance = this.balance.add(amount);
    }

    public void withdraw(Money amount) {
        requireActive("Solo se puede retirar de cuenta activa");
        validatePositiveAmount(amount);
        validateSameCurrency(amount, "La divisa del retiro debe coincidir con la de la cuenta");
        validateSufficientFunds(amount);
        this.balance = this.balance.subtract(amount);
    }

    public void transferTo(Account destination, Money amount) {
        validateNotSameAccount(destination);
        requireActive("La cuenta origen debe estar activa");
        destination.requireActive("La cuenta destino debe estar activa");
        validateTransferAmount(amount, destination);
        this.balance = this.balance.subtract(amount);
        destination.credit(amount);
    }

    void credit(Money amount) {
        this.balance = this.balance.add(amount);
    }

    private void validatePositiveAmount(Money amount) {
        if (amount.getAmount().compareTo(ZERO) <= 0) {
            throw new InvalidAmountException("El monto debe ser mayor a cero");
        }
    }

    private void validateSameCurrency(Money amount, String message) {
        if (!balance.hasSameCurrency(amount)) {
            throw new CurrencyMismatchException(message);
        }
    }

    private void validateSufficientFunds(Money amount) {
        if (balance.getAmount().compareTo(amount.getAmount()) < 0) {
            throw new InsufficientFundsException("Fondos insuficientes");
        }
    }

    private void validateNotSameAccount(Account destination) {
        if (this.equals(destination)) {
            throw new InvalidTransferException("No se puede transferir a la misma cuenta");
        }
    }

    private void validateTransferAmount(Money amount, Account destination) {
        validatePositiveAmount(amount);
        validateSameCurrency(amount, "La divisa debe coincidir con la de la cuenta");
        validateSufficientFunds(amount);
        if (!balance.hasSameCurrency(destination.getBalance())) {
            throw new CurrencyMismatchException("No se pueden transferir entre cuentas de diferente divisa");
        }
    }

    private void requireActive(String message) {
        if (status != AccountStatus.ACTIVE) {
            throw new InactiveAccountException(message);
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
}
