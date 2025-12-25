package com.koushik.expansetracker.dto;

import com.koushik.expansetracker.entity.finance.enums.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequest {

    @NotBlank(message = "Account name is required")
    @Size(max = 100, message = "Account name cannot exceed 100 characters")
    private String accountName;

    @NotNull(message = "Account type is required")
    private AccountType accountType;

    // Optional: initial balance
    private BigDecimal balance;

    @NotBlank(message = "Currency is required")
    @Size(max = 10, message = "Currency cannot exceed 10 characters")
    private String currency;
}
