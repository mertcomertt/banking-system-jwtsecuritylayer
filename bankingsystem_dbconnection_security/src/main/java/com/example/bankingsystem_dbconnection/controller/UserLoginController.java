package com.example.bankingsystem_dbconnection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.bankingsystem_dbconnection.dto.request.UserLoginRequest;
import com.example.bankingsystem_dbconnection.security.JWTTokenUtil;
import com.example.bankingsystem_dbconnection.service.UserControlService;

public class UserLoginController {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JWTTokenUtil jwtTokenUtil;
	@Autowired
	private UserControlService userDetailsService;
	
	//Checking logging user credentials and giving token to authenticate
	@PostMapping("/auth")
	public ResponseEntity<?> login(@RequestBody UserLoginRequest loginRequest) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		}
		catch (BadCredentialsException e){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Credentials");
		}
		catch (DisabledException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Credentials");
		}
		final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
		final String token = jwtTokenUtil.generateToken(userDetails);
		return ResponseEntity.status(HttpStatus.OK).body(token);
		
	}
	
}
