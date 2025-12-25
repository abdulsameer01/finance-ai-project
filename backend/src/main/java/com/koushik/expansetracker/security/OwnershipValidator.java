package com.koushik.expansetracker.security;

import com.koushik.expansetracker.entity.finance.*;
import com.koushik.expansetracker.repository.finance.*;
import org.springframework.stereotype.Component;

@Component
public class OwnershipValidator {

    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;
    private final SavingsGoalRepository savingsGoalRepository;
    private final BillReminderRepository billReminderRepository;
    private final RecurringTransactionRepository recurringTransactionRepository;

    public OwnershipValidator(AccountRepository accountRepository,
                              CategoryRepository categoryRepository,
                              TransactionRepository transactionRepository,
                              BudgetRepository budgetRepository,
                              SavingsGoalRepository savingsGoalRepository,
                              BillReminderRepository billReminderRepository,
                              RecurringTransactionRepository recurringTransactionRepository) {
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
        this.budgetRepository = budgetRepository;
        this.savingsGoalRepository = savingsGoalRepository;
        this.billReminderRepository = billReminderRepository;
        this.recurringTransactionRepository = recurringTransactionRepository;
    }

    public void validateAccount(Long id, Long userId) {
        Account a = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        if (!a.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized account access");
        }
    }

    public void validateCategory(Long id, Long userId) {
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        if (!c.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized category access");
        }
    }

    public void validateTransaction(Long id, Long userId) {
        Transaction t = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        if (!t.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized transaction access");
        }
    }

    public void validateBudget(Long id, Long userId) {
        Budget b = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));
        if (!b.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized budget access");
        }
    }

    public void validateSavingsGoal(Long id, Long userId) {
        SavingsGoal g = savingsGoalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Savings goal not found"));
        if (!g.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized savings goal access");
        }
    }

    public void validateBillReminder(Long id, Long userId) {
        BillReminder r = billReminderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill reminder not found"));
        if (!r.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized bill reminder access");
        }
    }

    public void validateRecurringRule(Long id, Long userId) {
        RecurringTransaction rt = recurringTransactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurring rule not found"));
        if (!rt.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized recurring rule access");
        }
    }
}
