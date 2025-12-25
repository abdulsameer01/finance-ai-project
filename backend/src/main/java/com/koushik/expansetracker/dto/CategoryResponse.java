package com.koushik.expansetracker.dto;

import com.koushik.expansetracker.entity.finance.enums.TransactionType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

    private Long categoryId;
    private String categoryName;
    private TransactionType type;
}
