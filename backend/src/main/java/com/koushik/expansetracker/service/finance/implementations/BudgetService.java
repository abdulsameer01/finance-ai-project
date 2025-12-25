package com.koushik.expansetracker.service.finance.implementations;

import com.koushik.expansetracker.entity.finance.Budget;
import com.koushik.expansetracker.entity.finance.Transaction;
import com.koushik.expansetracker.entity.finance.enums.TransactionType;
import com.koushik.expansetracker.repository.finance.BudgetRepository;
import com.koushik.expansetracker.repository.finance.TransactionRepository;
import com.koushik.expansetracker.security.OwnershipValidator;
import com.koushik.expansetracker.service.finance.interfaces.BudgetServiceInterface;
import com.koushik.expansetracker.util.SecurityUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BudgetService implements BudgetServiceInterface {

    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;
    private final OwnershipValidator ownershipValidator;

    public BudgetService(
            BudgetRepository budgetRepository,
            TransactionRepository transactionRepository,
            OwnershipValidator ownershipValidator
    ) {
        this.budgetRepository = budgetRepository;
        this.transactionRepository = transactionRepository;
        this.ownershipValidator = ownershipValidator;
    }

    @Override
    public Budget createBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    @Override
    public List<Budget> getBudgetsForUser(Long userId) {
        return budgetRepository.findByUserId(userId);
    }

    @Override
    public Budget getBudgetById(Long budgetId) {
        Long userId = SecurityUtil.getCurrentUserId();
        ownershipValidator.validateBudget(budgetId, userId);

        return budgetRepository.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Budget not found"));
    }

    @Override
    public Budget updateBudget(Long budgetId, Budget updated) {
        Long userId = SecurityUtil.getCurrentUserId();
        ownershipValidator.validateBudget(budgetId, userId);

        Budget existing = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        existing.setCategoryId(updated.getCategoryId());
        existing.setAmountLimit(updated.getAmountLimit());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());

        return budgetRepository.save(existing);
    }

    @Override
    public void deleteBudget(Long budgetId) {
        Long userId = SecurityUtil.getCurrentUserId();
        ownershipValidator.validateBudget(budgetId, userId);
        budgetRepository.deleteById(budgetId);
    }

    @Override
    public BigDecimal calculateSpentForBudget(Long budgetId) {

        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        Timestamp startTs = Timestamp.valueOf(
                budget.getStartDate().atStartOfDay()
        );
        Timestamp endTs = Timestamp.valueOf(
                budget.getEndDate().atTime(23, 59, 59)
        );

        List<Transaction> txs = transactionRepository
                .findByUserIdAndCategoryIdAndTransactionDateBetweenAndType(
                        budget.getUserId(),
                        budget.getCategoryId(),
                        startTs,
                        endTs,
                        TransactionType.EXPENSE
                );

        // 🔥 FIX: EXPENSE AMOUNTS ARE NEGATIVE
        return txs.stream()
                .map(tx -> tx.getAmount().abs())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }
}