package com.example.bankingsystem_dbconnection.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDepositRequest {
 private double balance;
}
