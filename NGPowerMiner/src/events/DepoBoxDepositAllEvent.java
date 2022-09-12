package events;

import org.osbot.rs07.event.Event;

public class DepoBoxDepositAllEvent extends Event {
	
	@Override public int execute() throws InterruptedException {
		if (!getDepositBox().isOpen()) {
			setFailed();
			return 100;
		}
		if (getInventory().isEmpty()) {
			setFinished();
			return 100;
		}
		
		getDepositBox().depositAll();
		
		return 1_000;
	}

}
