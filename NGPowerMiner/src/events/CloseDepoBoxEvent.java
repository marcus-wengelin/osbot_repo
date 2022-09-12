package events;

import org.osbot.rs07.event.Event;

public class CloseDepoBoxEvent extends Event {
	@Override public int execute() throws InterruptedException {
		if (!getDepositBox().isOpen()) setFinished();
		getDepositBox().close();
		
		return 1_000;
	}

}
