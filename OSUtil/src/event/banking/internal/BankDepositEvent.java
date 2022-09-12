package event.banking.internal;

import org.osbot.rs07.event.Event;

public class BankDepositEvent extends Event {
	private String name;
	
	public BankDepositEvent(String name) {
		this.name = name;
	}
	
	@Override public int execute() throws InterruptedException {
		if (!getBank().isOpen()) {
			setFailed();
			return 100;
		}
		if (!getInventory().contains(name)) {
			setFinished();
			return 100;
		}
		
		getBank().depositAll(name);
		
		return 1_000;
	}

}
