package events;

import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.utility.ConditionalSleep;

import util.Counter;

public class DepositEvent extends Event {
	private Counter fishCounter;
	private String[] depositItems;
	public DepositEvent(Counter fishCounter, String... depositItems) {
		this.depositItems = depositItems;
		this.fishCounter = fishCounter;
	}
	
	
	@Override
	public int execute() throws InterruptedException {
		if (getDepositBox().isOpen()) {
			long n = getInventory().getAmount(depositItems);
			getDepositBox().depositAll(depositItems);
			getDepositBox().close();
			if (!getInventory().contains(depositItems))
				fishCounter.inc(n);
			setFinished();
		} else {
			RS2Object depo = getObjects().closest("Bank deposit box");
			if (depo != null) {
				depo.interact("Deposit");
				new ConditionalSleep(random(5_000, 10_000)) {
					@Override public boolean condition() throws InterruptedException {
						return !getDepositBox().isOpen();
					}
				}.sleep();
			} else setFailed();
		}
		return 300;
	}
}
