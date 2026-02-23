package com.bank.core.application.port.input;

import com.bank.core.domain.model.Money;

import java.util.UUID;

/**
 * Puerto de entrada para consultar el saldo de una cuenta.
 */
public interface GetBalanceUseCase {

    /**
     * Obtiene el saldo de la cuenta.
     *
     * @param accountId identificador de la cuenta
     * @return el saldo (Money) de la cuenta
     */
    Money getBalance(UUID accountId);
}
