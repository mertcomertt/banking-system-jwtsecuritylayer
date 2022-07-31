package com.example.bankingsystem_dbconnection.exchange;

public interface IExchange {

	public double exchangeCurrency(String senderAccount, String receiverAccount, double balance);

	public double exchangeGAU();
}
