package ru.serdar1980.bank;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {

	private static DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SSSSSSS");
	private final String trasactionId;
	private final AccountImpl accountFrom;
	private final AccountImpl accountTo;
	private final long amountTransfer;
	private final Date transactionDate;

	public Transaction(AccountImpl accountFrom, AccountImpl accountTo, long amountTransfer, String trasactionId) {
		this.trasactionId = trasactionId;
		this.accountFrom = accountFrom;
		this.accountTo = accountTo;
		this.amountTransfer = amountTransfer;
		this.transactionDate = new Date();
	}

	public AccountImpl getAccountFrom() {
		return accountFrom;
	}

	public AccountImpl getAccountTo() {
		return accountTo;
	}

	public long getAmountTransfer() {
		return amountTransfer;
	}

	@Override
	public int hashCode() {
		return trasactionId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Transaction))
			return false;
		if (obj == this)
			return true;

		Transaction t = (Transaction) obj;
		return this.trasactionId.equals(t.trasactionId);
	}

	@Override
	public String toString() {
		return new StringBuilder(" Transaction from:").append(accountFrom.getAccNumber()).append(" To")
				.append(accountTo.getAccNumber()).append(" amountTransfer").append(amountTransfer).append(" date")
				.append(df.format(transactionDate.getTime())).toString();
	}

}
