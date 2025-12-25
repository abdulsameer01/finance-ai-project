package com.koushik.expansetracker.controller.finance;

import com.koushik.expansetracker.dto.SavingsGoalRequest;
import com.koushik.expansetracker.dto.SavingsGoalResponse;
import com.koushik.expansetracker.entity.finance.SavingsGoal;
import com.koushik.expansetracker.mapper.FinanceMapper;
import com.koushik.expansetracker.security.CustomUserDetails;
import com.koushik.expansetracker.service.finance.interfaces.SavingsGoalServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class SavingsGoalController {

    private final SavingsGoalServiceInterface savingsGoalService;
    private final FinanceMapper mapper;

    @PostMapping
    public ResponseEntity<SavingsGoalResponse> createGoal(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody SavingsGoalRequest request
    ) {
        SavingsGoal saved = savingsGoalService.createGoal(
                mapper.toSavingsGoalEntity(request, user.getUser().getUserId())
        );
        return ResponseEntity.ok(mapper.toSavingsGoalResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<SavingsGoalResponse>> getGoals(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(
                savingsGoalService.getGoalsForUser(user.getUser().getUserId())
                        .stream()
                        .map(mapper::toSavingsGoalResponse)
                        .collect(Collectors.toList())
        );
    }

    @PatchMapping("/{goalId}/transfer")
    public ResponseEntity<SavingsGoalResponse> transferToGoal(
            @PathVariable Long goalId,
            @RequestParam Long accountId,
            @RequestParam BigDecimal amount
    ) {
        SavingsGoal updated =
                savingsGoalService.transferFromAccount(goalId, accountId, amount);

        return ResponseEntity.ok(mapper.toSavingsGoalResponse(updated));
    }

    @DeleteMapping("/{goalId}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long goalId) {
        savingsGoalService.deleteGoal(goalId);
        return ResponseEntity.ok().build();
    }
}
