package com.example.bankingsystem_dbconnection.dto.request;



import com.example.bankingsystem_dbconnection.model.Account.AccountType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountCreateRequest {
	private String name;
	private String surname;
	private String email;
	private String tc;
	private String  type;
	
}
