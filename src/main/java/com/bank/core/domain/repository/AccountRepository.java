package com.bank.core.domain.repository;

import com.bank.core.domain.model.Account;

import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de persistencia para la entidad Account.
 */
public interface AccountRepository {

    Account save(Account account);

    Optional<Account> findById(UUID accountId);

    Optional<Account> findByAccountNumber(String accountNumber);

    boolean existsByAccountNumber(String accountNumber);
}
