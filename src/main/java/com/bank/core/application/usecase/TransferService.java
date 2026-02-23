package com.bank.core.application.usecase;

import com.bank.core.application.port.input.TransferUseCase;
import com.bank.core.application.port.output.AccountRepository;
import com.bank.core.application.port.output.TransactionRepository;
import com.bank.core.domain.exception.AccountNotFoundException;
import com.bank.core.domain.model.Account;
import com.bank.core.domain.model.Money;
import com.bank.core.domain.model.Transaction;
import com.bank.core.domain.model.TransactionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Caso de uso: transferir dinero entre cuentas.
 */
@Service
public class TransferService implements TransferUseCase {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransferService(AccountRepository accountRepository,
                          TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public void transfer(UUID fromAccountId, UUID toAccountId, BigDecimal amount) {
        Account source = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new AccountNotFoundException(fromAccountId));
        Account destination = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new AccountNotFoundException(toAccountId));

        Money transferAmount = new Money(amount, source.getBalance().getCurrency());
        source.transferTo(destination, transferAmount);

        accountRepository.save(source);
        accountRepository.save(destination);

        Transaction transaction = new Transaction(
                UUID.randomUUID(),
                fromAccountId,
                toAccountId,
                transferAmount,
                TransactionType.TRANSFER,
                LocalDateTime.now()
        );
        transactionRepository.save(transaction);
    }
}
