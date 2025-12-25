package com.koushik.expansetracker.dto;

import com.koushik.expansetracker.entity.finance.enums.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {

    private Long transactionId;
    private Long accountId;
    private Long categoryId;
    private TransactionType type;
    private BigDecimal amount;
    private String description;
    private Timestamp transactionDate;
    private Timestamp createdAt;
    private List<String> tags;
}
