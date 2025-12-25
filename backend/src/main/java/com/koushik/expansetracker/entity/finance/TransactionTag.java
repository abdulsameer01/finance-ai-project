package com.koushik.expansetracker.entity.finance;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "transaction_tags",
        uniqueConstraints = @UniqueConstraint(columnNames = {"transactionId","tagId"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long transactionId;
    private Long tagId;
}
