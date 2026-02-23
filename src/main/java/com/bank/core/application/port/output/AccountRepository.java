package com.bank.core.application.port.output;

import com.bank.core.domain.model.Account;

import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida para persistencia de cuentas.
 */
public interface AccountRepository {

    Account save(Account account);

    Optional<Account> findById(UUID accountId);

    Optional<Account> findByAccountNumber(String accountNumber);
}
