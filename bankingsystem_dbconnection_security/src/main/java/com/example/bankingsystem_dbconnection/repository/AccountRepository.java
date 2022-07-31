package com.example.bankingsystem_dbconnection.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.example.bankingsystem_dbconnection.exchange.Exchange;
import com.example.bankingsystem_dbconnection.model.Account;
import com.example.bankingsystem_dbconnection.model.Account.AccountType;


public class AccountRepository implements IAccountRepository {
	public static long randNumber;
	
	@Autowired
	private Exchange exchanger;
	


	@Override
	public Account createAccount(String name, String surname, String email, String tc, String type) {
		// TODO Auto-generated method stub
		Random rand = new Random();
		randNumber = rand.nextLong(1_000_000_000L, 7_000_000_000L);
		

		Account tmp = Account.builder().id(randNumber).name(name).surname(surname).email(email).tc(tc)
				.type(AccountType.valueOf(type.toUpperCase())).balance(0).lastUpdateDate(System.currentTimeMillis()).isDeleted(false)
				.build();

		String file = tmp.WritetoFile();
		File f = new File("C:\\Users\\mert\\Desktop\\account\\" + randNumber + ".txt");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(String.valueOf(f))));
			writer.write(file);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tmp;
	}

	public Account getAccountById(long id) {
		File file = new File("C:\\Users\\mert\\Desktop\\account\\" + id + ".txt");
		Account detail = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String col = br.readLine();
			String[] data = col.split(",");

			detail = Account.builder().id(Long.parseLong(data[0])).name(data[1]).surname(data[2]).email(data[3])
					.tc(data[4]).type(AccountType.valueOf(data[5])).balance(Double.parseDouble(data[6]))
					.lastUpdateDate(Long.parseLong(data[7])).isDeleted(Boolean.parseBoolean(data[8])).build();
			br.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.updateAccount(detail); 
		return detail;

	}

	public Account depositAccount(long id, double balance) {
		File file = new File("C:\\Users\\mert\\Desktop\\account\\" + id + ".txt");
		File file2 = new File("C:\\Users\\mert\\Desktop\\account\\logs.txt");
		BufferedReader br;
		Account a = null;
		String filePrint = "";
		String logFormat = "";
		try {
			br = new BufferedReader(new FileReader(file));
			String col = null;
			try {
				col = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			a = new Account();
			String[] data = col.split(",");
			a.setId(Long.parseLong(data[0]));
			a.setName(data[1]);
			a.setSurname(data[2]);
			a.setEmail(data[3]);
			a.setTc(data[4]);
			a.setType(AccountType.valueOf(data[5]));
			a.setBalance(Double.parseDouble(data[6]) + balance);
			a.setLastUpdateDate(Long.parseLong(data[7]));
			a.setDeleted(Boolean.parseBoolean(data[8]));
			logFormat = a.getId() + " " + "deposit amount: " + a.getBalance() ;
			file.delete();

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		filePrint = a.WritetoFile();
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(new File("C:\\Users\\mert\\Desktop\\account\\" + id + ".txt")));
			writer.write(filePrint);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		BufferedWriter writer2;
		try {
			writer2 = new BufferedWriter(new FileWriter(new File("C:\\Users\\mert\\Desktop\\account\\logs.txt")));
			writer2.write(logFormat);
			writer2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		return a;
	}
	public boolean transferBalance(long transferredAccount,double balance, long sender) {
		Account a1 = this.getAccountById(sender);
		Account a2 = this.getAccountById(transferredAccount);
		double d;
		if(balance > a2.getBalance())
		return false;
		
		else {
		if(a1.getType() != a2.getType()) {
			if(a1.getType().equals("USD") && a2.getType().equals("TRY")) {
				d = this.exchanger.exchangeCurrency(String.valueOf(a1.getType()), String.valueOf(a2.getType()), balance);
				a1.setBalance(a1.getBalance() - balance);
				a2.setBalance(a2.getBalance() + d);
			}
			else if(a1.getType().equals("TRY") && a2.getType().equals("USD")) {
				d = this.exchanger.exchangeCurrency(String.valueOf(a1.getType()), String.valueOf(a2.getType()), balance);
				a1.setBalance(a1.getBalance() - balance);
				a2.setBalance(a2.getBalance() + d);
			}
			else if(a1.getType().equals("USD") && a2.getType().equals("EUR")) {
				d = this.exchanger.exchangeCurrency(String.valueOf(a1.getType()), String.valueOf(a2.getType()), balance);
				a1.setBalance(a1.getBalance() - balance);
				a2.setBalance(a2.getBalance() + d);
			}
			else if(a1.getType().equals("EUR") && a2.getType().equals("USD")) {
				d = this.exchanger.exchangeCurrency(String.valueOf(a1.getType()), String.valueOf(a2.getType()), balance);
				a1.setBalance(a1.getBalance() - balance);
				a2.setBalance(a2.getBalance() + d);
			}
			else if(a1.getType().equals("TRY") && a2.getType().equals("EUR")) {
				d = this.exchanger.exchangeCurrency(String.valueOf(a1.getType()), String.valueOf(a2.getType()), balance);
				a1.setBalance(a1.getBalance() - balance);
				a2.setBalance(a2.getBalance() + d);
			}
			else if(a1.getType().equals("EUR") && a2.getType().equals("TRY")) {
				d = this.exchanger.exchangeCurrency(String.valueOf(a1.getType()), String.valueOf(a2.getType()), balance);
				a1.setBalance(a1.getBalance() - balance);
				a2.setBalance(a2.getBalance() + d);
			}
			else if(a1.getType().equals("GAU") && a2.getType().equals("TRY")) {
				d = this.exchanger.exchangeGAU();
				double calculated = balance * d;
				a1.setBalance(a1.getBalance() - balance);
				a2.setBalance(a2.getBalance() + calculated);
			}
			else if(a1.getType().equals("TRY") && a2.getType().equals("GAU")) {
				d = this.exchanger.exchangeGAU();
				double calculated = balance / d;
				a1.setBalance(a1.getBalance() - balance);
				a2.setBalance(a2.getBalance() + calculated);
			}
			else if(a1.getType().equals("GAU") && a2.getType().equals("USD")) {
				double t = this.exchanger.exchangeGAU();
				double calculated = balance * t;
				d = this.exchanger.exchangeCurrency("TRY", String.valueOf(a2.getType()), calculated);
				a1.setBalance(a1.getBalance() - balance);
				a2.setBalance(a2.getBalance() + d);
			}
			else if(a1.getType().equals("USD") && a2.getType().equals("GAU")) {
			    double c = this.exchanger.exchangeCurrency(String.valueOf(a1.getType()), "TRY", balance);
				double t = this.exchanger.exchangeGAU();
				double calculated = c / t;
				a1.setBalance(a1.getBalance() - balance);
				a2.setBalance(a2.getBalance() + calculated);
			}
			else if(a1.getType().equals("GAU") && a2.getType().equals("EUR")) {
				double t = this.exchanger.exchangeGAU();
				double calculated = balance * t;
				d = this.exchanger.exchangeCurrency("TRY", String.valueOf(a2.getType()), calculated);
				a1.setBalance(a1.getBalance() - balance);
				a2.setBalance(a2.getBalance() + d);
			}
			else if(a1.getType().equals("EUR") && a2.getType().equals("GAU")) {
				    double c = this.exchanger.exchangeCurrency(String.valueOf(a1.getType()), "TRY", balance);
					double t = this.exchanger.exchangeGAU();
					double calculated = c / t;
					a1.setBalance(a1.getBalance() - balance);
					a2.setBalance(a2.getBalance() + calculated);
			}
		}
		a1.setBalance(a1.getBalance() - balance);
		a2.setBalance(a2.getBalance() + balance);
		
		
		this.updateAccount(a1);
		this.updateAccount(a2);
		return true;
		}
	}
	
	public Account updateAccount(Account ac) {
		

			ac.setLastUpdateDate(System.currentTimeMillis());
			ac.setDeleted(false);
			String format = ac.WritetoFile();
			File file = new File("C:\\Users\\mert\\Desktop\\account\\" + ac.getId() + ".txt");			
			file.delete();

		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(new File("C:\\Users\\mert\\Desktop\\account\\" + ac.getId() + ".txt")));
			writer.write(format);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ac;
	}
	public List<String> getAccountLog(long id){
		BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader("C:\\Users\\mert\\Desktop\\account\\logs.txt"));
				String row = reader.readLine();
				List<String> list = new ArrayList<String>();
				while (row != null) {
					String[] pieces = row.split(" ");
					if (pieces[0].equals(String.valueOf(id) + "")) {
						if (pieces[1].equals("deposit")) {
							list.add(String.valueOf(id) + " nolu hesaba " + pieces[3] + " " + pieces[4] + " yatırılmıştır.");
						} else {
							list.add(String.valueOf(id) + " hesaptan " + pieces[6] + " hesaba " + pieces[3] + " " + pieces[4]
									+ "  transfer edilmiştir.");
						}
						row = reader.readLine();
					} else {
						row = reader.readLine();
					}
				}

				reader.close();
				return list;

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
	
	
}
