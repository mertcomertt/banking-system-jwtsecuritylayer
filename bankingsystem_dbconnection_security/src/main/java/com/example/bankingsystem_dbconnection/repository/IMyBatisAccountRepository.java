package com.example.bankingsystem_dbconnection.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.bankingsystem_dbconnection.model.Account;
import com.example.bankingsystem_dbconnection.model.Account.AccountType;

@Mapper
public interface IMyBatisAccountRepository {

	@Transactional
	public void createAccount(Account account);

	@Transactional
	Account getAccountById(long id);
	
	@Transactional
	public Account depositAccount(long id, double balance);

	@Transactional
	public boolean updateAccountBalance(long id, double balance, long lastUpdateDate);

	@Transactional
	public void deleteAccount(long id, boolean isdeleted, long lastUpdateDate);
	
	@Transactional
	boolean transferBalance(long transferredAccount,double balance, long sender);


}
