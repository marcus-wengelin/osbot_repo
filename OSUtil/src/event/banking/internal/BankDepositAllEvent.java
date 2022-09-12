package event.banking.internal;

import org.osbot.rs07.event.Event;

public class BankDepositAllEvent extends Event {
	@Override public int execute() throws InterruptedException {
		if (!getBank().isOpen()) {
			setFailed();
			return 100;
		}
		if (getInventory().isEmpty()) {
			setFinished();
			return 100;
		}
		
		getBank().depositAll();
		return 100;
	}
}
