package com.koushik.expansetracker.entity.finance;

import com.koushik.expansetracker.entity.finance.enums.BillStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "bill_reminders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillReminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reminderId;

    private Long userId;

    private String billName;
    private BigDecimal amountDue;

    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private BillStatus status = BillStatus.PENDING;
}
