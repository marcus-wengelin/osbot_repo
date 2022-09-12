package event.banking.internal;

import org.osbot.rs07.event.Event;

public class DepoBoxDepositAllEvent extends Event {
	private int[] targetIds;
	
	public DepoBoxDepositAllEvent(int[] targetIds) {
		this.targetIds = targetIds;
	}
	
	@Override public int execute() throws InterruptedException {
		if (!getDepositBox().isOpen()) {
			setFailed();
			return 100;
		}
		if (!getInventory().contains(targetIds)) {
			setFinished();
			return 100;
		}
		
		getDepositBox().depositAll(targetIds);
		
		return 1_000;
	}

}
