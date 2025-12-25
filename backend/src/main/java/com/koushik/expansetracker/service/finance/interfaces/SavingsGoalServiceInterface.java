package com.koushik.expansetracker.service.finance.interfaces;

import com.koushik.expansetracker.entity.finance.SavingsGoal;

import java.math.BigDecimal;
import java.util.List;

public interface SavingsGoalServiceInterface {

    SavingsGoal createGoal(SavingsGoal goal);

    List<SavingsGoal> getGoalsForUser(Long userId);

    SavingsGoal transferFromAccount(Long goalId, Long accountId, BigDecimal amount);

    void deleteGoal(Long goalId);
}
