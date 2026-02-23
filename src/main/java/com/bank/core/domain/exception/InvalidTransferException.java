package com.bank.core.domain.exception;

/**
 * Excepción lanzada cuando se intenta transferir a la misma cuenta de origen.
 */
public class InvalidTransferException extends DomainException {

    public InvalidTransferException(String message) {
        super(message);
    }
}
