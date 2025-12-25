package com.koushik.expansetracker.controller.finance;

import com.koushik.expansetracker.dto.AccountRequest;
import com.koushik.expansetracker.dto.AccountResponse;
import com.koushik.expansetracker.entity.finance.Account;
import com.koushik.expansetracker.mapper.FinanceMapper;
import com.koushik.expansetracker.security.CustomUserDetails;
import com.koushik.expansetracker.service.finance.interfaces.AccountServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountServiceInterface accountService;
    private final FinanceMapper mapper;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody AccountRequest request
    ) {
        Long userId = currentUser.getUser().getUserId();
        Account saved = accountService.createAccount(mapper.toAccountEntity(request, userId));
        return ResponseEntity.ok(mapper.toAccountResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getUserAccounts(
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        Long userId = currentUser.getUser().getUserId();

        var list = accountService.getAccountsForUser(userId)
                .stream()
                .map(mapper::toAccountResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccountById(
            @PathVariable Long accountId
    ) {
        return ResponseEntity.ok(
                mapper.toAccountResponse(accountService.getAccountById(accountId))
        );
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable Long accountId,
            @Valid @RequestBody AccountRequest request
    ) {
        Account updated = accountService.updateAccount(accountId, mapper.toAccountEntity(request, null));
        return ResponseEntity.ok(mapper.toAccountResponse(updated));
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.ok("Account deleted successfully.");
    }
}
