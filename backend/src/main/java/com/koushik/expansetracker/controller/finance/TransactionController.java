package com.koushik.expansetracker.controller.finance;

import com.koushik.expansetracker.dto.TransactionRequest;
import com.koushik.expansetracker.dto.TransactionResponse;
import com.koushik.expansetracker.entity.finance.Tag;
import com.koushik.expansetracker.entity.finance.Transaction;
import com.koushik.expansetracker.entity.finance.TransactionTag;
import com.koushik.expansetracker.mapper.FinanceMapper;
import com.koushik.expansetracker.security.CustomUserDetails;
import com.koushik.expansetracker.repository.finance.TagRepository;
import com.koushik.expansetracker.repository.finance.TransactionTagRepository;
import com.koushik.expansetracker.service.finance.interfaces.TransactionServiceInterface;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionServiceInterface transactionService;
    private final FinanceMapper mapper;
    private final TransactionTagRepository transactionTagRepository;
    private final TagRepository tagRepository;

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody TransactionRequest request
    ) {
        Long userId = currentUser.getUser().getUserId();

        Transaction saved = transactionService.createTransaction(
                mapper.toTransactionEntity(request, userId),
                request.getTags()
        );

        return ResponseEntity.ok(
                mapper.toTransactionResponse(saved, getTagNames(saved.getTransactionId()))
        );
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @PathVariable Long transactionId,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody TransactionRequest request
    ) {
        Long userId = currentUser.getUser().getUserId();

        Transaction updated = transactionService.updateTransaction(
                transactionId,
                mapper.toTransactionEntity(request, userId),
                request.getTags()
        );

        return ResponseEntity.ok(
                mapper.toTransactionResponse(updated, getTagNames(updated.getTransactionId()))
        );
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getUserTransactions(
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        Long userId = currentUser.getUser().getUserId();

        var list = transactionService.getTransactionsForUser(userId)
                .stream()
                .map(t -> mapper.toTransactionResponse(t, getTagNames(t.getTransactionId())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @GetMapping("/range")
    public ResponseEntity<List<TransactionResponse>> getByDateRange(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam("start") LocalDate start,
            @RequestParam("end") LocalDate end
    ) {
        Long userId = currentUser.getUser().getUserId();

        Timestamp startTs = Timestamp.valueOf(LocalDateTime.of(start, LocalDateTime.MIN.toLocalTime()));
        Timestamp endTs = Timestamp.valueOf(LocalDateTime.of(end, LocalDateTime.MAX.toLocalTime()));

        var list = transactionService.getTransactionsForUserInRange(userId, startTs, endTs)
                .stream().map(t -> mapper.toTransactionResponse(t, getTagNames(t.getTransactionId())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransactionById(
            @PathVariable Long transactionId
    ) {
        Transaction t = transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(mapper.toTransactionResponse(t, getTagNames(t.getTransactionId())));
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long transactionId) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.ok("Transaction deleted successfully.");
    }

    private List<String> getTagNames(Long transactionId) {
        return transactionTagRepository.findByTransactionId(transactionId)
                .stream()
                .map(TransactionTag::getTagId)
                .map(tagRepository::findById)
                .filter(Optional::isPresent)
                .map(o -> o.get().getTagName())
                .collect(Collectors.toList());
    }
}
