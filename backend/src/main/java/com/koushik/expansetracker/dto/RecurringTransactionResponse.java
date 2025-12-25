package com.koushik.expansetracker.dto;

import com.koushik.expansetracker.entity.finance.enums.Frequency;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecurringTransactionResponse {

    private Long recurringId;
    private Long accountId;
    private Long categoryId;
    private BigDecimal amount;
    private Frequency frequency;
    private Timestamp nextRun;
}
