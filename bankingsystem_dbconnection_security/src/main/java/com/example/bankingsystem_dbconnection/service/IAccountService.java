package com.example.bankingsystem_dbconnection.service;

import java.util.List;

import com.example.bankingsystem_dbconnection.dto.response.AccountCreateSuccessResponse;
import com.example.bankingsystem_dbconnection.dto.response.AccountTransferBalanceResponse;
import com.example.bankingsystem_dbconnection.model.Account;
import com.example.bankingsystem_dbconnection.model.Account.AccountType;

public interface IAccountService {
	
	Object createAccount(String name, String surname, String email, String tc, String type);
	Account getAccountById(long id);
	Account depositAccount(long id, double balance);
	AccountTransferBalanceResponse transferBalance (long transferredAccount,double balance, long sender);
	List<String> getAccountLog(long id);
}
