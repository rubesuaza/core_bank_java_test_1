package com.bank.core.domain.entity;

import com.bank.core.domain.exception.*;
import com.bank.core.domain.model.AccountStatus;
import com.bank.core.domain.valueobject.Money;

import java.util.UUID;

/**
 * Agregado raíz que representa una cuenta bancaria.
 */
public class Account {

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
        if (amount.getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("El monto debe ser mayor a cero");
        }
        if (!balance.hasSameCurrency(amount)) {
            throw new CurrencyMismatchException("La divisa del depósito debe coincidir con la de la cuenta");
        }
        this.balance = this.balance.add(amount);
    }

    public void withdraw(Money amount) {
        requireActive("Solo se puede retirar de cuenta activa");
        if (amount.getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("El monto debe ser mayor a cero");
        }
        if (!balance.hasSameCurrency(amount)) {
            throw new CurrencyMismatchException("La divisa del retiro debe coincidir con la de la cuenta");
        }
        if (balance.getAmount().compareTo(amount.getAmount()) < 0) {
            throw new InsufficientFundsException("Fondos insuficientes");
        }
        this.balance = this.balance.subtract(amount);
    }

    public void transferTo(Account destination, Money amount) {
        if (this.equals(destination)) {
            throw new InvalidTransferException("No se puede transferir a la misma cuenta");
        }
        requireActive("La cuenta origen debe estar activa");
        destination.requireActive("La cuenta destino debe estar activa");
        if (amount.getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("El monto debe ser mayor a cero");
        }
        if (!balance.hasSameCurrency(amount)) {
            throw new CurrencyMismatchException("La divisa debe coincidir con la de la cuenta");
        }
        if (balance.getAmount().compareTo(amount.getAmount()) < 0) {
            throw new InsufficientFundsException("Fondos insuficientes");
        }
        if (!balance.hasSameCurrency(destination.getBalance())) {
            throw new CurrencyMismatchException("No se pueden transferir entre cuentas de diferente divisa");
        }
        this.balance = this.balance.subtract(amount);
        destination.balance = destination.balance.add(amount);
    }

    private void requireActive(String message) {
        if (status != AccountStatus.ACTIVE) {
            throw new InactiveAccountException(message);
        }
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
