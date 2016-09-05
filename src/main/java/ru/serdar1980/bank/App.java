package ru.serdar1980.bank;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.weld.environment.se.events.ContainerInitialized;

import ru.serdar1980.bank.comsumer.CheckedCunsumer;
import ru.serdar1980.bank.provider.TransferProducer;
import ru.serdar1980.bank.utils.BankInit;
import ru.serdar1980.bank.utils.LongRandom;

/**
 * Bank App
 *
 */
public class App 
{
	//TODO вынести в конфиг 
	public static final int NUM_OF_THREAD_ACCOUNT_TRANSFER = 10;
	public static final int NUM_OF_THREAD = 150;
	public static final int NUM_OF_ACCOUNTS = 1500;
	public static final long MAX_TRANSFER_MONEY= 180000;
	public static final long MAX_MONEY_ON_ACCOUNT= 70000;
	public static final long AVG_TRANSFER_MONEY=45000;
	public static final int NUM_OF_TRANSACTION_CREATE = 10;
	public static final int ACCOUNT_ID_LENGTH = 32;
	public static final int TRANSACTION_ID_LENGTH = 128;
	public static final int AMOUNT_FOR_CHECK = 50000;
	public static final boolean AMOUNT_BLOCK = true;
	public static final boolean AMOUNT_UN_BLOCK = false;
	

	
	private static final CopyOnWriteArrayList<String> accountListOnChecking = new CopyOnWriteArrayList<>();
	private static final ConcurrentLinkedQueue<Transaction> currentQueue = new ConcurrentLinkedQueue<Transaction>();
	private static final ConcurrentLinkedQueue<Transaction> checkedQueue = new ConcurrentLinkedQueue<Transaction>();
	public static boolean STARTED = false;
	
	
	@Inject
	 private Bank bank;
	
    public void main( @Observes ContainerInitialized event )
    {
    	
    	List<Future<Integer>> list = new ArrayList<Future<Integer>>();
    	ExecutorService  serviceTrancationCreate = Executors.newFixedThreadPool(NUM_OF_THREAD);
    	ExecutorService  serviceInit = Executors.newFixedThreadPool(NUM_OF_THREAD);
    	ExecutorService  serviceTrancationTransfer  = Executors.newFixedThreadPool(NUM_OF_THREAD);
    	Thread checkedConsumer = new Thread(new CheckedCunsumer(App.checkedQueue, bank));
    	
    	// Create Accounts
        for (int i = 0;i < App.NUM_OF_THREAD;i++) {
        	Future<Integer> future = serviceInit.submit(new BankInit(bank));
        	list.add(future);
        }
        
        for(Future<Integer> fut : list){
            try {
                // будет задержка, потому что Future.get()
                // ждет пока таск закончит выполнение
                fut.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        System.out.println(bank.getBankStorage().size());
        serviceInit.shutdownNow();
        System.out.println("Bank load accounts");
        App.STARTED=true;
        System.out.println("Start create Transaction");
        for (int i = 0;i < App.NUM_OF_THREAD;i++) {
        	serviceTrancationCreate.execute(new TransferProducer(App.currentQueue, App.checkedQueue, bank));
        }
        serviceTrancationCreate.shutdownNow();
        
        checkedConsumer.start();
        
        System.out.println("Start transfer Transaction");
        for (int i = 0;i < App.NUM_OF_THREAD;i++) {
        	serviceTrancationTransfer.execute(new 	TransferProducer(App.currentQueue, App.checkedQueue, bank));
        }
        serviceTrancationTransfer.shutdownNow();
       
        
    }
}
