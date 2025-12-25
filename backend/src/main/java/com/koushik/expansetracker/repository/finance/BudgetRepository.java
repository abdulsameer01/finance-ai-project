package com.koushik.expansetracker.repository.finance;

import com.koushik.expansetracker.entity.finance.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByUserId(Long userId);
}
