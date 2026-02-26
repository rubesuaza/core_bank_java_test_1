package com.bank.core.application.port.in;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * Resultado del caso de uso de creación de cuenta.
 */
public final class AccountCreatedResult {

    private final UUID accountId;
    private final String accountNumber;
    private final String status;
    private final BigDecimal balance;
    private final String currency;

    public AccountCreatedResult(
            UUID accountId,
            String accountNumber,
            String status,
            BigDecimal balance,
            String currency
    ) {
        this.accountId = Objects.requireNonNull(accountId, "accountId must not be null");
        this.accountNumber = Objects.requireNonNull(accountNumber, "accountNumber must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.balance = Objects.requireNonNull(balance, "balance must not be null");
        this.currency = Objects.requireNonNull(currency, "currency must not be null");
    }

    public UUID getAccountId() {
        return accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccountCreatedResult that = (AccountCreatedResult) o;
        return Objects.equals(accountId, that.accountId)
                && Objects.equals(accountNumber, that.accountNumber)
                && Objects.equals(status, that.status)
                && Objects.equals(balance, that.balance)
                && Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, accountNumber, status, balance, currency);
    }

    @Override
    public String toString() {
        return "AccountCreatedResult{" +
                "accountId=" + accountId +
                ", accountNumber='" + accountNumber + '\'' +
                ", status='" + status + '\'' +
                ", balance=" + balance +
                ", currency='" + currency + '\'' +
                '}';
    }
}

