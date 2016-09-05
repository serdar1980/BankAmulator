package ru.serdar1980.bank.utils;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import ru.serdar1980.bank.AccountImpl;
import ru.serdar1980.bank.App;
import ru.serdar1980.bank.Bank;

public class BankInit implements Callable<Integer>  {
	
	private Bank bank;
	public BankInit(Bank bank){
		this.bank=bank;
	}
	
	public Integer call() throws Exception {
		for(int i=1; i<=App.NUM_OF_ACCOUNTS/App.NUM_OF_THREAD; i++) {
			if(bank.getBankStorage().size()> App.NUM_OF_ACCOUNTS){
				break;
			}
        	String accNumber = GenerateAccountData.getAccountNumber(App.ACCOUNT_ID_LENGTH);
        	Long money = GenerateAccountData.getAccountMoney();
        	boolean block = (Math.random() < 0.1);
        	//System.out.println(new StringBuilder("accNumber:").append(accNumber).append(" money:").append(money).append(" block:").append(block));
        	bank.getBankStorage().putIfAbsent(accNumber, new AccountImpl(accNumber, money, block));
		}			
		//System.out.println(bank.getBankStorage().size());
		return App.NUM_OF_ACCOUNTS/App.NUM_OF_THREAD;
	}

}