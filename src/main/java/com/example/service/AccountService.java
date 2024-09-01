package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.DuplicateUsernameException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account register(Account account) throws DuplicateUsernameException{
        if(account.getUsername().length() == 0 || account.getPassword().length() < 4){
            // throw client error
            return null;
        }

        if (accountRepository.existsByUsername(account.getUsername())) {
            // throw duplicate error
            throw new DuplicateUsernameException("Username alraedy exists");

        }
        //  successful
        return accountRepository.save(account);
        
    }

    public Account loginAccount(Account acc) {
        Optional<Account> account = accountRepository.findByUsernameAndPassword(acc.getUsername(), acc.getPassword());
        if(account.isPresent()){
            return account.get();
        } 
        return null;
    }

}
