package com.koushik.expansetracker.service.finance.implementations;

import com.koushik.expansetracker.entity.finance.*;
import com.koushik.expansetracker.entity.finance.enums.TransactionType;
import com.koushik.expansetracker.repository.finance.*;
import com.koushik.expansetracker.security.OwnershipValidator;
import com.koushik.expansetracker.service.finance.interfaces.TransactionServiceInterface;
import com.koushik.expansetracker.util.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class TransactionService implements TransactionServiceInterface {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TagRepository tagRepository;
    private final TransactionTagRepository transactionTagRepository;
    private final OwnershipValidator ownershipValidator;

    public TransactionService(
            TransactionRepository transactionRepository,
            AccountRepository accountRepository,
            TagRepository tagRepository,
            TransactionTagRepository transactionTagRepository,
            OwnershipValidator ownershipValidator
    ) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.tagRepository = tagRepository;
        this.transactionTagRepository = transactionTagRepository;
        this.ownershipValidator = ownershipValidator;
    }

    /* ================= CREATE ================= */

    @Override
    @Transactional
    public Transaction createTransaction(Transaction transaction, List<String> tagNames) {

        if (transaction.getTransactionDate() == null) {
            transaction.setTransactionDate(Timestamp.from(Instant.now()));
        }

        Long userId = SecurityUtil.getCurrentUserId();

        // 🔐 OWNERSHIP CHECK
        ownershipValidator.validateAccount(transaction.getAccountId(), userId);

        Account account = accountRepository.findById(transaction.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        BigDecimal amount = transaction.getAmount().abs();

        if (transaction.getType() == TransactionType.EXPENSE) {
            account.setBalance(account.getBalance().subtract(amount));
            transaction.setAmount(amount.negate());
        } else {
            account.setBalance(account.getBalance().add(amount));
            transaction.setAmount(amount);
        }

        accountRepository.save(account);

        Transaction saved = transactionRepository.save(transaction);

        saveTags(saved.getTransactionId(), tagNames);
        return saved;
    }

    /* ================= UPDATE ================= */

    @Override
    @Transactional
    public Transaction updateTransaction(Long transactionId, Transaction updated, List<String> tagNames) {

        Long userId = SecurityUtil.getCurrentUserId();
        ownershipValidator.validateTransaction(transactionId, userId);
        ownershipValidator.validateAccount(updated.getAccountId(), userId);

        Transaction existing = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // 🔁 REVERSE OLD ACCOUNT BALANCE
        Account oldAccount = accountRepository.findById(existing.getAccountId())
                .orElseThrow();

        BigDecimal oldAmount = existing.getAmount().abs();

        if (existing.getType() == TransactionType.EXPENSE) {
            oldAccount.setBalance(oldAccount.getBalance().add(oldAmount));
        } else {
            oldAccount.setBalance(oldAccount.getBalance().subtract(oldAmount));
        }

        accountRepository.save(oldAccount);

        // ➕ APPLY NEW ACCOUNT BALANCE
        Account newAccount = accountRepository.findById(updated.getAccountId())
                .orElseThrow();

        BigDecimal newAmount = updated.getAmount().abs();

        if (updated.getType() == TransactionType.EXPENSE) {
            newAccount.setBalance(newAccount.getBalance().subtract(newAmount));
            updated.setAmount(newAmount.negate());
        } else {
            newAccount.setBalance(newAccount.getBalance().add(newAmount));
            updated.setAmount(newAmount);
        }

        accountRepository.save(newAccount);

        // 🔄 UPDATE TRANSACTION FIELDS
        existing.setAccountId(updated.getAccountId());
        existing.setCategoryId(updated.getCategoryId());
        existing.setAmount(updated.getAmount());
        existing.setType(updated.getType());
        existing.setDescription(updated.getDescription());
        existing.setTransactionDate(updated.getTransactionDate());

        Transaction saved = transactionRepository.save(existing);

        transactionTagRepository.deleteByTransactionId(transactionId);
        saveTags(transactionId, tagNames);

        return saved;
    }

    /* ================= DELETE ================= */

    @Override
    @Transactional
    public void deleteTransaction(Long transactionId) {

        Long userId = SecurityUtil.getCurrentUserId();
        ownershipValidator.validateTransaction(transactionId, userId);

        Transaction tx = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        ownershipValidator.validateAccount(tx.getAccountId(), userId);

        Account account = accountRepository.findById(tx.getAccountId())
                .orElseThrow();

        BigDecimal amount = tx.getAmount().abs();

        // 🔄 REVERSE BALANCE
        if (tx.getType() == TransactionType.EXPENSE) {
            account.setBalance(account.getBalance().add(amount));
        } else {
            account.setBalance(account.getBalance().subtract(amount));
        }

        accountRepository.save(account);

        transactionTagRepository.deleteByTransactionId(transactionId);
        transactionRepository.deleteById(transactionId);
    }

    /* ================= READ ================= */

    @Override
    public Transaction getTransactionById(Long transactionId) {
        Long userId = SecurityUtil.getCurrentUserId();
        ownershipValidator.validateTransaction(transactionId, userId);

        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    @Override
    public List<Transaction> getTransactionsForUser(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    @Override
    public List<Transaction> getTransactionsForUserInRange(Long userId, Timestamp start, Timestamp end) {
        return transactionRepository.findByUserIdAndTransactionDateBetween(userId, start, end);
    }

    /* ================= TAGS ================= */

    private void saveTags(Long transactionId, List<String> tagNames) {

        if (tagNames == null || tagNames.isEmpty()) return;

        tagNames.stream().distinct().forEach(name -> {

            Tag tag = tagRepository.findByTagName(name)
                    .orElseGet(() -> tagRepository.save(
                            Tag.builder().tagName(name).build()
                    ));

            transactionTagRepository.save(
                    TransactionTag.builder()
                            .transactionId(transactionId)
                            .tagId(tag.getTagId())
                            .build()
            );
        });
    }
}
