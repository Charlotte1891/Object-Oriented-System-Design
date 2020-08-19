package assign5;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.CountDownLatch;



public class Cracker {
	
	// Array of chars used to produce strings
		public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();	
		
		private static int passwordLength;
		private static int numOfThreads;
		private static List<String> results;
		private static CountDownLatch latch;
		
		
	
	public static void main(String[] args) {
		// if input length = 1: generating mode
		if(args.length == 1) {
			System.out.println(generatingMode(args[0]));
		}
		// if input length = 3: cracking mode
		if(args.length == 3) {
			crackingMode(args[0], args[1], args[2]);
			if(results.isEmpty()) {
				System.out.println("No result found!");
			}else {
				for(String result : results) {
					System.out.println(result);
				}
			}
			System.out.println("all done");
		}
	}
	
	
	
	// Generating Mode
	private static String generatingMode(String toHash) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(toHash.getBytes());
			return hexToString(md.digest());
		}catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	
	// Cracking Mode
	private static void crackingMode(String hashValue, String pwLength, String numOfT) {
		
		results = Collections.synchronizedList(new ArrayList<String>());
		
		// set password length
		try {
			int pw = Integer.parseInt(pwLength);
			passwordLength = pw;
		}catch(NumberFormatException e) {
			System.err.println("Wrong input format for password length.");
		}
		
		// set number of threads
		try {
			int nt = Integer.parseInt(numOfT);
			numOfThreads = (nt > 0) ? nt : 1;
		}catch(NumberFormatException e) {
			System.err.println("Wrong input format for number of threads.");
		}
		
		// count down latch
		latch = new CountDownLatch(numOfThreads);
		
		// start workers
		int segmentLength = CHARS.length/numOfThreads;
		Worker[] workers = new Worker[numOfThreads];
		for(int i = 0; i < numOfThreads; i ++) {
			if(i != numOfThreads - 1) {
				workers[i] = new Worker(i*segmentLength, (i+1)*segmentLength, hashValue);
			}else {
				workers[i] = new Worker((numOfThreads - 1) * segmentLength, CHARS.length, hashValue);
			}
		}
		for(int i = 0; i < numOfThreads; i++) {
			workers[i].start();
		}
		
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	
	// create Worker class
	static class Worker extends Thread {
		private int start;
		private int end;
		private String hashValue;
		private boolean solved;
		
		
		public Worker(int start, int end, String hashValue) {
			this.start = start;
			this.end = end;
			this.hashValue = hashValue;
			solved = false;
		}
		
		
		public void run() {
			for(int i = start; i < end; i ++) {
				char curr = CHARS[i];
				recursiveHelper("" + curr);
			}
			latch.countDown();
		}
		
		
		
		private void recursiveHelper(String curr) {
			if(!solved) {
				if(curr.length() == passwordLength) {
					String curHash = generatingMode(curr);
					if(curHash.contentEquals(hashValue)) {
						results.add(curr);
						solved = true;
						return;
				    }
				}else {
					for(char ch:CHARS) {
						recursiveHelper(curr + ch);
					}
				}
			}
		}
		
	}
	
	
	/*
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code)
	*/
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	
	
	/*
	 Given a string of hex byte values such as "24a26f", creates
	 a byte[] array of those values, one byte value -128..127
	 for each 2 chars.
	 (provided code)
	*/
	public static byte[] hexToArray(String hex) {
		byte[] result = new byte[hex.length()/2];
		for (int i=0; i<hex.length(); i+=2) {
			result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
		}
		return result;
	}
	
	
	// possible test values:
	// a ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb
	// fm 440f3041c89adee0f2ad780704bcc0efae1bdb30f8d77dc455a2f6c823b87ca0
	// a! 242ed53862c43c5be5f2c5213586d50724138dea7ae1d8760752c91f315dcd31
	// xyz 3608bca1e44ea6c4d268eb6db02260269892c0b42b86bbf1e77a6fa16c3c9282

}
