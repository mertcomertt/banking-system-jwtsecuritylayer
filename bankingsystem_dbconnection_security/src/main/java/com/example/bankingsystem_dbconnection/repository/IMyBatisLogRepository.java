package com.example.bankingsystem_dbconnection.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.bankingsystem_dbconnection.model.Log;

@Mapper
public interface IMyBatisLogRepository {

	public void saveLog(String message);
			
	public List<Log> getAccountLogs(long id);
	
	
}
