package com.bank.core.infrastructure.adapter.output;

import com.bank.core.application.port.output.AccountNumberGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Generador de números de cuenta basado en UUID (8 primeros caracteres numéricos).
 */
@Component
public class UuidAccountNumberGenerator implements AccountNumberGenerator {

    @Override
    public String generate() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid.substring(0, 8).toUpperCase();
    }
}
