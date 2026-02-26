package com.bank.core.application.port.in;

import java.util.UUID;

/**
 * Puerto de entrada para consulta de saldo de cuenta.
 */
public interface GetAccountBalanceQuery {

    AccountBalanceView getBalance(UUID accountId);
}

