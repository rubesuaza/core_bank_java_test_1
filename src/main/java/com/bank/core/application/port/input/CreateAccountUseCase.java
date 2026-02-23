package com.bank.core.application.port.input;

import com.bank.core.domain.model.Account;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Puerto de entrada para crear una cuenta bancaria.
 */
public interface CreateAccountUseCase {

    /**
     * Crea una cuenta con el saldo inicial indicado.
     *
     * @param ownerId        identificador del dueño
     * @param initialBalance saldo inicial
     * @param currency       código de moneda ISO
     * @return la cuenta creada
     */
    Account create(UUID ownerId, BigDecimal initialBalance, String currency);
}
