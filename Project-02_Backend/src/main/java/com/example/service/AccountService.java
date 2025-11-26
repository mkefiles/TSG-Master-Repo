package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

import java.util.List;

// Step 000: Annotation-based Configuration
// Step 01A: Identify a Controller (Annotation)
@Service // Service "Bean"
public class AccountService {

    private Account account = new Account();

    @Autowired
    AccountRepository accountRepository;

    // PT01 -- Service for User Registration (POST - localhost:8080/register)
    public int createNewAccount(Account newAccount) {
        // DESC: Ensure username is not blank
        if (newAccount.getUsername().isEmpty()) {
            return 400;
        } else {
            // DESC: Ensure password is greater-than or equal-to four characters
            if (newAccount.getPassword().length() < 4) {
                return 400;
            } else {
                // DESC: Ensure username is not duplicate
                if (accountRepository.existsByUsername(newAccount.getUsername())) {
                    return 409;
                } else {
                    // DESC: Add account to database
                    accountRepository.save(newAccount);

                    // DESC: Update newAccount instance with ID from database
                    List<Account> accountEntries = accountRepository.findByUsername(newAccount.getUsername());
                    for (Account accountEntry : accountEntries) {
                        if (accountEntry.getUsername().equals(newAccount.getUsername())) {
                            newAccount.setAccountId(accountEntry.getAccountId());
                        }
                    }
                    return 200;
                }
            }
        }
    }

    // PT02 -- Service for User Login (POST - localhost:8080/login)
    public int readAccountToLogin(Account existingAccount) {
        // DESC: Get all database entries with specified username
        List<Account> accountEntries = accountRepository.findByUsername(existingAccount.getUsername());

        // DESC: Ensure List is not empty (i.e., username match located)
        if (!accountEntries.isEmpty()) {
            // DESC: Locate username in List
            for (Account accountEntry: accountEntries) {
                // DESC: Ensure username match (prior to comparing password; redundant code)
                if (accountEntry.getUsername().equals(existingAccount.getUsername())) {
                    // DESC: Ensure password is a match
                    if (accountEntry.getPassword().equals(existingAccount.getPassword())) {
                        // DESC: Update existingAccount instance with ID from database
                        existingAccount.setAccountId(accountEntry.getAccountId());
                        // DESC: Username and password matched
                        return 200;
                    } else {
                        // DESC: Username matched, yet password did not
                        return 401;
                    }
                }
            }
        }

        // DESC: Username not found (i.e., List is empty)
        return 401;
    }
}
