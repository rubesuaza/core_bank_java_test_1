package com.bank.core.domain.model;

import com.bank.core.domain.exception.CurrencyMismatchException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyTest {

    @Test
    void shouldCreateMoneyWithAmountAndCurrency() {
        Money money = new Money(new BigDecimal("100.50"), "USD");

        assertThat(money.amount()).isEqualByComparingTo(new BigDecimal("100.50"));
        assertThat(money.currency()).isEqualTo("USD");
    }

    @Test
    void shouldAddMoneyWithSameCurrency() {
        Money a = new Money(new BigDecimal("100"), "USD");
        Money b = new Money(new BigDecimal("50.25"), "USD");

        Money result = a.add(b);

        assertThat(result.amount()).isEqualByComparingTo(new BigDecimal("150.25"));
        assertThat(result.currency()).isEqualTo("USD");
    }

    @Test
    void shouldThrowCurrencyMismatchExceptionWhenAddingDifferentCurrencies() {
        Money usd = new Money(new BigDecimal("100"), "USD");
        Money eur = new Money(new BigDecimal("50"), "EUR");

        assertThatThrownBy(() -> usd.add(eur))
                .isInstanceOf(CurrencyMismatchException.class)
                .hasMessageContaining("USD")
                .hasMessageContaining("EUR");
    }

    @Test
    void shouldSubtractMoneyWithSameCurrency() {
        Money a = new Money(new BigDecimal("100"), "USD");
        Money b = new Money(new BigDecimal("30.50"), "USD");

        Money result = a.subtract(b);

        assertThat(result.amount()).isEqualByComparingTo(new BigDecimal("69.50"));
        assertThat(result.currency()).isEqualTo("USD");
    }

    @Test
    void shouldThrowCurrencyMismatchExceptionWhenSubtractingDifferentCurrencies() {
        Money usd = new Money(new BigDecimal("100"), "USD");
        Money eur = new Money(new BigDecimal("50"), "EUR");

        assertThatThrownBy(() -> usd.subtract(eur))
                .isInstanceOf(CurrencyMismatchException.class);
    }

    @Test
    void shouldValidateSameCurrency() {
        Money usd = new Money(new BigDecimal("100"), "USD");
        Money eur = new Money(new BigDecimal("50"), "EUR");

        assertThat(usd.hasSameCurrency(usd)).isTrue();
        assertThat(usd.hasSameCurrency(eur)).isFalse();
    }

    @Test
    void shouldBeGreaterThanOrEqual() {
        Money hundred = new Money(new BigDecimal("100"), "USD");
        Money fifty = new Money(new BigDecimal("50"), "USD");
        Money alsoHundred = new Money(new BigDecimal("100"), "USD");

        assertThat(hundred.isGreaterThanOrEqual(fifty)).isTrue();
        assertThat(hundred.isGreaterThanOrEqual(alsoHundred)).isTrue();
        assertThat(fifty.isGreaterThanOrEqual(hundred)).isFalse();
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNull() {
        assertThatThrownBy(() -> new Money(null, "USD"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("amount");
    }

    @Test
    void shouldThrowExceptionWhenCurrencyIsNull() {
        assertThatThrownBy(() -> new Money(new BigDecimal("100"), null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("currency");
    }

    @Test
    void shouldThrowExceptionWhenCurrencyIsBlank() {
        assertThatThrownBy(() -> new Money(new BigDecimal("100"), ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("blank");

        assertThatThrownBy(() -> new Money(new BigDecimal("100"), "   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("blank");
    }

    @Test
    void moneyShouldBeImmutable() {
        Money original = new Money(new BigDecimal("100"), "USD");
        Money added = original.add(new Money(new BigDecimal("50"), "USD"));

        assertThat(original.amount()).isEqualByComparingTo(new BigDecimal("100"));
        assertThat(added.amount()).isEqualByComparingTo(new BigDecimal("150"));
    }

    @Test
    void shouldImplementEqualsCorrectly() {
        Money a = new Money(new BigDecimal("100.00"), "USD");
        Money b = new Money(new BigDecimal("100.00"), "USD");
        Money c = new Money(new BigDecimal("100.01"), "USD");
        Money d = new Money(new BigDecimal("100"), "EUR");

        assertThat(a).isEqualTo(b);
        assertThat(a).isNotEqualTo(c);
        assertThat(a).isNotEqualTo(d);
        assertThat(a.equals(null)).isFalse();
        assertThat(a).isEqualTo(a);
    }

    @Test
    void shouldImplementHashCodeConsistentlyWithEquals() {
        Money a = new Money(new BigDecimal("100.00"), "USD");
        Money b = new Money(new BigDecimal("100.00"), "USD");

        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
