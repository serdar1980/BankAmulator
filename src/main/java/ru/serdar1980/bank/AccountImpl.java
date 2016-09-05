package ru.serdar1980.bank;

import java.util.Comparator;
import java.util.Random;

public class AccountImpl implements AccountInterface  {
	private long money;
	private String accNumber;
	private boolean block;
	
	public AccountImpl(String accNumber, long money, boolean block ){
		 final Random random = new Random();
		 this.accNumber = accNumber;
		 this.money = money;
		 this.block = block;
	}
	
	public long getBalance() {
		return money;
	}

	public boolean isBlock() {
		return block;
	}

	public void setBlock(boolean block) {
		this.block = block;
	}

	public void deposit(long deposit) {
		money += deposit;
	}

	public void withdraw(long w) {
		if (w < money) {
			money -= w;
		} else {
			System.out.println(new StringBuilder("You can't withdraw  ").append(w).append(" !").toString() );
		}
	}
	
	public  Boolean hasSufficientBalance(long w){
		return (w < money);
	}

	public String getAccNumber() {
		return accNumber;
	}
}
