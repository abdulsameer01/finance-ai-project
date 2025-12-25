package com.koushik.expansetracker.service.finance.implementations;

import com.koushik.expansetracker.dto.UpcomingRecurringPaymentResponse;
import com.koushik.expansetracker.entity.finance.RecurringTransaction;
import com.koushik.expansetracker.entity.finance.Transaction;
import com.koushik.expansetracker.entity.finance.enums.TransactionType;
import com.koushik.expansetracker.repository.finance.RecurringTransactionRepository;
import com.koushik.expansetracker.repository.finance.TransactionRepository;
import com.koushik.expansetracker.security.OwnershipValidator;
import com.koushik.expansetracker.service.finance.interfaces.RecurringTransactionServiceInterface;
import com.koushik.expansetracker.util.SecurityUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecurringTransactionService implements RecurringTransactionServiceInterface {

    private final RecurringTransactionRepository recurringTransactionRepository;
    private final TransactionRepository transactionRepository;
    private final OwnershipValidator ownershipValidator;

    public RecurringTransactionService(RecurringTransactionRepository recurringTransactionRepository,
                                       TransactionRepository transactionRepository,
                                       OwnershipValidator ownershipValidator) {
        this.recurringTransactionRepository = recurringTransactionRepository;
        this.transactionRepository = transactionRepository;
        this.ownershipValidator = ownershipValidator;
    }

    @Override
    public RecurringTransaction createRule(RecurringTransaction rule) {
        if (rule.getNextRun() == null) {
            rule.setNextRun(Timestamp.from(Instant.now()));
        }
        return recurringTransactionRepository.save(rule);
    }

    @Override
    public List<RecurringTransaction> getRulesForUser(Long userId) {
        return recurringTransactionRepository.findByUserId(userId);
    }

    @Override
    public RecurringTransaction getRuleById(Long recurringId) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        ownershipValidator.validateRecurringRule(recurringId, currentUserId);

        return recurringTransactionRepository.findById(recurringId)
                .orElseThrow(() -> new RuntimeException("Recurring rule not found"));
    }

    @Override
    public RecurringTransaction updateRule(Long recurringId, RecurringTransaction updated) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        ownershipValidator.validateRecurringRule(recurringId, currentUserId);

        RecurringTransaction existing = recurringTransactionRepository.findById(recurringId)
                .orElseThrow(() -> new RuntimeException("Recurring rule not found"));

        existing.setAccountId(updated.getAccountId());
        existing.setCategoryId(updated.getCategoryId());
        existing.setAmount(updated.getAmount());
        existing.setFrequency(updated.getFrequency());
        existing.setNextRun(updated.getNextRun());

        return recurringTransactionRepository.save(existing);
    }

    @Override
    public void deleteRule(Long recurringId) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        ownershipValidator.validateRecurringRule(recurringId, currentUserId);
        recurringTransactionRepository.deleteById(recurringId);
    }

    @Override
    @Scheduled(cron = "0 0 * * * *") // Every hour; you can adjust
    public void processDueRecurringTransactions() {
        Timestamp now = Timestamp.from(Instant.now());
        List<RecurringTransaction> dueRules =
                recurringTransactionRepository.findByNextRunBefore(now)
                        .stream()
                        .filter(RecurringTransaction::isActive)
                        .toList();


        for (RecurringTransaction rule : dueRules) {
            // Create a real transaction based on rule
            Transaction tx = Transaction.builder()
                    .userId(rule.getUserId())
                    .accountId(rule.getAccountId())
                    .categoryId(rule.getCategoryId())
                    .amount(rule.getAmount())
                    .type(TransactionType.EXPENSE) // Or maybe from rule if needed
                    .description("Recurring: " + rule.getFrequency())
                    .transactionDate(now)
                    .build();

            transactionRepository.save(tx);

            // Move nextRun forward based on frequency
            Instant nextRunInstant = rule.getNextRun().toInstant();

            switch (rule.getFrequency()) {
                case DAILY -> nextRunInstant = nextRunInstant.plus(1, ChronoUnit.DAYS);
                case WEEKLY -> nextRunInstant = nextRunInstant.plus(1, ChronoUnit.WEEKS);
                case MONTHLY -> nextRunInstant = nextRunInstant.plus(30, ChronoUnit.DAYS);
                case YEARLY -> nextRunInstant = nextRunInstant.plus(365, ChronoUnit.DAYS);
            }

            rule.setNextRun(Timestamp.from(nextRunInstant));
            recurringTransactionRepository.save(rule);
        }
    }
    @Override
    public List<UpcomingRecurringPaymentResponse> getUpcomingPayments() {
        Long userId = SecurityUtil.getCurrentUserId();
        List<RecurringTransaction> rules = recurringTransactionRepository.findByUserId(userId);

        List<UpcomingRecurringPaymentResponse> response = new ArrayList<>();

        for (RecurringTransaction rule : rules) {
            response.add(
                    UpcomingRecurringPaymentResponse.builder()
                            .recurringId(rule.getRecurringId())
                            .description("Recurring: " + rule.getFrequency())
                            .amount(rule.getAmount())
                            .nextRun(rule.getNextRun().toLocalDateTime())
                            .frequency(rule.getFrequency().name())
                            .build()
            );
        }

        return response;
    }
    @Override
    public RecurringTransaction pauseRule(Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        ownershipValidator.validateRecurringRule(id, userId);

        RecurringTransaction rule = recurringTransactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rule not found"));

        rule.setActive(false);
        return recurringTransactionRepository.save(rule);
    }

    @Override
    public RecurringTransaction resumeRule(Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        ownershipValidator.validateRecurringRule(id, userId);

        RecurringTransaction rule = recurringTransactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rule not found"));

        rule.setActive(true);
        return recurringTransactionRepository.save(rule);
    }

}
