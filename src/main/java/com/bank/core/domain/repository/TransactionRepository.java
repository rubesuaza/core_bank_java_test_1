package com.bank.core.domain.repository;

import com.bank.core.domain.model.Transaction;

/**
 * Puerto de persistencia para la entidad Transaction.
 */
public interface TransactionRepository {

    Transaction save(Transaction transaction);
}
