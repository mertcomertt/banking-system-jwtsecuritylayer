package com.example.bankingsystem_dbconnection.dto.response;

import lombok.Data;

@Data
public class NotAuthorizedAccessResponse {
	
private String message;

public NotAuthorizedAccessResponse() {
	this.message = "Invalid Account Number";
}
}
