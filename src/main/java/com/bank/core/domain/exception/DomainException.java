package com.bank.core.domain.exception;

/**
 * Clase base para todas las excepciones de dominio.
 * Las reglas de negocio lanzan excepciones que heredan de esta clase.
 */
public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
