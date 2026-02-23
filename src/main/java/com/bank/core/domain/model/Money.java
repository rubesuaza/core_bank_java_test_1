package com.bank.core.domain.model;

import com.bank.core.domain.exception.CurrencyMismatchException;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Objeto de Valor inmutable que representa una cantidad monetaria.
 */
public final class Money {

    private final BigDecimal amount;
    private final String currency;

    public Money(BigDecimal amount, String currency) {
        this.amount = Objects.requireNonNull(amount, "amount cannot be null");
        this.currency = Objects.requireNonNull(currency, "currency cannot be null");
        if (currency.isBlank()) {
            throw new IllegalArgumentException("currency cannot be blank");
        }
    }

    public BigDecimal amount() {
        return amount;
    }

    public String currency() {
        return currency;
    }

    public Money add(Money other) {
        ensureSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        ensureSameCurrency(other);
        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    public boolean hasSameCurrency(Money other) {
        return this.currency.equals(other.currency);
    }

    public boolean isGreaterThanOrEqual(Money other) {
        ensureSameCurrency(other);
        return this.amount.compareTo(other.amount) >= 0;
    }

    private void ensureSameCurrency(Money other) {
        if (!hasSameCurrency(other)) {
            throw new CurrencyMismatchException(
                    String.format("Currency mismatch: %s and %s cannot be used together", this.currency, other.currency));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount.compareTo(money.amount) == 0 && Objects.equals(currency, money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
}
