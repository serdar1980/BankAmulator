package ru.serdar1980.bank;

public interface IDistribuitionCenterBankService<T> {
	 public void addQueue(T bank);
	 public boolean unloadInProgress(T bank);
	 public void departureQueue(T bank);
}
