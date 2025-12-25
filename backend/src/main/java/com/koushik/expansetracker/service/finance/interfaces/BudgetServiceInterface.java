package com.koushik.expansetracker.service.finance.interfaces;

import com.koushik.expansetracker.entity.finance.Budget;

import java.math.BigDecimal;
import java.util.List;

public interface BudgetServiceInterface {

    Budget createBudget(Budget budget);

    List<Budget> getBudgetsForUser(Long userId);

    Budget getBudgetById(Long budgetId);

    Budget updateBudget(Long budgetId, Budget updated);

    void deleteBudget(Long budgetId);

    BigDecimal calculateSpentForBudget(Long budgetId);
}
