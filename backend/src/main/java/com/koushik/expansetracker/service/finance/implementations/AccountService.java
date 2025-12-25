package com.koushik.expansetracker.service.finance.implementations;

import com.koushik.expansetracker.entity.finance.Account;
import com.koushik.expansetracker.repository.finance.AccountRepository;
import com.koushik.expansetracker.security.OwnershipValidator;
import com.koushik.expansetracker.service.finance.interfaces.AccountServiceInterface;
import com.koushik.expansetracker.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService implements AccountServiceInterface {

    private final AccountRepository accountRepository;
    private final OwnershipValidator ownershipValidator;
    @Override
    public Account createAccount(Account account) {
        if (account.getBalance() == null) {
            account.setBalance(BigDecimal.ZERO);
        }
        return accountRepository.save(account);
    }

    @Override
    public List<Account> getAccountsForUser(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    @Override
    public Account getAccountById(Long accountId) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        ownershipValidator.validateAccount(accountId, currentUserId);
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
    }

    @Override
    public Account updateAccount(Long accountId, Account updatedAccount) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        ownershipValidator.validateAccount(accountId, currentUserId);
        Account existing = getAccountById(accountId);
        existing.setAccountName(updatedAccount.getAccountName());
        existing.setAccountType(updatedAccount.getAccountType());
        existing.setCurrency(updatedAccount.getCurrency());
        // balance normally should be managed by transactions; update only if you allow manual override
        if (updatedAccount.getBalance() != null) {
            existing.setBalance(updatedAccount.getBalance());
        }
        return accountRepository.save(existing);
    }

    @Override
    public void deleteAccount(Long accountId) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        ownershipValidator.validateAccount(accountId, currentUserId);
        accountRepository.deleteById(accountId);
    }
}
