package com.bank.core.infrastructure.persistence.repository;

import com.bank.core.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repositorio Spring Data JPA para TransactionEntity.
 */
@Repository
public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, UUID> {
}
