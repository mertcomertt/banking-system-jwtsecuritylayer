package com.example.bankingsystem_dbconnection.model;

import java.util.Collection;
import java.util.Iterator;

import org.apache.catalina.Group;
import org.apache.catalina.Role;
import org.apache.catalina.User;
import org.apache.catalina.UserDatabase;
import org.apache.ibatis.type.Alias;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
import lombok.NoArgsConstructor;

@Alias("AccountUser")
@Data
@NoArgsConstructor
public class AccountUser{

	private int id;
	private String username;
	private String password;
	private String authorities;

	
}
