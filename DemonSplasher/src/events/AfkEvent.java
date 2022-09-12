package events;

import org.osbot.rs07.event.Event;

public class AfkEvent extends Event {
	private long sleepTime;
	private long startTime;
	
	public AfkEvent(long sleepTime) {
		this.sleepTime = sleepTime;
		this.startTime = System.currentTimeMillis();
	}

	@Override
	public int execute() throws InterruptedException {
		
		// Stop if we afk for long enough
		if (startTime + sleepTime < System.currentTimeMillis())
			setFinished();
		// Stop if demon dies
		else if (npcs.closest("Lesser demon") == null)
			setFinished();
		else if (mouse.isOnScreen())
			mouse.moveOutsideScreen();
		return 300;
	}
}
