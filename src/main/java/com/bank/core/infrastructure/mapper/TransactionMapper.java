package com.bank.core.infrastructure.mapper;

import com.bank.core.domain.model.Money;
import com.bank.core.domain.model.Transaction;
import com.bank.core.domain.model.TransactionType;
import com.bank.core.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.stereotype.Component;

/**
 * Mapea entre Transaction (dominio) y TransactionEntity (infraestructura).
 */
@Component
public class TransactionMapper {

    public TransactionEntity toEntity(Transaction transaction) {
        return TransactionEntity.builder()
                .id(transaction.getTransactionId())
                .sourceAccountId(transaction.getSourceAccountId())
                .destinationAccountId(transaction.getDestinationAccountId())
                .amount(transaction.getAmount().getAmount())
                .currency(transaction.getAmount().getCurrency())
                .type(transaction.getType().name())
                .timestamp(transaction.getTimestamp())
                .build();
    }

    public Transaction toDomain(TransactionEntity entity) {
        Money amount = new Money(entity.getAmount(), entity.getCurrency());
        return new Transaction(
                entity.getId(),
                entity.getSourceAccountId(),
                entity.getDestinationAccountId(),
                amount,
                TransactionType.valueOf(entity.getType()),
                entity.getTimestamp()
        );
    }
}
