package com.bank.core.application.port.in;

/**
 * Puerto de entrada para ejecutar transferencias entre cuentas.
 */
public interface TransferFundsUseCase {

    void transfer(TransferCommand command);
}

