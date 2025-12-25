package com.koushik.expansetracker.repository.finance;

import com.koushik.expansetracker.entity.finance.RecurringTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface RecurringTransactionRepository extends JpaRepository<RecurringTransaction, Long> {

    List<RecurringTransaction> findByUserId(Long userId);

    List<RecurringTransaction> findByNextRunBefore(Timestamp timestamp);
    List<RecurringTransaction> findByUserIdAndIsActiveTrue(Long userId);
}
