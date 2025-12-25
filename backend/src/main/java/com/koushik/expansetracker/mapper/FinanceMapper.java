package com.koushik.expansetracker.mapper;

import com.koushik.expansetracker.dto.*;
import com.koushik.expansetracker.entity.finance.*;
import com.koushik.expansetracker.entity.finance.enums.TransactionType;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;


import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FinanceMapper {

    // ------------------ Account ------------------ //

    public Account toAccountEntity(AccountRequest dto, Long userId) {
        return Account.builder()
                .accountName(dto.getAccountName())
                .accountType(dto.getAccountType())
                .balance(dto.getBalance())
                .currency(dto.getCurrency())
                .userId(userId)
                .build();
    }

    public AccountResponse toAccountResponse(Account account) {
        return AccountResponse.builder()
                .accountId(account.getAccountId())
                .accountName(account.getAccountName())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .build();
    }

    // ------------------ Category ------------------ //

    public Category toCategoryEntity(CategoryRequest dto, Long userId) {
        return Category.builder()
                .categoryName(dto.getCategoryName())
                .type(dto.getType())
                .userId(userId)
                .build();
    }

    public CategoryResponse toCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .type(category.getType())
                .build();
    }

    // ------------------ Transaction ------------------ //

    public Transaction toTransactionEntity(TransactionRequest dto, Long userId) {
        return Transaction.builder()
                .userId(userId)
                .accountId(dto.getAccountId())
                .categoryId(dto.getCategoryId())
                .amount(dto.getAmount())
                .type(dto.getType())
                .description(dto.getDescription())
                .transactionDate(dto.getTransactionDate() != null
                        ? dto.getTransactionDate()
                        : Timestamp.from(Instant.now()))
                .build();
    }

    public TransactionResponse toTransactionResponse(Transaction tx, List<String> tags) {
        return TransactionResponse.builder()
                .transactionId(tx.getTransactionId())
                .accountId(tx.getAccountId())
                .categoryId(tx.getCategoryId())
                .type(tx.getType())
                .amount(tx.getAmount())
                .description(tx.getDescription())
                .transactionDate(tx.getTransactionDate())
                .createdAt(tx.getCreatedAt())
                .tags(tags)
                .build();
    }

    // ------------------ Budget ------------------ //

    public Budget toBudgetEntity(BudgetRequest req, Long userId) {
        return Budget.builder()
                .userId(userId)
                .categoryId(req.getCategoryId())
                .amountLimit(req.getAmountLimit())
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .build();
    }

    public BudgetResponse toBudgetResponse(Budget b, BigDecimal spent) {
        return BudgetResponse.builder()
                .budgetId(b.getBudgetId())
                .categoryId(b.getCategoryId())
                .amountLimit(b.getAmountLimit())
                .spent(spent)
                .startDate(b.getStartDate())
                .endDate(b.getEndDate())
                .build();
    }


    // ------------------ Savings Goal ------------------ //

    public SavingsGoal toSavingsGoalEntity(SavingsGoalRequest dto, Long userId) {
        return SavingsGoal.builder()
                .userId(userId)
                .goalName(dto.getGoalName())
                .targetAmount(dto.getTargetAmount())
                .deadline(dto.getDeadline())
                .build();
    }

    public SavingsGoalResponse toSavingsGoalResponse(SavingsGoal goal) {
        return SavingsGoalResponse.builder()
                .goalId(goal.getGoalId())
                .goalName(goal.getGoalName())
                .targetAmount(goal.getTargetAmount())
                .currentAmount(goal.getCurrentAmount())
                .deadline(goal.getDeadline())
                .build();
    }

    // ------------------ Bill Reminder ------------------ //

    public BillReminder toBillReminderEntity(BillReminderRequest dto, Long userId) {
        return BillReminder.builder()
                .userId(userId)
                .billName(dto.getBillName())
                .amountDue(dto.getAmountDue())
                .dueDate(dto.getDueDate())
                .build();
    }

    public BillReminderResponse toBillReminderResponse(BillReminder reminder) {
        return BillReminderResponse.builder()
                .reminderId(reminder.getReminderId())
                .billName(reminder.getBillName())
                .amountDue(reminder.getAmountDue())
                .dueDate(reminder.getDueDate())
                .status(reminder.getStatus())
                .build();
    }

    // ------------------ Recurring Transactions ------------------ //

    public RecurringTransaction toRecurringEntity(RecurringTransactionRequest dto, Long userId) {
        return RecurringTransaction.builder()
                .userId(userId)
                .accountId(dto.getAccountId())
                .categoryId(dto.getCategoryId())
                .amount(dto.getAmount())
                .frequency(dto.getFrequency())
                .nextRun(dto.getNextRun())
                .build();
    }

    public RecurringTransactionResponse toRecurringResponse(RecurringTransaction recurring) {
        return RecurringTransactionResponse.builder()
                .recurringId(recurring.getRecurringId())
                .accountId(recurring.getAccountId())
                .categoryId(recurring.getCategoryId())
                .amount(recurring.getAmount())
                .frequency(recurring.getFrequency())
                .nextRun(recurring.getNextRun())
                .build();
    }
}
