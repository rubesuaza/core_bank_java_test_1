package com.bank.core.domain.entity;

import com.bank.core.domain.exception.InactiveAccountException;
import com.bank.core.domain.exception.InsufficientFundsException;
import com.bank.core.domain.exception.InvalidAmountException;
import com.bank.core.domain.exception.InvalidTransferException;
import com.bank.core.domain.exception.CurrencyMismatchException;
import com.bank.core.domain.model.AccountStatus;
import com.bank.core.domain.valueobject.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Account - Agregado Raíz")
class AccountTest {

    private static final String USD = "USD";
    private UUID accountId;
    private UUID ownerId;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
        ownerId = UUID.randomUUID();
    }

    private Account createActiveAccount(Money balance) {
        return new Account(accountId, "ACC-001", balance, ownerId, AccountStatus.ACTIVE);
    }

    @Nested
    @DisplayName("Depósito")
    class Deposit {

        @Test
        void depositaMontoPositivoEnCuentaActiva() {
            Account account = createActiveAccount(new Money(BigDecimal.ZERO, USD));
            Money depositAmount = new Money(new BigDecimal("100"), USD);

            account.deposit(depositAmount);

            assertThat(account.getBalance().getAmount()).isEqualByComparingTo("100");
        }

        @Test
        void depositoEnCuentaCongeladaLanzaExcepcion() {
            Account account = new Account(accountId, "ACC-001",
                    new Money(BigDecimal.ZERO, USD), ownerId, AccountStatus.FROZEN);

            assertThatThrownBy(() -> account.deposit(new Money(new BigDecimal("100"), USD)))
                    .isInstanceOf(InactiveAccountException.class)
                    .hasMessageContaining("activa");
        }

        @Test
        void depositoEnCuentaCerradaLanzaExcepcion() {
            Account account = new Account(accountId, "ACC-001",
                    new Money(BigDecimal.ZERO, USD), ownerId, AccountStatus.CLOSED);

            assertThatThrownBy(() -> account.deposit(new Money(new BigDecimal("100"), USD)))
                    .isInstanceOf(InactiveAccountException.class);
        }

        @Test
        void depositoConMontoCeroLanzaExcepcion() {
            Account account = createActiveAccount(new Money(BigDecimal.ZERO, USD));

            assertThatThrownBy(() -> account.deposit(new Money(BigDecimal.ZERO, USD)))
                    .isInstanceOf(InvalidAmountException.class)
                    .hasMessageContaining("mayor a cero");
        }

        @Test
        void depositoConDivisaDiferenteLanzaExcepcion() {
            Account account = createActiveAccount(new Money(BigDecimal.ZERO, USD));

            assertThatThrownBy(() -> account.deposit(new Money(new BigDecimal("100"), "EUR")))
                    .isInstanceOf(CurrencyMismatchException.class);
        }
    }

    @Nested
    @DisplayName("Retiro")
    class Withdraw {

        @Test
        void retiraMontoDisponibleEnCuentaActiva() {
            Account account = createActiveAccount(new Money(new BigDecimal("100"), USD));

            account.withdraw(new Money(new BigDecimal("30"), USD));

            assertThat(account.getBalance().getAmount()).isEqualByComparingTo("70");
        }

        @Test
        void retiroConFondosInsuficientesLanzaExcepcion() {
            Account account = createActiveAccount(new Money(new BigDecimal("50"), USD));

            assertThatThrownBy(() -> account.withdraw(new Money(new BigDecimal("100"), USD)))
                    .isInstanceOf(InsufficientFundsException.class)
                    .hasMessageContaining("insuficientes");
        }

        @Test
        void retiroEnCuentaCongeladaLanzaExcepcion() {
            Account account = new Account(accountId, "ACC-001",
                    new Money(new BigDecimal("100"), USD), ownerId, AccountStatus.FROZEN);

            assertThatThrownBy(() -> account.withdraw(new Money(new BigDecimal("50"), USD)))
                    .isInstanceOf(InactiveAccountException.class);
        }

        @Test
        void retiroConMontoCeroLanzaExcepcion() {
            Account account = createActiveAccount(new Money(new BigDecimal("100"), USD));

            assertThatThrownBy(() -> account.withdraw(new Money(BigDecimal.ZERO, USD)))
                    .isInstanceOf(InvalidAmountException.class);
        }
    }

    @Nested
    @DisplayName("Transferencia")
    class Transfer {

        @Test
        void transferirACuentaDiferenteDescuentaOrigenYAcreditaDestino() {
            Account origin = createActiveAccount(new Money(new BigDecimal("200"), USD));
            UUID destId = UUID.randomUUID();
            Account destination = new Account(destId, "ACC-002",
                    new Money(new BigDecimal("50"), USD), UUID.randomUUID(), AccountStatus.ACTIVE);

            Money amount = new Money(new BigDecimal("75"), USD);
            origin.transferTo(destination, amount);

            assertThat(origin.getBalance().getAmount()).isEqualByComparingTo("125");
            assertThat(destination.getBalance().getAmount()).isEqualByComparingTo("125");
        }

        @Test
        void transferirALaMismaCuentaLanzaExcepcion() {
            Account account = createActiveAccount(new Money(new BigDecimal("200"), USD));

            assertThatThrownBy(() -> account.transferTo(account, new Money(new BigDecimal("50"), USD)))
                    .isInstanceOf(InvalidTransferException.class)
                    .hasMessageContaining("misma cuenta");
        }

        @Test
        void transferirConDivisaDiferenteLanzaExcepcion() {
            Account origin = createActiveAccount(new Money(new BigDecimal("200"), USD));
            Account destination = new Account(UUID.randomUUID(), "ACC-002",
                    new Money(new BigDecimal("50"), "EUR"), UUID.randomUUID(), AccountStatus.ACTIVE);

            assertThatThrownBy(() -> origin.transferTo(destination, new Money(new BigDecimal("50"), USD)))
                    .isInstanceOf(CurrencyMismatchException.class);
        }

        @Test
        void transferirConFondosInsuficientesLanzaExcepcion() {
            Account origin = createActiveAccount(new Money(new BigDecimal("50"), USD));
            Account destination = new Account(UUID.randomUUID(), "ACC-002",
                    new Money(BigDecimal.ZERO, USD), UUID.randomUUID(), AccountStatus.ACTIVE);

            assertThatThrownBy(() -> origin.transferTo(destination, new Money(new BigDecimal("100"), USD)))
                    .isInstanceOf(InsufficientFundsException.class);
        }

        @Test
        void transferirACuentaNoActivaLanzaExcepcion() {
            Account origin = createActiveAccount(new Money(new BigDecimal("200"), USD));
            Account frozenDestination = new Account(UUID.randomUUID(), "ACC-002",
                    new Money(BigDecimal.ZERO, USD), UUID.randomUUID(), AccountStatus.FROZEN);

            assertThatThrownBy(() -> origin.transferTo(frozenDestination, new Money(new BigDecimal("50"), USD)))
                    .isInstanceOf(InactiveAccountException.class);
        }
    }
}
