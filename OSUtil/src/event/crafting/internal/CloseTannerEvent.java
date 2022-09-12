package event.crafting.internal;

import org.osbot.rs07.event.Event;

import util.Misc;

public class CloseTannerEvent extends Event {

	@Override
	public int execute() throws InterruptedException {
		if (!Misc.isTannerOpen(this)) {
			log("[CloseTannerEvent][+] Closed tanner widget");
			setFinished();
		}
		
		getWidgets().closeOpenInterface();
		return random(500, 1_000);
	}

}
