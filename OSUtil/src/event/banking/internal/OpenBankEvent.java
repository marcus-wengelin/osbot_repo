package event.banking.internal;

import org.osbot.rs07.api.filter.ActionFilter;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.event.Event;

public class OpenBankEvent extends Event {
	private RS2Object getClosestBank() {
		return getObjects().closest(new ActionFilter<RS2Object>("Bank"));
	}
	
	@Override public int execute() throws InterruptedException {
		if (myPlayer().isMoving()) return 1_000;
		
		if (getBank().isOpen()) {
			log("[OpenBankEvent] bank is open");
			setFinished();
			return 100;
		}
		
		RS2Object bank = getClosestBank();
		if (bank == null || !getMap().canReach(bank)) {
			log("[OpenBankEvent] did not find a reachable bank");
			setFailed();
			return 100;
		}
		
		bank.interact("Bank");
		
		return 1_000;
	}	
}