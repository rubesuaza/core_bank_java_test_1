package com.bank.core.domain.valueobject;

import com.bank.core.domain.exception.CurrencyMismatchException;
import com.bank.core.domain.exception.InvalidAmountException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Money - Objeto de Valor")
class MoneyTest {

    @Nested
    @DisplayName("Creación")
    class Creation {

        @Test
        void creaDineroConMontoYDivisaValidos() {
            Money money = new Money(new BigDecimal("100.50"), "USD");

            assertThat(money.getAmount()).isEqualByComparingTo("100.50");
            assertThat(money.getCurrency()).isEqualTo("USD");
        }

        @Test
        void rechazaMontoNulo() {
            assertThatThrownBy(() -> new Money(null, "USD"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void rechazaDivisaNula() {
            assertThatThrownBy(() -> new Money(BigDecimal.ZERO, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void rechazaMontoNegativo() {
            assertThatThrownBy(() -> new Money(new BigDecimal("-10"), "USD"))
                    .isInstanceOf(InvalidAmountException.class)
                    .hasMessageContaining("negativo");
        }

        @Test
        void aceptaMontoCero() {
            Money money = new Money(BigDecimal.ZERO, "EUR");
            assertThat(money.getAmount()).isEqualByComparingTo(BigDecimal.ZERO);
        }
    }

    @Nested
    @DisplayName("Suma")
    class Add {

        @Test
        void sumaDosMontosMismaDivisa() {
            Money a = new Money(new BigDecimal("100"), "USD");
            Money b = new Money(new BigDecimal("50.25"), "USD");

            Money result = a.add(b);

            assertThat(result.getAmount()).isEqualByComparingTo("150.25");
            assertThat(result.getCurrency()).isEqualTo("USD");
        }

        @Test
        void sumaRetornaNuevoObjetoInmutable() {
            Money a = new Money(new BigDecimal("100"), "USD");
            Money b = new Money(new BigDecimal("50"), "USD");

            Money result = a.add(b);

            assertThat(a.getAmount()).isEqualByComparingTo("100");
            assertThat(result).isNotSameAs(a);
        }

        @Test
        void sumaConDivisasDiferentesLanzaExcepcion() {
            Money usd = new Money(new BigDecimal("100"), "USD");
            Money eur = new Money(new BigDecimal("50"), "EUR");

            assertThatThrownBy(() -> usd.add(eur))
                    .isInstanceOf(CurrencyMismatchException.class)
                    .hasMessageContaining("divisa");
        }
    }

    @Nested
    @DisplayName("Resta")
    class Subtract {

        @Test
        void restaDosMontosMismaDivisa() {
            Money a = new Money(new BigDecimal("100"), "USD");
            Money b = new Money(new BigDecimal("30.50"), "USD");

            Money result = a.subtract(b);

            assertThat(result.getAmount()).isEqualByComparingTo("69.50");
            assertThat(result.getCurrency()).isEqualTo("USD");
        }

        @Test
        void restaConDivisasDiferentesLanzaExcepcion() {
            Money usd = new Money(new BigDecimal("100"), "USD");
            Money eur = new Money(new BigDecimal("50"), "EUR");

            assertThatThrownBy(() -> usd.subtract(eur))
                    .isInstanceOf(CurrencyMismatchException.class);
        }

        @Test
        void restaQueResultaNegativoLanzaExcepcion() {
            Money a = new Money(new BigDecimal("50"), "USD");
            Money b = new Money(new BigDecimal("100"), "USD");

            assertThatThrownBy(() -> a.subtract(b))
                    .isInstanceOf(InvalidAmountException.class);
        }
    }

    @Nested
    @DisplayName("Validación de divisa")
    class CurrencyValidation {

        @Test
        void hasSameCurrencyRetornaTrueParaMismaDivisa() {
            Money a = new Money(new BigDecimal("100"), "USD");
            Money b = new Money(new BigDecimal("50"), "USD");

            assertThat(a.hasSameCurrency(b)).isTrue();
        }

        @Test
        void hasSameCurrencyRetornaFalseParaDivisasDiferentes() {
            Money usd = new Money(new BigDecimal("100"), "USD");
            Money eur = new Money(new BigDecimal("100"), "EUR");

            assertThat(usd.hasSameCurrency(eur)).isFalse();
        }
    }
}
