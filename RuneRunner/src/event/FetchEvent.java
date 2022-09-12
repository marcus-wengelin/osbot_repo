package event;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.utility.ConditionalSleep;

public class FetchEvent extends Event {
	
	private static final Area BANK_AREA =
			new Area(3098, 3499, 3091, 3488);
	private long numEssence = -1;
	
	private void withdrawRuneEssence() {
		if (!bank.isOpen()) {
			RS2Object banker = objects.closest("Bank booth");
			if (banker == null) {
				logger.warn("Unable to find banker, retrying...");
			} else {
				banker.interact("Bank");
				new ConditionalSleep(10_000) {
					@Override public boolean condition() throws InterruptedException {
						return bank.isOpen();
					}
				}.sleep();
			}
		} else {
			bank.withdrawAll("Rune essence");
			new ConditionalSleep(10_000) {
				@Override public boolean condition() throws InterruptedException {
					return inventory.getAmount("Rune essence") > numEssence;
				}
			}.sleep();
		}
	}
	
	@Override
	public void onStart() {
		numEssence = inventory.getAmount("Rune essence");
	}
	
	@Override
	public int execute() throws InterruptedException {
		
		if (!BANK_AREA.contains(myPlayer()))
			walking.webWalk(BANK_AREA);
		else
			withdrawRuneEssence();
		
		if (inventory.isFull()) setFinished();
		
		return 300;
	}

}