package com.bank.core.infrastructure.mapper;

import com.bank.core.domain.model.Account;
import com.bank.core.domain.model.AccountStatus;
import com.bank.core.domain.model.Money;
import com.bank.core.infrastructure.persistence.entity.AccountEntity;
import org.springframework.stereotype.Component;

/**
 * Mapea entre Account (dominio) y AccountEntity (infraestructura).
 */
@Component
public class AccountMapper {

    public AccountEntity toEntity(Account account) {
        return AccountEntity.builder()
                .id(account.getAccountId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance().getAmount())
                .currency(account.getBalance().getCurrency())
                .status(account.getStatus().name())
                .ownerId(account.getOwnerId())
                .build();
    }

    public Account toDomain(AccountEntity entity) {
        Money balance = new Money(entity.getBalance(), entity.getCurrency());
        return new Account(
                entity.getId(),
                entity.getAccountNumber(),
                balance,
                entity.getOwnerId(),
                AccountStatus.valueOf(entity.getStatus())
        );
    }
}
