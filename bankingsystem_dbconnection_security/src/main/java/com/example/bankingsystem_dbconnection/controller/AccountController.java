package com.example.bankingsystem_dbconnection.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.bankingsystem_dbconnection.dto.request.AccountCreateRequest;
import com.example.bankingsystem_dbconnection.dto.request.AccountDepositRequest;
import com.example.bankingsystem_dbconnection.dto.request.AccountTransferRequest;
import com.example.bankingsystem_dbconnection.dto.response.NotAuthorizedAccessResponse;
import com.example.bankingsystem_dbconnection.model.Account;
import com.example.bankingsystem_dbconnection.model.Account.AccountType;
import com.example.bankingsystem_dbconnection.model.AccountUserDetails;
import com.example.bankingsystem_dbconnection.model.Log;
import com.example.bankingsystem_dbconnection.repository.IAccountRepository;
import com.example.bankingsystem_dbconnection.service.IAccountService;
import com.example.bankingsystem_dbconnection.service.IMyBatisAccountService;
import com.example.bankingsystem_dbconnection.service.IMyBatisLogService;

@RestController
public class AccountController {

	private final IMyBatisAccountService mybatisAccountService;

	private final IMyBatisLogService mybatisLogService;

	private final KafkaTemplate<String, String> producer;

	@Autowired
	public AccountController(IMyBatisAccountService mybatisAccountService, IMyBatisLogService mybatisLogService,
			KafkaTemplate<String, String> producer) {
		this.mybatisAccountService = mybatisAccountService;
		this.mybatisLogService = mybatisLogService;
		this.producer = producer;
	}

	// Creating new account with given parameters
	@PostMapping(path = "/accounts")
	public ResponseEntity<?> createAccount(@RequestBody AccountCreateRequest request) {
		AccountUserDetails logged = (AccountUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		try {
			boolean obj = this.mybatisAccountService.createAccount(request.getName(), request.getSurname(),
					request.getEmail(), request.getTc(), request.getType(), logged.getId());
			return ResponseEntity.status(HttpStatus.OK).body(obj);
		} catch (ResponseStatusException e) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	// Getting account details with given id parameter
	@GetMapping(path = "/accounts/{id}")
	public ResponseEntity<?> getAccountById(@PathVariable long id) {
		Account ac = this.mybatisAccountService.getAccountById(id);
		AccountUserDetails logged = (AccountUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		if (!ac.equals(null) && logged.getId() == ac.getUserId()) {
			return ResponseEntity.status(HttpStatus.OK).lastModified(ac.getLastUpdateDate()).body(ac);
		} else {
			NotAuthorizedAccessResponse aut = new NotAuthorizedAccessResponse();
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(aut);
		}

	}

	// Updating account balance with given amount
	@PutMapping(path = "/accounts/{id}")
	public ResponseEntity<?> depositAccount(@PathVariable long id, @RequestBody AccountDepositRequest request) {
		String msg = id + " deposit amount: " + request.getBalance();
		producer.send("logs", msg);
		AccountUserDetails logged = (AccountUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Account t = this.mybatisAccountService.updateAccountBalance(id, request.getBalance(),
				System.currentTimeMillis());
		Account ac = this.mybatisAccountService.getAccountById(id);
		if (!ac.equals(null) && logged.getId() == ac.getUserId()) {
			if (t != null) {
				return ResponseEntity.status(HttpStatus.OK).body(this.mybatisAccountService.updateAccountBalance(id,
						request.getBalance(), System.currentTimeMillis()));
			} else {
				return ResponseEntity.status(HttpStatus.OK).body(null);
			}
		} else {
			NotAuthorizedAccessResponse aut = new NotAuthorizedAccessResponse();
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(aut);
		}

	}

	// Transferring amount between sender and receiver account by updating balance
	@PostMapping("/accounts/{id}/")
	public ResponseEntity<?> transferBalance(@PathVariable long id, @RequestBody AccountTransferRequest request) {

		AccountUserDetails logged = (AccountUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Account ac = this.mybatisAccountService.getAccountById(id);
		String msg = id + " transfer amount: " + request.getAmount() + " " + " ,transferred_account: "
				+ request.getTransferredAccountNumber();
		producer.send("logs", msg);
		if (!ac.equals(null) && logged.getId() == ac.getUserId()) {
			return ResponseEntity.status(HttpStatus.OK).body(this.mybatisAccountService
					.transferBalance(request.getTransferredAccountNumber(), request.getAmount(), id));
		} else {
			NotAuthorizedAccessResponse aut = new NotAuthorizedAccessResponse();
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(aut);
		}

	}
	// Accessing logs of given id
	@GetMapping("/accounts/{id}/log")
	@CrossOrigin(origins = "http://localhost:6162")
	public ResponseEntity<Object> loggingAccount(@PathVariable long id) {
		List<Log> log = this.mybatisLogService.getAccountLogs(id);
		return ResponseEntity.status(HttpStatus.OK).body(log);
	}
	// Deleting account given id (soft delete)
	@DeleteMapping("/accounts/{id}")
	public ResponseEntity<?> delete(@PathVariable int id) {
		AccountUserDetails logged = (AccountUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Account ac = this.mybatisAccountService.getAccountById(id);
		if (logged.getId() == ac.getUserId() && !ac.equals(null)) {

			mybatisAccountService.deleteAccount(id);
			Account check = mybatisAccountService.getAccountById(id);

			if (check.isDeleted() == true) {
				return ResponseEntity.status(HttpStatus.OK).body("Account successfully deleted");
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account deletion failed");
			}

		} else {
			NotAuthorizedAccessResponse aut = new NotAuthorizedAccessResponse();
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(aut);
		}
	}
}