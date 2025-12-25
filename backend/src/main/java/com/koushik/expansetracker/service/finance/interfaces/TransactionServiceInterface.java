package com.koushik.expansetracker.service.finance.interfaces;

import com.koushik.expansetracker.entity.finance.Transaction;

import java.sql.Timestamp;
import java.util.List;

public interface TransactionServiceInterface {

    Transaction createTransaction(Transaction transaction, List<String> tagNames);

    Transaction updateTransaction(Long transactionId, Transaction updatedTransaction, List<String> tagNames);

    void deleteTransaction(Long transactionId);

    Transaction getTransactionById(Long transactionId);

    List<Transaction> getTransactionsForUser(Long userId);

    List<Transaction> getTransactionsForUserInRange(Long userId, Timestamp start, Timestamp end);
}
