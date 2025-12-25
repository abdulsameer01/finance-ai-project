package com.koushik.expansetracker.repository.finance;

import com.koushik.expansetracker.entity.finance.BillReminder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BillReminderRepository extends JpaRepository<BillReminder, Long> {

    List<BillReminder> findByUserId(Long userId);

    List<BillReminder> findByDueDateBetween(LocalDate start, LocalDate end);
}
