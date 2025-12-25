package com.koushik.expansetracker.entity.finance;

import com.koushik.expansetracker.entity.finance.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    private Long userId;

    private String categoryName;

    @Enumerated(EnumType.STRING)
    private TransactionType type;
}
