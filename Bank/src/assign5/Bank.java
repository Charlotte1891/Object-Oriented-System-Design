package assign5;


import java.io.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.*;


public class Bank {
	private final static int ACCOUNT_NUM = 20;
	private final static int INI_BALANCE = 1000;
	private final static int QUEUE_SIZE = 100;
	
	private static Account[] accounts;
	private static ArrayBlockingQueue<Transaction> queue;
	private static int numOfWorkers;
	private static Transaction nullTrans = new Transaction(-1, 0, 0);
	private static CountDownLatch latch;
	

	public static void main(String[] args) {
		// set up initial accounts
		accounts = new Account[ACCOUNT_NUM];
		for(int i = 0; i < ACCOUNT_NUM; i++) {
			accounts[i] = new Account(i, INI_BALANCE);
		}

		queue = new ArrayBlockingQueue<Transaction>(QUEUE_SIZE);

		// create and start worker threads
		numOfWorkers = Integer.parseInt(args[1]);
		for(int i = 0; i < numOfWorkers; i++) {
			Worker w = new Worker();
			w.start();
		}
		
		latch = new CountDownLatch(numOfWorkers);

        // get info from input file
		processFromInput(args);

		try {
			latch.await();
			for(int i = 0; i < ACCOUNT_NUM; i++) {
				System.out.println(accounts[i]);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}     
	}
	

	
	private static void processFromInput(String[] args) {
		try {
			String fileName = args[0];
			BufferedReader br = new BufferedReader(new FileReader(fileName));

			String inputLine = "";
			while((inputLine = br.readLine()) != null) {
				String[] lines = inputLine.split(" ");
				int fromId = Integer.parseInt(lines[0]);
				int toId = Integer.parseInt(lines[1]);
				int amount = Integer.parseInt(lines[2]);
				try {
					queue.put(new Transaction(fromId, toId, amount));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			br.close();
			
			// add one nullTrans per Worker thread
			for(int i = 0; i < numOfWorkers; i++) {
				try {
					queue.put(nullTrans);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}


	public static class Worker extends Thread {
		@Override
		public void run() {
		    try{
				Transaction current = queue.take();
				while (!current.equals(nullTrans)) {
					process(current);
					current = queue.take();
				}
				if (current.equals(nullTrans)) {
					queue.put(current);
				}
				latch.countDown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		
		
		private static void process(Transaction trans) {
			int fromId = trans.getFromId();
			int toId = trans.getToId();
			
			if(fromId == toId) {
				accounts[fromId].selfTxn(trans);
			} else {
				accounts[fromId].withdraw(trans);
				accounts[toId].deposit(trans);
			}
		}
	}
	
 }







