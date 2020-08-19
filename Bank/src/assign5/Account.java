package assign5;


import java.util.*;
import java.lang.Comparable.*;


public class Account{
	
    private int id;
    private int balance;
    private int txnCount;
    private Object Lock;
 
    public Account(int id, int balance) {
    this.id = id;
    this.balance = balance;
    this.txnCount = 0;
    Lock = new Object();
 }
 
 
    public void deposit(Transaction txn) {
	    synchronized(Lock) {
		    balance += txn.getAmount();
		    txnCount ++;
	    }
     }
 
 
    public void withdraw(Transaction txn) {
	    synchronized(Lock) {
		    balance -= txn.getAmount();
		    txnCount ++;
	    } 
    }
 
 
    public void selfTxn(Transaction txn) {
	    synchronized(Lock) {
		    txnCount ++;
	    }
    }
 
 
    public int getId() {
	    return id;
    }
 
 
    @Override
    public String toString() {
	    return "acct: " + id + " bal: " + balance + " trans: " + txnCount;
    }
 
}
 
 