package com.example.bankingsystem_dbconnection.model;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Alias("Log")
@NoArgsConstructor
@AllArgsConstructor
public class Log {
	private int id;
	private String message;
	
}
