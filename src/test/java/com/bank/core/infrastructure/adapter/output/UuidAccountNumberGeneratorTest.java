package com.bank.core.infrastructure.adapter.output;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

class UuidAccountNumberGeneratorTest {

    private UuidAccountNumberGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new UuidAccountNumberGenerator();
    }

    @Test
    void shouldGenerateNonEmptyAccountNumber() {
        String result = generator.generate();
        assertThat(result).isNotBlank();
    }

    @Test
    void shouldGenerateAccountNumberWith8Characters() {
        String result = generator.generate();
        assertThat(result).hasSize(8);
    }

    @Test
    void shouldGenerateAccountNumberWithOnlyAlphanumericCharacters() {
        // UUID hex format: 0-9, A-F. After substring(0,8) and toUpperCase we get valid hex
        String result = generator.generate();
        assertThat(result).matches("^[0-9A-F]{8}$");
    }

    @RepeatedTest(20)
    void shouldGenerateUniqueAccountNumbers() {
        Set<String> generated = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            String number = generator.generate();
            assertThat(generated).doesNotContain(number);
            generated.add(number);
        }
    }
}
