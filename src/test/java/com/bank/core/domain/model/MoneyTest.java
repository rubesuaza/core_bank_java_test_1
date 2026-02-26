package com.bank.core.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bank.core.domain.exception.CurrencyMismatchException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MoneyTest {

    @Test
    void addShouldSumAmountsOfSameCurrency() {
        Money first = Money.of(new BigDecimal("100.00"), "USD");
        Money second = Money.of(new BigDecimal("50.00"), "USD");

        Money result = first.add(second);

        assertThat(result.getAmount()).isEqualByComparingTo("150.00");
        assertThat(result.getCurrency()).isEqualTo("USD");
    }

    @Test
    void addShouldFailWhenCurrenciesDiffer() {
        Money usd = Money.of(new BigDecimal("100.00"), "USD");
        Money eur = Money.of(new BigDecimal("10.00"), "EUR");

        assertThatThrownBy(() -> usd.add(eur))
                .isInstanceOf(CurrencyMismatchException.class);
    }

    @Test
    void subtractShouldSubtractAmountsOfSameCurrency() {
        Money first = Money.of(new BigDecimal("100.00"), "USD");
        Money second = Money.of(new BigDecimal("40.00"), "USD");

        Money result = first.subtract(second);

        assertThat(result.getAmount()).isEqualByComparingTo("60.00");
        assertThat(result.getCurrency()).isEqualTo("USD");
    }

    @Test
    void operationsMustNotMutateOriginalInstances() {
        Money original = Money.of(new BigDecimal("100.00"), "USD");
        Money other = Money.of(new BigDecimal("40.00"), "USD");

        Money added = original.add(other);
        Money subtracted = original.subtract(other);

        assertThat(original.getAmount()).isEqualByComparingTo("100.00");
        assertThat(added.getAmount()).isEqualByComparingTo("140.00");
        assertThat(subtracted.getAmount()).isEqualByComparingTo("60.00");
    }
}

