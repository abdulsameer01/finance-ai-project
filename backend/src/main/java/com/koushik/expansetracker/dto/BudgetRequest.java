package com.koushik.expansetracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BudgetRequest {

    @NotNull
    private Long categoryId;

    @NotNull
    private BigDecimal amountLimit;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}

