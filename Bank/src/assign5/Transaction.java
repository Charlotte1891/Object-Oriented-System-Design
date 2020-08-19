package assign5;


public class Transaction{
	private int fromId;
	private int toId;
	private int amount;
	
	
	public Transaction(int fromId, int toId, int amount) {
		this.fromId = fromId;
		this.toId = toId;
		this.amount = amount;
	}
	
	
	public int getFromId() {
		return fromId;
	}
	
	
	public int getToId() {
		return toId;
	}
	
	
	public int getAmount() {
		return amount;
	}
	
}