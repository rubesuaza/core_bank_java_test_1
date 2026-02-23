package com.bank.core.application.port.output;

import com.bank.core.domain.model.Transaction;

/**
 * Puerto de salida para persistencia de transacciones.
 */
public interface TransactionRepository {

    Transaction save(Transaction transaction);
}
