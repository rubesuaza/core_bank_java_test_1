package com.bank.core;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CoreBankApplicationTest {

    @Test
    void contextLoads() {
        assertThat(true).isTrue();
    }
}
