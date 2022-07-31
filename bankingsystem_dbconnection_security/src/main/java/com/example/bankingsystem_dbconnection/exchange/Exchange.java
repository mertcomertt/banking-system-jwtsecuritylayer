package com.example.bankingsystem_dbconnection.exchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Component
public class Exchange {

	@Autowired
	private RestTemplate client;
	
	public double exchangeCurrency(String senderAccount, String receiverAccount, double balance) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("content-type", "application/json");
		headers.add("authorization", "yourkey");
		String url = "https://api.collectapi.com/economy/exchange?int=" + balance + "&to=" + receiverAccount + "&base="
				+ senderAccount;
		HttpEntity<?> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = client.exchange(url, HttpMethod.GET, requestEntity, String.class);
		String outcome = response.getBody();
		ObjectMapper objectMapper = new ObjectMapper();
		Double result = 0.0;
		JsonNode node;
		try {
			node = objectMapper.readTree(outcome);
			JsonNode tmp = node.get("result");
			ArrayNode data = (ArrayNode) tmp.get("data");
			JsonNode info = data.get(0);
			result = info.get("calculated").asDouble();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public double exchangeGAU() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("content-type", "application/json");
		headers.add("authorization", "yourkey");
		String url = "https://api.collectapi.com/economy/goldPrice";
		HttpEntity<?> requestEntity = new HttpEntity<>(headers);
		ObjectMapper obj = new ObjectMapper();	
		ResponseEntity<String> response = client.exchange(url, HttpMethod.GET,requestEntity,String.class);
		String outcome = response.getBody();
		JsonNode node;
		Double result = 0.0;
		try {
			node = obj.readTree(outcome);
			result = node.get("result").get(1).get("buying").asDouble();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	}

