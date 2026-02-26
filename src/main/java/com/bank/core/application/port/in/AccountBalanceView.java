package com.bank.core.application.port.in;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * Proyección de lectura para el saldo de una cuenta.
 */
public final class AccountBalanceView {

    private final UUID accountId;
    private final BigDecimal balance;
    private final String currency;

    public AccountBalanceView(UUID accountId, BigDecimal balance, String currency) {
        this.accountId = Objects.requireNonNull(accountId, "accountId must not be null");
        this.balance = Objects.requireNonNull(balance, "balance must not be null");
        this.currency = Objects.requireNonNull(currency, "currency must not be null");
    }

    public UUID getAccountId() {
        return accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }
}

