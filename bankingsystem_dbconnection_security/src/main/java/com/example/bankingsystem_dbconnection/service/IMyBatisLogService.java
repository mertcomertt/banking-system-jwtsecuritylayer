package com.example.bankingsystem_dbconnection.service;

import java.util.List;

import com.example.bankingsystem_dbconnection.model.Log;

public interface IMyBatisLogService {

	public Log saveLog(String message);

	public List<Log> getAccountLogs(long id);

}
