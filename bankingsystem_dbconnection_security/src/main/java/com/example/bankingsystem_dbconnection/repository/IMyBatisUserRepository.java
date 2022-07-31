package com.example.bankingsystem_dbconnection.repository;

import org.apache.ibatis.annotations.Mapper;

import com.example.bankingsystem_dbconnection.model.AccountUser;

@Mapper
public interface IMyBatisUserRepository {
	
	public AccountUser getUserWithName(String username);
}
