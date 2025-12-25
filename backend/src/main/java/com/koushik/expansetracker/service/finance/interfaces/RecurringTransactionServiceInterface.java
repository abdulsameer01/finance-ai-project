package com.koushik.expansetracker.service.finance.interfaces;

import com.koushik.expansetracker.dto.UpcomingRecurringPaymentResponse;
import com.koushik.expansetracker.entity.finance.RecurringTransaction;

import java.sql.Timestamp;
import java.util.List;

public interface RecurringTransactionServiceInterface {

    RecurringTransaction createRule(RecurringTransaction rule);

    List<RecurringTransaction> getRulesForUser(Long userId);

    RecurringTransaction getRuleById(Long recurringId);

    RecurringTransaction updateRule(Long recurringId, RecurringTransaction updated);

    void deleteRule(Long recurringId);

    void processDueRecurringTransactions();
    List<UpcomingRecurringPaymentResponse> getUpcomingPayments();

    RecurringTransaction pauseRule(Long id);

    RecurringTransaction resumeRule(Long id);
}
