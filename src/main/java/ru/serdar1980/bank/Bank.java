package ru.serdar1980.bank;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Bank {
	private final Random random = new Random();

	private final ConcurrentMap<String, AccountImpl> bankStorage = new ConcurrentHashMap();

	public void transferMoney(AccountImpl fromAccount, AccountImpl toAccount, long amountToTransfer) throws Exception {
		AccountImpl firstLock, secondLock;

		StringBuilder strForlog = new StringBuilder();

		strForlog.append("Balance from Account ").append(fromAccount.getBalance()).append("\r\n");
		strForlog.append("Balance to Account ").append(toAccount.getBalance()).append("\r\n");

		strForlog.append("amountToTransfer:").append(amountToTransfer).append("\r\n");

		if (fromAccount.getAccNumber().compareTo(toAccount.getAccNumber()) == 0) {
			strForlog.append("Cannot transfer from account to itself").append("\r\n");
			throw new Exception(strForlog.toString());
		} else if (fromAccount.getAccNumber().compareTo(toAccount.getAccNumber()) == -1) {
			firstLock = fromAccount;
			secondLock = toAccount;
		} else {
			firstLock = toAccount;
			secondLock = fromAccount;
		}
		synchronized (firstLock) {
			synchronized (secondLock) {
				boolean checkNotBlock = fromAccount.isBlock() || toAccount.isBlock();
				if (checkNotBlock) {
					strForlog.append(
							String.format("Cannot transfer fromAccount:%s is block %b, toAccount:%s is block %b   ",
									fromAccount.getAccNumber(), fromAccount.isBlock(), toAccount.getAccNumber(),
									toAccount.isBlock()))
							.append("\r\n");
					throw new Exception(strForlog.toString());

				}
				if (fromAccount.hasSufficientBalance(amountToTransfer)) {
					fromAccount.withdraw(amountToTransfer);
					toAccount.deposit(amountToTransfer);

					strForlog.append("Balance from Account ").append(fromAccount.getBalance()).append("\r\n");
					strForlog.append("Balance to Account ").append(toAccount.getBalance()).append("\r\n");
					System.out.println(strForlog.toString());
				} else {
					strForlog.append("Cannot transfer from account no money").append("\r\n");
					;
					throw new Exception(strForlog.toString());
				}
			}
		}
	}

	public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount)
			throws InterruptedException {
		Thread.sleep(1000);
		return random.nextBoolean();
	}

	public ConcurrentMap<String, AccountImpl> getBankStorage() {
		return bankStorage;
	}

}
