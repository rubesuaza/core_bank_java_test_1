package com.bank.core.application.usecase;

import com.bank.core.application.port.output.AccountRepository;
import com.bank.core.domain.exception.AccountNotFoundException;
import com.bank.core.domain.model.Account;
import com.bank.core.domain.model.AccountStatus;
import com.bank.core.domain.model.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetBalanceServiceTest {

    @Mock
    private AccountRepository accountRepository;

    private GetBalanceService getBalanceService;

    @BeforeEach
    void setUp() {
        getBalanceService = new GetBalanceService(accountRepository);
    }

    @Test
    void shouldReturnBalanceWhenAccountExists() {
        // Arrange
        UUID accountId = UUID.randomUUID();
        Money expectedBalance = new Money(new BigDecimal("150.75"), "USD");
        Account account = new Account(
                accountId,
                "ACC-001",
                expectedBalance,
                UUID.randomUUID(),
                AccountStatus.ACTIVE
        );
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // Act
        Money result = getBalanceService.getBalance(accountId);

        // Assert
        assertThat(result).isEqualTo(expectedBalance);
        assertThat(result.getAmount()).isEqualByComparingTo("150.75");
        assertThat(result.getCurrency()).isEqualTo("USD");
        verify(accountRepository).findById(accountId);
    }

    @Test
    void shouldThrowAccountNotFoundExceptionWhenAccountDoesNotExist() {
        // Arrange
        UUID accountId = UUID.randomUUID();
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> getBalanceService.getBalance(accountId))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("Account not found")
                .hasMessageContaining(accountId.toString());

        verify(accountRepository).findById(accountId);
    }

    @Test
    void shouldReturnZeroBalanceWhenAccountHasZeroBalance() {
        // Arrange
        UUID accountId = UUID.randomUUID();
        Money zeroBalance = new Money(BigDecimal.ZERO, "EUR");
        Account account = new Account(
                accountId,
                "ACC-002",
                zeroBalance,
                UUID.randomUUID(),
                AccountStatus.ACTIVE
        );
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // Act
        Money result = getBalanceService.getBalance(accountId);

        // Assert
        assertThat(result.getAmount()).isEqualByComparingTo("0");
    }
}
