package com.bank.core.domain.model;

import com.bank.core.domain.exception.CurrencyMismatchException;
import com.bank.core.domain.exception.InvalidAmountException;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Objeto de valor inmutable que representa una cantidad de dinero.
 * Incluye métodos para suma, resta y validación de divisa.
 */
public final class Money {

    private final BigDecimal amount;
    private final String currency;

    public Money(BigDecimal amount, String currency) {
        if (amount == null || currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Amount and currency must not be null or empty");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException("Amount cannot be negative: " + amount);
        }
        this.amount = amount.setScale(2, java.math.RoundingMode.HALF_UP);
        this.currency = currency.trim().toUpperCase();
    }

    public Money(String amount, String currency) {
        this(new BigDecimal(amount), currency);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Money add(Money other) {
        validateSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        validateSameCurrency(other);
        BigDecimal result = this.amount.subtract(other.amount);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new com.bank.core.domain.exception.InsufficientFundsException(
                    "Result would be negative: " + result);
        }
        return new Money(result, this.currency);
    }

    private void validateSameCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new CurrencyMismatchException(
                    "Currency mismatch: " + this.currency + " vs " + other.currency);
        }
    }

    public boolean isGreaterThanOrEqual(Money other) {
        validateSameCurrency(other);
        return this.amount.compareTo(other.amount) >= 0;
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
