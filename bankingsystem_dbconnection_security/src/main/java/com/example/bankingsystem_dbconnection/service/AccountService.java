package com.example.bankingsystem_dbconnection.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bankingsystem_dbconnection.dto.response.AccountCreateNotSuccessResponse;
import com.example.bankingsystem_dbconnection.dto.response.AccountCreateSuccessResponse;
import com.example.bankingsystem_dbconnection.dto.response.AccountTransferBalanceResponse;
import com.example.bankingsystem_dbconnection.model.Account;
import com.example.bankingsystem_dbconnection.repository.AccountRepository;
import com.example.bankingsystem_dbconnection.repository.IMyBatisAccountRepository;

//@Service
public class AccountService implements IAccountService {

	
private AccountRepository accountRepository;
	

	public Object createAccount(String name, String surname, String email, String tc, String type) {
		// Check if account type is valid
		boolean checkType = false;
		if (type.equalsIgnoreCase("TRY") || type.equalsIgnoreCase("USD") || type.equalsIgnoreCase("GAU"))
			checkType = true;

		if (checkType) {
			Account acc = new Account();
			acc = (Account) accountRepository.createAccount(name, surname, email, tc, type);
			if (acc != null) {
				AccountCreateSuccessResponse successResponse = new AccountCreateSuccessResponse();
				successResponse.setMessage("Account Created");
				successResponse.setAccountNumber(acc.getId());
				return successResponse;
			}

		}

		else {
			AccountCreateNotSuccessResponse notSuccessResponse = new AccountCreateNotSuccessResponse();
			notSuccessResponse.setMessage("Invalid Account Type: " + type);
			return notSuccessResponse;

		}
		return null;

	}

	public Account getAccountById(long id) {
		Account d = this.accountRepository.getAccountById(id);
		return d;
	}

	public Account depositAccount(long id, double balance) {
		Account t = new Account();
		t = this.accountRepository.depositAccount(id, balance);

		if (1_000_000_000L <= id && id < 7_000_000_000L)
			return null;

		
		return t;

	}
	public AccountTransferBalanceResponse transferBalance(long transferredAccount,double balance, long sender) {
		boolean check = this.accountRepository.transferBalance(transferredAccount, balance, sender);
		AccountTransferBalanceResponse transferResponse = new AccountTransferBalanceResponse();
		if(check) {
			transferResponse.setMessage("Transferred Successfully");
			return transferResponse;
		}
		else {
			transferResponse.setMessage("Insufficient balance");
			return transferResponse;
		}
	}
	public List<String> getAccountLog(long id){
		List<String> logs = new ArrayList<String>();
		logs = this.accountRepository.getAccountLog(id);
		if(logs != null)
		{
			return logs;
		}
		return null;
	}
}
