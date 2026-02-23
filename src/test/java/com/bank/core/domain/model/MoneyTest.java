package com.bank.core.domain.model;

import com.bank.core.domain.exception.CurrencyMismatchException;
import com.bank.core.domain.exception.InsufficientFundsException;
import com.bank.core.domain.exception.InvalidAmountException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyTest {

    @Test
    void shouldCreateMoneyWithValidAmountAndCurrency() {
        Money money = new Money(new BigDecimal("100.50"), "USD");
        assertThat(money.getAmount()).isEqualByComparingTo("100.50");
        assertThat(money.getCurrency()).isEqualTo("USD");
    }

    @Test
    void shouldAddMoneyWithSameCurrency() {
        Money a = new Money("100.00", "USD");
        Money b = new Money("50.50", "USD");
        Money result = a.add(b);
        assertThat(result.getAmount()).isEqualByComparingTo("150.50");
        assertThat(result.getCurrency()).isEqualTo("USD");
    }

    @Test
    void shouldSubtractMoneyWithSameCurrency() {
        Money a = new Money("100.00", "USD");
        Money b = new Money("30.00", "USD");
        Money result = a.subtract(b);
        assertThat(result.getAmount()).isEqualByComparingTo("70.00");
        assertThat(result.getCurrency()).isEqualTo("USD");
    }

    @Test
    void shouldThrowCurrencyMismatchExceptionWhenAddingDifferentCurrencies() {
        Money usd = new Money("100.00", "USD");
        Money eur = new Money("50.00", "EUR");
        assertThatThrownBy(() -> usd.add(eur))
                .isInstanceOf(CurrencyMismatchException.class)
                .hasMessageContaining("Currency mismatch");
    }

    @Test
    void shouldThrowInsufficientFundsExceptionWhenSubtractResultIsNegative() {
        Money a = new Money("50.00", "USD");
        Money b = new Money("100.00", "USD");
        assertThatThrownBy(() -> a.subtract(b))
                .isInstanceOf(InsufficientFundsException.class);
    }

    @Test
    void shouldThrowInvalidAmountExceptionWhenAmountIsNegative() {
        assertThatThrownBy(() -> new Money(new BigDecimal("-10"), "USD"))
                .isInstanceOf(InvalidAmountException.class)
                .hasMessageContaining("negative");
    }

    @Test
    void isGreaterThanOrEqual_shouldReturnTrueWhenGreaterOrEqual() {
        Money a = new Money("100.00", "USD");
        Money b = new Money("50.00", "USD");
        assertThat(a.isGreaterThanOrEqual(b)).isTrue();
        assertThat(a.isGreaterThanOrEqual(a)).isTrue();
    }

    @Test
    void isGreaterThanOrEqual_shouldReturnFalseWhenLess() {
        Money a = new Money("50.00", "USD");
        Money b = new Money("100.00", "USD");
        assertThat(a.isGreaterThanOrEqual(b)).isFalse();
    }
}
