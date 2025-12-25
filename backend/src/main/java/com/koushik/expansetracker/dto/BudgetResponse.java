package com.koushik.expansetracker.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class BudgetResponse {

    private Long budgetId;
    private Long categoryId;

    private BigDecimal amountLimit;
    private BigDecimal spent;

    private LocalDate startDate;
    private LocalDate endDate;
}
