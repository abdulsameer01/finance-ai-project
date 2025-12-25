package com.koushik.expansetracker.dto;

import com.koushik.expansetracker.entity.finance.enums.AccountType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {

    private Long accountId;
    private String accountName;
    private AccountType accountType;
    private BigDecimal balance;
    private String currency;
}
