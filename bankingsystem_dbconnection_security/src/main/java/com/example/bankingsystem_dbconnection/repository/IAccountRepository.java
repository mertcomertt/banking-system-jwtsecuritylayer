package com.example.bankingsystem_dbconnection.repository;

import java.util.List;

import com.example.bankingsystem_dbconnection.model.Account;
import com.example.bankingsystem_dbconnection.model.Account.AccountType;


public interface IAccountRepository {
    Account createAccount(String name, String surname, String email, String tc, String type);
    Account getAccountById(long id);
    Account depositAccount(long id, double balance);
    Account updateAccount(Account ac);
    List<String> getAccountLog(long id);
    boolean transferBalance(long transferredAccount,double balance, long sender);
    
    
}
