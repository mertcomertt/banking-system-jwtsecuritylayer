package com.example.bankingsystem_dbconnection.dto.request;

import lombok.Data;

@Data
public class AccountTransferRequest {
	    private long transferredAccountNumber;
	    private double amount;
	
}
