package com.koushik.expansetracker.controller.finance;

import com.koushik.expansetracker.dto.RecurringTransactionRequest;
import com.koushik.expansetracker.dto.RecurringTransactionResponse;
import com.koushik.expansetracker.dto.UpcomingRecurringPaymentResponse;
import com.koushik.expansetracker.entity.finance.RecurringTransaction;
import com.koushik.expansetracker.mapper.FinanceMapper;
import com.koushik.expansetracker.security.CustomUserDetails;
import com.koushik.expansetracker.service.finance.interfaces.RecurringTransactionServiceInterface;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recurring")
@RequiredArgsConstructor
public class RecurringTransactionController {

    private final RecurringTransactionServiceInterface service;
    private final FinanceMapper mapper;

    @PostMapping
    public ResponseEntity<RecurringTransactionResponse> createRule(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody RecurringTransactionRequest request
    ) {
        Long userId = currentUser.getUser().getUserId();
        RecurringTransaction saved = service.createRule(mapper.toRecurringEntity(request, userId));
        return ResponseEntity.ok(mapper.toRecurringResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<RecurringTransactionResponse>> getUserRules(
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        Long userId = currentUser.getUser().getUserId();

        var list = service.getRulesForUser(userId)
                .stream()
                .map(mapper::toRecurringResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{recurringId}")
    public ResponseEntity<RecurringTransactionResponse> getRuleById(
            @PathVariable Long recurringId
    ) {
        RecurringTransaction rule = service.getRuleById(recurringId);
        return ResponseEntity.ok(mapper.toRecurringResponse(rule));
    }

    @PutMapping("/{recurringId}")
    public ResponseEntity<RecurringTransactionResponse> updateRule(
            @PathVariable Long recurringId,
            @Valid @RequestBody RecurringTransactionRequest request
    ) {
        RecurringTransaction updated = service.updateRule(recurringId, mapper.toRecurringEntity(request, null));
        return ResponseEntity.ok(mapper.toRecurringResponse(updated));
    }

    @DeleteMapping("/{recurringId}")
    public ResponseEntity<String> deleteRule(@PathVariable Long recurringId) {
        service.deleteRule(recurringId);
        return ResponseEntity.ok("Recurring rule deleted successfully.");
    }

    @PostMapping("/run-now")
    public ResponseEntity<String> runRecurringNow() {
        service.processDueRecurringTransactions();
        return ResponseEntity.ok("Recurring transactions processed.");
    }
    @GetMapping("/upcoming")
    public ResponseEntity<List<UpcomingRecurringPaymentResponse>> getUpcomingPayments() {
        return ResponseEntity.ok(
                service.getUpcomingPayments()
        );
    }
    @PatchMapping("/{id}/pause")
    public ResponseEntity<?> pauseRule(@PathVariable Long id) {
        return ResponseEntity.ok(service.pauseRule(id));
    }

    @PatchMapping("/{id}/resume")
    public ResponseEntity<?> resumeRule(@PathVariable Long id) {
        return ResponseEntity.ok(service.resumeRule(id));
    }

}
