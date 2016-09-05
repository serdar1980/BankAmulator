package ru.serdar1980.bank.comsumer;

import java.util.concurrent.ConcurrentLinkedQueue;

import ru.serdar1980.bank.App;
import ru.serdar1980.bank.Bank;
import ru.serdar1980.bank.Transaction;

public class CurrentConsumer implements Runnable {
	
	
	private ConcurrentLinkedQueue<Transaction> queue = new ConcurrentLinkedQueue<Transaction>();
	private Bank bank;
	
	public CurrentConsumer(ConcurrentLinkedQueue<Transaction> queue, Bank bank) {
		this.queue = queue;
		this.bank = bank;
	}
	
	public void run() {
	      Transaction t;
	      System.out.println("Current Consumer Started");
	     while(App.STARTED){ 
	    	 while ((t = queue.poll()) != null) {
	    		 try {
					bank.transferMoney(t.getAccountFrom(), t.getAccountTo(), t.getAmountTransfer());
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
	    	 }
	    	 try {
	    		 Thread.currentThread().sleep(500);
	    	 } catch (Exception ex) {
	    		 ex.printStackTrace();
	    	 }
	     }
	   }
}
