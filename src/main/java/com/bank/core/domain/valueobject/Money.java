package com.bank.core.domain.valueobject;

import com.bank.core.domain.exception.CurrencyMismatchException;
import com.bank.core.domain.exception.InvalidAmountException;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Objeto de valor inmutable que representa una cantidad monetaria.
 */
public final class Money {

    private final BigDecimal amount;
    private final String currency;

    public Money(BigDecimal amount, String currency) {
        if (amount == null) {
            throw new IllegalArgumentException("El monto no puede ser nulo");
        }
        if (currency == null) {
            throw new IllegalArgumentException("La divisa no puede ser nula");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException("El monto no puede ser negativo");
        }
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Money add(Money other) {
        requireSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        requireSameCurrency(other);
        BigDecimal result = this.amount.subtract(other.amount);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException("El resultado de la resta no puede ser negativo");
        }
        return new Money(result, this.currency);
    }

    public boolean hasSameCurrency(Money other) {
        return this.currency.equals(other.currency);
    }

    private void requireSameCurrency(Money other) {
        if (!hasSameCurrency(other)) {
            throw new CurrencyMismatchException("No se pueden operar montos de diferente divisa");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount) && Objects.equals(currency, money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
}
