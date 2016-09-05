package ru.serdar1980.bank.provider;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import ru.serdar1980.bank.App;
import ru.serdar1980.bank.Bank;
import ru.serdar1980.bank.Transaction;
import ru.serdar1980.bank.utils.GenerateAccountData;
import ru.serdar1980.bank.utils.LongRandom;

public class TransferProducer implements Runnable {

	private ConcurrentLinkedQueue<Transaction> currentQueue = new ConcurrentLinkedQueue<Transaction>();
	private ConcurrentLinkedQueue<Transaction> checkedQueue = new ConcurrentLinkedQueue<Transaction>();
	private Bank bank;

	public TransferProducer(ConcurrentLinkedQueue<Transaction> currentQueue,
			ConcurrentLinkedQueue<Transaction> checkedQueue, Bank bank) {
		this.currentQueue = currentQueue;
		this.checkedQueue = checkedQueue;
		this.bank = bank;
	}

	@Override
	public void run() {
		for (int i = 1; i < App.NUM_OF_THREAD_ACCOUNT_TRANSFER; i++) {
			Object[] accountKeys = bank.getBankStorage().keySet().toArray();

			Object key1 = accountKeys[new Random().nextInt(accountKeys.length)];
			Object key2 = accountKeys[new Random().nextInt(accountKeys.length)];

			try {

				boolean maxTransfer = (Math.random() < 0.2);
				long amountToTransfer;
				
				if (maxTransfer) {
					amountToTransfer = new LongRandom().nextLong(App.MAX_TRANSFER_MONEY);
				} else {
					amountToTransfer = new LongRandom().nextLong(App.AVG_TRANSFER_MONEY);
				}

				String transactionId = GenerateAccountData.getAccountNumber(App.TRANSACTION_ID_LENGTH);
				
				Transaction t = new Transaction(bank.getBankStorage().get(key1), bank.getBankStorage().get(key2),
						amountToTransfer, transactionId);
				
				System.out.println("Add transaction with amountToTransfer:"+amountToTransfer);
				if(amountToTransfer>=App.AMOUNT_FOR_CHECK){
					//TODO Подумать как можно сделать чтоб копить транзакции пока не прошел проверку 
					// пока блокирую до выяснения обстоятельств
					//t.getAccountFrom().setBlock(App.AMOUNT_BLOCK);
					//t.getAccountFrom().setBlock(App.AMOUNT_BLOCK);
					System.out.println("Add to checkedQueue");
					checkedQueue.add(t);
				}else{
					//System.out.println("Add to currentQueue");
					currentQueue.add(t);
				}
				// bank.transferMoney(bank.getBankStorage().get(key1),
				// bank.getBankStorage().get(key2), amountToTransfer);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		}
	}

}
