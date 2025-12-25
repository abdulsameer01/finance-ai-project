package com.koushik.expansetracker.service.finance.implementations;

import com.koushik.expansetracker.entity.finance.Account;
import com.koushik.expansetracker.entity.finance.SavingsGoal;
import com.koushik.expansetracker.repository.finance.AccountRepository;
import com.koushik.expansetracker.repository.finance.SavingsGoalRepository;
import com.koushik.expansetracker.security.OwnershipValidator;
import com.koushik.expansetracker.service.finance.interfaces.SavingsGoalServiceInterface;
import com.koushik.expansetracker.util.SecurityUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SavingsGoalService implements SavingsGoalServiceInterface {

    private final SavingsGoalRepository goalRepo;
    private final AccountRepository accountRepo;
    private final OwnershipValidator ownershipValidator;

    public SavingsGoalService(
            SavingsGoalRepository goalRepo,
            AccountRepository accountRepo,
            OwnershipValidator ownershipValidator
    ) {
        this.goalRepo = goalRepo;
        this.accountRepo = accountRepo;
        this.ownershipValidator = ownershipValidator;
    }

    @Override
    public SavingsGoal createGoal(SavingsGoal goal) {
        goal.setCurrentAmount(BigDecimal.ZERO);
        return goalRepo.save(goal);
    }

    @Override
    public List<SavingsGoal> getGoalsForUser(Long userId) {
        return goalRepo.findByUserId(userId);
    }

    @Transactional
    @Override
    public SavingsGoal transferFromAccount(Long goalId, Long accountId, BigDecimal amount) {

        Long userId = SecurityUtil.getCurrentUserId();
        ownershipValidator.validateSavingsGoal(goalId, userId);
        ownershipValidator.validateAccount(accountId, userId);

        SavingsGoal goal = goalRepo.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));
        goal.setCurrentAmount(goal.getCurrentAmount().add(amount));

        accountRepo.save(account);
        return goalRepo.save(goal);
    }

    @Override
    public void deleteGoal(Long goalId) {
        Long userId = SecurityUtil.getCurrentUserId();
        ownershipValidator.validateSavingsGoal(goalId, userId);
        goalRepo.deleteById(goalId);
    }
}
