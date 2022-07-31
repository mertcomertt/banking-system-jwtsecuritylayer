package com.example.bankingsystem_dbconnection.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.bankingsystem_dbconnection.model.Log;
import com.example.bankingsystem_dbconnection.repository.IMyBatisAccountRepository;
import com.example.bankingsystem_dbconnection.repository.IMyBatisLogRepository;

public class MyBatisLogService implements IMyBatisLogService{

	Log record;
	
	
	private IMyBatisLogRepository mybatisLogRepository;
	
	private IMyBatisAccountRepository mybatisAccountRepository;
	
	@Autowired
	MyBatisLogService(IMyBatisLogRepository mybatisLogRepository,IMyBatisAccountRepository mybatisAccountRepository){
		this.mybatisLogRepository = mybatisLogRepository;
		this.mybatisAccountRepository = mybatisAccountRepository;
	}
	
	//Saving logs of transaction
	@Override
	public Log saveLog(String message) {
		record = new Log();
		record.setMessage(message);
		this.mybatisLogRepository.saveLog(message);
		return record;
		
		
	}
	
	//Getting all logs of given id
	@Override
	public List<Log> getAccountLogs(long id) {
	
			List<Log> list = new ArrayList<Log>();			
			list = this.mybatisLogRepository.getAccountLogs(id);
			List<Log> formattedLogs = new ArrayList<Log>();
			
			for(int t = 0 ; t < list.size() ; t++) {
				if(list.get(t).getMessage().contains("transferred")) {
					String [] record = list.get(t).getMessage().split(" ");
					String text = String.valueOf(id)+ " hesaptan " + record[6]+ " hesaba "+ record[3]+ " "+ record[4]+ " transfer edilmiştir.";
					Log l1 = new Log();
					l1.setMessage(text);
					formattedLogs.add(l1);
				}
				else {
					String [] record2 = list.get(t).getMessage().split(" ");
					String text = String.valueOf(id) + " nolu hesaba " + record2[3] + " " + record2[4] + " yatırılmıştır.";
					Log l2 = new Log();
					l2.setMessage(text);
					formattedLogs.add(l2);
				}
				
			}
			
			return formattedLogs;
			
			
			
	}

}
