package com.koushik.expansetracker.dto;

import com.koushik.expansetracker.entity.finance.enums.BillStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillReminderResponse {

    private Long reminderId;
    private String billName;
    private BigDecimal amountDue;
    private LocalDate dueDate;
    private BillStatus status;
}
