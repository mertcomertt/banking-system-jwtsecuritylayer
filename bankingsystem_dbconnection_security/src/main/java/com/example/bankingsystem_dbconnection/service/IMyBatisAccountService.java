package com.example.bankingsystem_dbconnection.service;

import org.springframework.transaction.annotation.Transactional;

import com.example.bankingsystem_dbconnection.dto.response.AccountTransferBalanceResponse;
import com.example.bankingsystem_dbconnection.model.Account;

public interface IMyBatisAccountService {

	
	
	

	public boolean createAccount(String name, String surname, String email, String tc, String type, int userId);

	
	public Account getAccountById(long id);
	
	
	public Account updateAccountBalance(long id, double balance,long lastUpdateDate);


	public AccountTransferBalanceResponse transferBalance(long transferredAccount,double balance, long sender);
	
	public void deleteAccount(long id);
	
	
	
}
