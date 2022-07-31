package com.example.bankingsystem_dbconnection.dto.request;

import lombok.Data;

@Data
public class UserLoginRequest {

	private String username;
	private String password;
}
