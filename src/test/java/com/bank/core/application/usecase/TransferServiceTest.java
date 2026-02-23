package com.bank.core.application.usecase;

import com.bank.core.application.port.output.AccountRepository;
import com.bank.core.application.port.output.TransactionRepository;
import com.bank.core.domain.exception.AccountNotFoundException;
import com.bank.core.domain.model.Account;
import com.bank.core.domain.model.AccountStatus;
import com.bank.core.domain.model.Money;
import com.bank.core.domain.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    private TransferService transferService;

    private UUID sourceAccountId;
    private UUID destinationAccountId;
    private Account sourceAccount;
    private Account destinationAccount;

    @BeforeEach
    void setUp() {
        transferService = new TransferService(accountRepository, transactionRepository);
        sourceAccountId = UUID.randomUUID();
        destinationAccountId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        sourceAccount = new Account(
                sourceAccountId,
                "ACC-SRC",
                new Money("200.00", "USD"),
                ownerId,
                AccountStatus.ACTIVE
        );
        destinationAccount = new Account(
                destinationAccountId,
                "ACC-DST",
                new Money("50.00", "USD"),
                ownerId,
                AccountStatus.ACTIVE
        );
    }

    @Test
    void shouldTransferMoneyBetweenAccountsAndSaveTransaction() {
        // Arrange
        BigDecimal amount = new BigDecimal("75.00");
        when(accountRepository.findById(sourceAccountId)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findById(destinationAccountId)).thenReturn(Optional.of(destinationAccount));
        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        transferService.transfer(sourceAccountId, destinationAccountId, amount);

        // Assert - accounts updated
        assertThat(sourceAccount.getBalance().getAmount()).isEqualByComparingTo("125.00");
        assertThat(destinationAccount.getBalance().getAmount()).isEqualByComparingTo("125.00");

        verify(accountRepository).save(sourceAccount);
        verify(accountRepository).save(destinationAccount);

        ArgumentCaptor<Transaction> txCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(txCaptor.capture());
        Transaction savedTx = txCaptor.getValue();
        assertThat(savedTx.getSourceAccountId()).isEqualTo(sourceAccountId);
        assertThat(savedTx.getDestinationAccountId()).isEqualTo(destinationAccountId);
        assertThat(savedTx.getAmount().getAmount()).isEqualByComparingTo("75.00");
    }

    @Test
    void shouldThrowAccountNotFoundExceptionWhenSourceAccountDoesNotExist() {
        when(accountRepository.findById(sourceAccountId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transferService.transfer(sourceAccountId, destinationAccountId, new BigDecimal("10")))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("Account not found");

        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void shouldThrowAccountNotFoundExceptionWhenDestinationAccountDoesNotExist() {
        when(accountRepository.findById(sourceAccountId)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findById(destinationAccountId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transferService.transfer(sourceAccountId, destinationAccountId, new BigDecimal("10")))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("Account not found");

        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }
}
