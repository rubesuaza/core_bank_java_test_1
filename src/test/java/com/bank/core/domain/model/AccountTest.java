package com.bank.core.domain.model;

import com.bank.core.domain.exception.InactiveAccountException;
import com.bank.core.domain.exception.InsufficientFundsException;
import com.bank.core.domain.exception.InvalidAmountException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountTest {

    private static final UUID ACCOUNT_ID = UUID.randomUUID();
    private static final UUID OWNER_ID = UUID.randomUUID();
    private static final String ACCOUNT_NUMBER = "1234567890";

    @Test
    void shouldCreateActiveAccount() {
        Account account = new Account(
                ACCOUNT_ID,
                ACCOUNT_NUMBER,
                new Money(new BigDecimal("100"), "USD"),
                OWNER_ID,
                AccountStatus.ACTIVE
        );

        assertThat(account.accountId()).isEqualTo(ACCOUNT_ID);
        assertThat(account.accountNumber()).isEqualTo(ACCOUNT_NUMBER);
        assertThat(account.balance().amount()).isEqualByComparingTo(new BigDecimal("100"));
        assertThat(account.ownerId()).isEqualTo(OWNER_ID);
        assertThat(account.status()).isEqualTo(AccountStatus.ACTIVE);
    }

    @Test
    void shouldDepositAndIncreaseBalance() {
        Account account = createActiveAccount("100");
        Money depositAmount = new Money(new BigDecimal("50"), "USD");

        account.deposit(depositAmount);

        assertThat(account.balance().amount()).isEqualByComparingTo(new BigDecimal("150"));
    }

    @Test
    void shouldWithdrawAndDecreaseBalance() {
        Account account = createActiveAccount("100");
        Money withdrawAmount = new Money(new BigDecimal("30"), "USD");

        account.withdraw(withdrawAmount);

        assertThat(account.balance().amount()).isEqualByComparingTo(new BigDecimal("70"));
    }

    @Test
    void shouldThrowInsufficientFundsExceptionWhenWithdrawingMoreThanBalance() {
        Account account = createActiveAccount("50");
        Money withdrawAmount = new Money(new BigDecimal("100"), "USD");

        assertThatThrownBy(() -> account.withdraw(withdrawAmount))
                .isInstanceOf(InsufficientFundsException.class);
    }

    @Test
    void shouldThrowInsufficientFundsExceptionWhenWithdrawingExactBalanceResultsInNegative() {
        Account account = createActiveAccount("0");
        Money withdrawAmount = new Money(new BigDecimal("1"), "USD");

        assertThatThrownBy(() -> account.withdraw(withdrawAmount))
                .isInstanceOf(InsufficientFundsException.class);
    }

    @Test
    void shouldThrowInactiveAccountExceptionWhenDepositingToFrozenAccount() {
        Account account = createAccount("100", AccountStatus.FROZEN);

        assertThatThrownBy(() -> account.deposit(new Money(new BigDecimal("10"), "USD")))
                .isInstanceOf(InactiveAccountException.class);
    }

    @Test
    void shouldThrowInactiveAccountExceptionWhenDepositingToClosedAccount() {
        Account account = createAccount("100", AccountStatus.CLOSED);

        assertThatThrownBy(() -> account.deposit(new Money(new BigDecimal("10"), "USD")))
                .isInstanceOf(InactiveAccountException.class);
    }

    @Test
    void shouldThrowInactiveAccountExceptionWhenWithdrawingFromFrozenAccount() {
        Account account = createAccount("100", AccountStatus.FROZEN);

        assertThatThrownBy(() -> account.withdraw(new Money(new BigDecimal("10"), "USD")))
                .isInstanceOf(InactiveAccountException.class);
    }

    @Test
    void shouldThrowInvalidAmountExceptionWhenDepositingZero() {
        Account account = createActiveAccount("100");

        assertThatThrownBy(() -> account.deposit(new Money(BigDecimal.ZERO, "USD")))
                .isInstanceOf(InvalidAmountException.class);
    }

    @Test
    void shouldThrowInvalidAmountExceptionWhenDepositingNegative() {
        Account account = createActiveAccount("100");

        assertThatThrownBy(() -> account.deposit(new Money(new BigDecimal("-10"), "USD")))
                .isInstanceOf(InvalidAmountException.class);
    }

    @Test
    void shouldThrowInvalidAmountExceptionWhenWithdrawingZero() {
        Account account = createActiveAccount("100");

        assertThatThrownBy(() -> account.withdraw(new Money(BigDecimal.ZERO, "USD")))
                .isInstanceOf(InvalidAmountException.class);
    }

    @Test
    void shouldThrowInvalidAmountExceptionWhenWithdrawingNegative() {
        Account account = createActiveAccount("100");

        assertThatThrownBy(() -> account.withdraw(new Money(new BigDecimal("-10"), "USD")))
                .isInstanceOf(InvalidAmountException.class);
    }

    @Test
    void shouldThrowCurrencyMismatchExceptionWhenDepositingDifferentCurrency() {
        Account account = createActiveAccount("100"); // USD

        assertThatThrownBy(() -> account.deposit(new Money(new BigDecimal("10"), "EUR")))
                .isInstanceOf(com.bank.core.domain.exception.CurrencyMismatchException.class);
    }

    @Test
    void shouldThrowCurrencyMismatchExceptionWhenWithdrawingDifferentCurrency() {
        Account account = createActiveAccount("100"); // USD

        assertThatThrownBy(() -> account.withdraw(new Money(new BigDecimal("10"), "EUR")))
                .isInstanceOf(com.bank.core.domain.exception.CurrencyMismatchException.class);
    }

    @Test
    void shouldCreateTransactionForDeposit() {
        Account account = createActiveAccount("100");
        Money amount = new Money(new BigDecimal("25"), "USD");

        Transaction transaction = account.recordDeposit(amount, UUID.randomUUID());

        assertThat(transaction.type()).isEqualTo(TransactionType.DEPOSIT);
        assertThat(transaction.amount()).isEqualTo(amount);
        assertThat(transaction.sourceAccountId()).isNull();
        assertThat(transaction.destinationAccountId()).isEqualTo(ACCOUNT_ID);
    }

    @Test
    void shouldCreateTransactionForWithdrawal() {
        Account account = createActiveAccount("100");
        Money amount = new Money(new BigDecimal("25"), "USD");

        Transaction transaction = account.recordWithdrawal(amount, UUID.randomUUID());

        assertThat(transaction.type()).isEqualTo(TransactionType.WITHDRAWAL);
        assertThat(transaction.amount()).isEqualTo(amount);
        assertThat(transaction.sourceAccountId()).isEqualTo(ACCOUNT_ID);
        assertThat(transaction.destinationAccountId()).isNull();
    }

    private Account createActiveAccount(String balance) {
        return createAccount(balance, AccountStatus.ACTIVE);
    }

    private Account createAccount(String balance, AccountStatus status) {
        return new Account(
                ACCOUNT_ID,
                ACCOUNT_NUMBER,
                new Money(new BigDecimal(balance), "USD"),
                OWNER_ID,
                status
        );
    }
}
