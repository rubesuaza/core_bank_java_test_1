package com.bank.core.application.service;

import com.bank.core.application.port.in.TransferCommand;
import com.bank.core.application.port.in.TransferFundsUseCase;

/**
 * Implementación del caso de uso de transferencia de fondos.
 */
public class TransferFundsService implements TransferFundsUseCase {

    @Override
    public void transfer(TransferCommand command) {
        // La implementación real requiere acceso a las cuentas (por ejemplo, mediante un repositorio)
        // y coordinación con el servicio de dominio.
        // TODO(user): Inyectar puertos de salida para cargar cuentas por ID, invocar el AccountDomainService
        //             y persistir los cambios y la transacción resultante.
        throw new UnsupportedOperationException("TransferFundsService.transfer is not implemented yet");
    }
}

