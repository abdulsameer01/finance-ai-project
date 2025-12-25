package com.koushik.expansetracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillReminderRequest {

    @NotBlank(message = "Bill name is required")
    private String billName;

    @NotNull(message = "Amount due is required")
    @Positive(message = "Amount due must be greater than zero")
    private BigDecimal amountDue;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;
}
