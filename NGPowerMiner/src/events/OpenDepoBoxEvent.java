package events;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.event.Event;

public class OpenDepoBoxEvent extends Event {
	private RS2Object getClosestDepositBox() {
		RS2Object depoBox = getObjects().closest(new Filter<RS2Object>() {
			@Override public boolean match(RS2Object o) {
				return o.getName().equals("Bank Deposit Box") &&
					getMap().canReach(o);
			}
		});
		
		return depoBox;
	}
	
	@Override public int execute() throws InterruptedException {
		if (myPlayer().isMoving()) return 1_000;
		
		if (getDepositBox().isOpen()) {
			log("[OpenDepoBoxEvent] deposit box open");
			setFinished();
			return 100;
		}
		
		RS2Object depoBox = getClosestDepositBox();
		if (depoBox == null) {
			log("[OpenDepoBoxEvent] did not find a reachable deposit box");
			setFailed();
			return 100;
		}
		
		depoBox.interact("Deposit");
		
		return 1_000;
	}
	
}
