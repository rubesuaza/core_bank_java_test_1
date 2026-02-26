package com.bank.core.infrastructure.web.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateAccountResponse {

    private String accountId;
    private String accountNumber;
    private String status;
    private BigDecimal balance;
}

