package com.bank.core.infrastructure.adapter.output;

import com.bank.core.application.port.output.AccountRepository;
import com.bank.core.domain.model.Account;
import com.bank.core.infrastructure.mapper.AccountMapper;
import com.bank.core.infrastructure.persistence.repository.AccountJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador de salida que implementa AccountRepository usando JPA.
 */
@Component
public class AccountRepositoryAdapter implements AccountRepository {

    private final AccountJpaRepository jpaRepository;
    private final AccountMapper mapper;

    public AccountRepositoryAdapter(AccountJpaRepository jpaRepository, AccountMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Account save(Account account) {
        var entity = mapper.toEntity(account);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Account> findById(UUID accountId) {
        return jpaRepository.findById(accountId)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return jpaRepository.findByAccountNumber(accountNumber)
                .map(mapper::toDomain);
    }
}
