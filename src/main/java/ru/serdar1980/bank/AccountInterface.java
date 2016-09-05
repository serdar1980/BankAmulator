package ru.serdar1980.bank;

/**
 * 
 * @author Serdar
 * AccountInterface 
 */
public interface AccountInterface {
	void deposit(long deposit);
	void withdraw(long w);
	Boolean hasSufficientBalance(long w);
	long getBalance();
	boolean isBlock();
	void setBlock(boolean block);
	String getAccNumber();
}
