package com.koushik.expansetracker.service.finance.interfaces;

import com.koushik.expansetracker.entity.finance.Account;

import java.util.List;

public interface AccountServiceInterface {

    Account createAccount(Account account);

    List<Account> getAccountsForUser(Long userId);

    Account getAccountById(Long accountId);

    Account updateAccount(Long accountId, Account updatedAccount);

    void deleteAccount(Long accountId);
}
