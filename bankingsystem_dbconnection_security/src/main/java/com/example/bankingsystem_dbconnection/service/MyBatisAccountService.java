package com.example.bankingsystem_dbconnection.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bankingsystem_dbconnection.dto.response.AccountCreateNotSuccessResponse;
import com.example.bankingsystem_dbconnection.dto.response.AccountCreateSuccessResponse;
import com.example.bankingsystem_dbconnection.dto.response.AccountTransferBalanceResponse;
import com.example.bankingsystem_dbconnection.exchange.Exchange;
import com.example.bankingsystem_dbconnection.model.Account;
import com.example.bankingsystem_dbconnection.model.Account.AccountType;
import com.example.bankingsystem_dbconnection.repository.IMyBatisAccountRepository;

@Service
public class MyBatisAccountService implements IMyBatisAccountService {

	public static long randNumber;
	
	
	private Exchange exchanger;
	
	
	private IMyBatisAccountRepository mybatisAccountRepository;
	
	@Autowired
	MyBatisAccountService(IMyBatisAccountRepository mybatisAccountRepository,Exchange exchanger){
		this.mybatisAccountRepository = mybatisAccountRepository;
		this.exchanger = exchanger;
		}

	
	@Override
	public boolean createAccount(String name, String surname, String email, String tc, String type, int userId) {
		
		boolean checkType = false;
		if (type.equalsIgnoreCase("TRY") || type.equalsIgnoreCase("USD") || type.equalsIgnoreCase("GAU"))
			checkType = true;

		if (checkType) {
			
			
			

			Account tmp = Account.builder().name(name).surname(surname).email(email).tc(tc)
					.type(AccountType.valueOf(type.toUpperCase())).balance(0).lastUpdateDate(System.currentTimeMillis()).isDeleted(false)
					.build();
			
				this.mybatisAccountRepository.createAccount(tmp);
				return true;
			}
		else {
			return false;

		}

		}
	
	
	@Override
	public Account getAccountById(long id) {
	
		return this.mybatisAccountRepository.getAccountById(id);
	}
	
	@Override
	public Account updateAccountBalance(long id, double balance,long lastUpdateDate) {
		Account a = mybatisAccountRepository.getAccountById(id);
		Account b;
		if(this.mybatisAccountRepository.updateAccountBalance(id, a.getBalance() + balance, lastUpdateDate)) {
			b = mybatisAccountRepository.getAccountById(id);
		 return b;
		}
		else {
			return null;
		}
	}
	
