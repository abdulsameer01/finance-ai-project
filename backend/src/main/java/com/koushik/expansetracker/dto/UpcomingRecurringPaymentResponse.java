package com.koushik.expansetracker.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpcomingRecurringPaymentResponse {
    private Long recurringId;
    private String description;
    private BigDecimal amount;
    private LocalDateTime nextRun;
    private String frequency;
}
