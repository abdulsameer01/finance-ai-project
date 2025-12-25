package com.koushik.expansetracker.entity.finance;

import com.koushik.expansetracker.entity.finance.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    private Long userId;
    private Long accountId;
    private Long categoryId;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private BigDecimal amount;

    private String description;

    private Timestamp transactionDate;

    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());
}
