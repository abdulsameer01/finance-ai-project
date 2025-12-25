package com.koushik.expansetracker.entity.finance;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "savings_goals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavingsGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goalId;

    private Long userId;

    private String goalName;
    private BigDecimal targetAmount;
    private BigDecimal currentAmount = BigDecimal.ZERO;

    private LocalDate deadline;
}
