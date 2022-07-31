package com.example.bankingsystem_dbconnection.model;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("Account")
public class Account implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private String surname;
	private String email;
	private String tc;
	private AccountType type;
	private double balance;
	private long lastUpdateDate;
	private boolean isDeleted;
	private int userId;
	
	public enum AccountType{
		USD,
		TRY,
		GAU
	}
	public String WritetoFile() {
		return this.id + "," + 
				this.name + "," + 
				this.surname + "," + 
				this.email + "," + 
				this.tc + "," + 
				this.type + "," + 
				this.balance + "," + 
				this.lastUpdateDate + "," + 
				this.isDeleted+ "," + 
				this.userId;
	}

}
