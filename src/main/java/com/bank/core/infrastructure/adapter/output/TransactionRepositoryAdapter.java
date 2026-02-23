package com.bank.core.infrastructure.adapter.output;

import com.bank.core.application.port.output.TransactionRepository;
import com.bank.core.domain.model.Transaction;
import com.bank.core.infrastructure.mapper.TransactionMapper;
import com.bank.core.infrastructure.persistence.repository.TransactionJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Adaptador de salida que implementa TransactionRepository usando JPA.
 */
@Component
public class TransactionRepositoryAdapter implements TransactionRepository {

    private final TransactionJpaRepository jpaRepository;
    private final TransactionMapper mapper;

    public TransactionRepositoryAdapter(TransactionJpaRepository jpaRepository, TransactionMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Transaction save(Transaction transaction) {
        var entity = mapper.toEntity(transaction);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
}