	@Override
	public AccountTransferBalanceResponse transferBalance(long transferredAccount, double balance, long sender) {
		// TODO Auto-generated method stub
		Account a1 = mybatisAccountRepository.getAccountById(sender);
		Account a2 = mybatisAccountRepository.getAccountById(transferredAccount);
		AccountTransferBalanceResponse transferResponse = new AccountTransferBalanceResponse();
		double d;
		if(balance > a1.getBalance()) {
			transferResponse = new AccountTransferBalanceResponse();
			transferResponse.setMessage("Insufficient balance");
			return transferResponse;
		}
		
		
		else {
		if(a1.getType() != a2.getType()) {
			if(a1.getType().equals("USD") && a2.getType().equals("TRY")) {
				d = this.exchanger.exchangeCurrency(String.valueOf(a1.getType()), String.valueOf(a2.getType()), balance);
				a1.setBalance(a1.getBalance() - balance);
				a2.setBalance(a2.getBalance() + d);
			}
			else if(a1.getType().equals("TRY") && a2.getType().equals("USD")) {
				d = this.exchanger.exchangeCurrency(String.valueOf(a1.getType()), String.valueOf(a2.getType()), balance);
				a1.setBalance(a1.getBalance() - balance);
				a2.setBalance(a2.getBalance() + d);
			}
			else if(a1.getType().equals("USD") && a2.getType().equals("EUR")) {
				d = this.exchanger.exchangeCurrency(String.valueOf(a1.getType()), String.valueOf(a2.getType()), balance);
				a1.setBalance(a1.getBalance() - balance);
				a2.setBalance(a2.getBalance() + d);
			}
			else if(a1.getType().equals("EUR") && a2.getType().equals("USD")) {
				d = this.exchanger.exchangeCurrency(String.valueOf(a1.getType()), String.valueOf(a2.getType()), balance);
				a1.setBalance(a1.getBalance() - balance);
				a2.setBalance(a2.getBalance() + d);
			}
			else if(a1.getType().equals("TRY") && a2.getType().equals("EUR")) {
				d = this.exchanger.exchangeCurrency(String.valueOf(a1.getType()), String.valueOf(a2.getType()), balance);
				a1.setBalance(a1.getBalance() - balance);
				a2.setBalance(a2.getBalance() + d);
			}
			else if(a1.getType().equals("EUR") && a2.getType().equals("TRY")) {
				d = this.exchanger.exchangeCurrency(String.valueOf(a1.getType()), String.valueOf(a2.getType()), balance);
				a1.setBalance(a1.getBalance() - balance);
				a2.setBalance(a2.getBalance() + d);
			}
			else if(a1.getType().equals("GAU") && a2.getType().equals("TRY")) {
				d = this.exchanger.exchangeGAU();
				double calculated = balance * d;
				a1.setBalance(a1.getBalance() - balance);
				a2.setBalance(a2.getBalance() + calculated);
			}
			else if(a1.getType().equals("TRY") && a2.getType().equals("GAU")) {
				d = this.exchanger.exchangeGAU();
				double calculated = balance / d;
				a1.setBalance(a1.getBalance() - balance);
				a2.setBalance(a2.getBalance() + calculated);
			}
			else if(a1.getType().equals("GAU") && a2.getType().equals("USD")) {
				double t = this.exchanger.exchangeGAU();
				double calculated = balance * t;
				d = this.exchanger.exchangeCurrency("TRY", String.valueOf(a2.getType()), calculated);
				a1.setBalance(a1.getBalance() - balance);
				a2.setBalance(a2.getBalance() + d);
			}
			else if(a1.getType().equals("USD") && a2.getType().equals("GAU")) {
			    double c = this.exchanger.exchangeCurrency(String.valueOf(a1.getType()), "TRY", balance);
				double t = this.exchanger.exchangeGAU();
				double calculated = c / t;
				a1.setBalance(a1.getBalance() - balance);
				a2.setBalance(a2.getBalance() + calculated);
			}
			else if(a1.getType().equals("GAU") && a2.getType().equals("EUR")) {
				double t = this.exchanger.exchangeGAU();
				double calculated = balance * t;
				d = this.exchanger.exchangeCurrency("TRY", String.valueOf(a2.getType()), calculated);
				a1.setBalance(a1.getBalance() - balance);
				a2.setBalance(a2.getBalance() + d);
			}
			else if(a1.getType().equals("EUR") && a2.getType().equals("GAU")) {
				    double c = this.exchanger.exchangeCurrency(String.valueOf(a1.getType()), "TRY", balance);
					double t = this.exchanger.exchangeGAU();
					double calculated = c / t;
					a1.setBalance(a1.getBalance() - balance);
					a2.setBalance(a2.getBalance() + calculated);
			}
		}
		a1.setBalance(a1.getBalance() - balance);
		a2.setBalance(a2.getBalance() + balance);
		
		
		
		this.mybatisAccountRepository.updateAccountBalance(a1.getId(), a1.getBalance(), System.currentTimeMillis());
		this.mybatisAccountRepository.updateAccountBalance(a2.getId(), a2.getBalance(), System.currentTimeMillis());
		transferResponse.setMessage("Transferred Successfully");
		return transferResponse;
		}
		
	}

	
	@Override
	public void deleteAccount(long id) {
			Account deletedAccount = mybatisAccountRepository.getAccountById(id);
			mybatisAccountRepository.deleteAccount(id, true, System.currentTimeMillis());

		
		
	}

}
