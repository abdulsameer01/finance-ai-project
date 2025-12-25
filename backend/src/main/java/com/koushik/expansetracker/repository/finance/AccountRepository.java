package com.koushik.expansetracker.repository.finance;

import com.koushik.expansetracker.entity.finance.Account;
import com.koushik.expansetracker.entity.finance.enums.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // ✅ Used by AccountService
    List<Account> findByUserId(Long userId);

    // ✅ Used by AccountSeeder (IMPORTANT)
    Optional<Account> findByAccountTypeAndUserId(AccountType accountType, Long userId);

    // ✅ Used by AccountSeeder (optional safety)
    boolean existsByUserId(Long userId);
}
