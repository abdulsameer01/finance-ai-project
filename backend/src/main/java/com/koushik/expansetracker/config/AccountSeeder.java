package com.koushik.expansetracker.config;

import com.koushik.expansetracker.entity.finance.Account;
import com.koushik.expansetracker.entity.finance.enums.AccountType;
import com.koushik.expansetracker.repository.finance.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class AccountSeeder {

    private final AccountRepository accountRepository;

    public void seedDefaultAccounts(Long userId) {

        seed(AccountType.CASH, "Cash", userId);
        seed(AccountType.BANK, "Bank", userId);
        seed(AccountType.WALLET, "GPay", userId);
        seed(AccountType.CARD, "Credit Card", userId);
    }

    private void seed(AccountType type, String name, Long userId) {
        accountRepository
                .findByAccountTypeAndUserId(type, userId)
                .orElseGet(() ->
                        accountRepository.save(
                                Account.builder()
                                        .accountName(name)
                                        .accountType(type)
                                        .balance(BigDecimal.ZERO)
                                        .currency("INR")
                                        .userId(userId)
                                        .build()
                        )
                );
    }
}
