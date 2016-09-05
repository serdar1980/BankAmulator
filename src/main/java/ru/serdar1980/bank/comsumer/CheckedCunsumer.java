package ru.serdar1980.bank.comsumer;

import java.util.concurrent.ConcurrentLinkedQueue;

import ru.serdar1980.bank.App;
import ru.serdar1980.bank.Bank;
import ru.serdar1980.bank.Transaction;

public class CheckedCunsumer implements Runnable {

	private ConcurrentLinkedQueue<Transaction> queue = new ConcurrentLinkedQueue<Transaction>();
	private Bank bank;

	public CheckedCunsumer(ConcurrentLinkedQueue<Transaction> queue, Bank bank) {
		this.queue = queue;
		this.bank = bank;
	}

	public void run() {
		Transaction t;
		System.out.println("Checked Cunsumer Started");
		while (App.STARTED) {
			while ((t = queue.poll()) != null) {
				try {

					System.out.println(new StringBuilder("Checked from:").append(t.getAccountFrom().getAccNumber())
							.append(" to:").append(t.getAccountTo().getAccNumber()).toString());
					if (bank.isFraud(t.getAccountFrom().getAccNumber(), t.getAccountTo().getAccNumber(),
							t.getAmountTransfer())) {
						System.out.println(new StringBuilder("Blocked from:").append(t.getAccountFrom().getAccNumber())
								.append(" to:").append(t.getAccountTo().getAccNumber()).toString());	
						// TODO может возникнуть ситуация, пока проходит
						// проверку спишут маленькими суммами
						// нет условия разблокировки
						t.getAccountFrom().setBlock(App.AMOUNT_BLOCK);
						t.getAccountFrom().setBlock(App.AMOUNT_BLOCK);
					} else {
						System.out.println(new StringBuilder("Transfer from:").append(t.getAccountFrom().getAccNumber())
								.append(" to:").append(t.getAccountTo().getAccNumber()).toString());
						bank.transferMoney(t.getAccountFrom(), t.getAccountTo(), t.getAmountTransfer());
					}
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
