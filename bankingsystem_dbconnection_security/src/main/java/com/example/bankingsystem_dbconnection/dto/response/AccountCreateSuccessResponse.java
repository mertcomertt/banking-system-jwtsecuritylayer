package com.example.bankingsystem_dbconnection.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreateSuccessResponse {

	private String message;
	private long accountNumber;

	

}
