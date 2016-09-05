package ru.serdar1980.bank.utils;

import java.security.SecureRandom;
import java.util.Random;

import ru.serdar1980.bank.App;

import java.math.BigInteger;

/**
 * 
 * @author Serdar Вспомогательный класс для запонения @see
 *         {ru.serdar1980.bank.AccountImpl}
 */
public final class GenerateAccountData {

	/**
	 * Генерация номера счета  
	 * @return Возвращает строку указанной длины символа; 
	 */
	public static String getAccountNumber(int length) {
		final SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(length);
	}

	/**
	 * Генератор денег на счету 
	 * @return Возвращает количество денег на счету 
	 * 
	 */
	public static Long getAccountMoney() {
		
		return new LongRandom().nextLong(App.MAX_MONEY_ON_ACCOUNT);
	}
}
