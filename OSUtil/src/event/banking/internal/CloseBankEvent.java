package event.banking.internal;

import org.osbot.rs07.event.Event;

public class CloseBankEvent extends Event {
	@Override public int execute() throws InterruptedException {
		if (!getBank().isOpen()) setFinished();
		getBank().close();
		
		return 1_000;
	}

}
