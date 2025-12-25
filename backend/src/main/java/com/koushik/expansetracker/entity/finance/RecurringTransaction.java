package com.koushik.expansetracker.entity.finance;

import com.koushik.expansetracker.entity.finance.enums.Frequency;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "recurring_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecurringTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recurringId;

    private Long userId;
    private Long accountId;
    private Long categoryId;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    private Timestamp nextRun;
    private boolean isActive = true;
}
