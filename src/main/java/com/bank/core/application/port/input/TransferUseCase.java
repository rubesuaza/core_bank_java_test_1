package com.bank.core.application.port.input;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Puerto de entrada para realizar transferencias entre cuentas.
 */
public interface TransferUseCase {

    /**
     * Transfiere dinero de una cuenta a otra.
     *
     * @param fromAccountId cuenta origen
     * @param toAccountId   cuenta destino
     * @param amount        monto a transferir
     */
    void transfer(UUID fromAccountId, UUID toAccountId, BigDecimal amount);
}
