package com.koushik.expansetracker.controller.finance;

import com.koushik.expansetracker.dto.BudgetRequest;
import com.koushik.expansetracker.dto.BudgetResponse;
import com.koushik.expansetracker.entity.finance.Budget;
import com.koushik.expansetracker.mapper.FinanceMapper;
import com.koushik.expansetracker.security.CustomUserDetails;
import com.koushik.expansetracker.service.finance.interfaces.BudgetServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetServiceInterface budgetService;
    private final FinanceMapper mapper;

    @PostMapping
    public ResponseEntity<BudgetResponse> create(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody BudgetRequest request
    ) {
        Budget saved = budgetService.createBudget(
                mapper.toBudgetEntity(request, user.getUser().getUserId())
        );

        BigDecimal spent = budgetService.calculateSpentForBudget(saved.getBudgetId());
        return ResponseEntity.ok(mapper.toBudgetResponse(saved, spent));
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getAll(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        List<BudgetResponse> list =
                budgetService.getBudgetsForUser(user.getUser().getUserId())
                        .stream()
                        .map(b -> mapper.toBudgetResponse(
                                b,
                                budgetService.calculateSpentForBudget(b.getBudgetId())
                        ))
                        .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponse> getById(@PathVariable Long id) {
        Budget b = budgetService.getBudgetById(id);
        return ResponseEntity.ok(
                mapper.toBudgetResponse(b, budgetService.calculateSpentForBudget(id))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody BudgetRequest request
    ) {
        Budget updated = budgetService.updateBudget(
                id,
                mapper.toBudgetEntity(request, null)
        );

        return ResponseEntity.ok(
                mapper.toBudgetResponse(
                        updated,
                        budgetService.calculateSpentForBudget(updated.getBudgetId())
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.ok("Budget deleted successfully");
    }
}
