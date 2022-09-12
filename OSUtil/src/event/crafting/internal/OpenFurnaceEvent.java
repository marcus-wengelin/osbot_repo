package event.crafting.internal;

import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.event.Event;

public class OpenFurnaceEvent extends Event {

	private boolean isFurnaceOpen() {
		return getWidgets().getWidgetContainingAction("Smelt") != null;
	}
	
	@Override
	public int execute() throws InterruptedException {
		if (isFurnaceOpen()) {
			log("[OpenFurnaceEvent][+] Furnace is open");
			setFinished();
			return 100;
		}
		
		RS2Object furnace = getObjects().closest("Furnace");
		if (furnace == null) {
			log("[OpenFurnaceEvent][-] Furnace not found");
			setFailed();
			return 100;
		}
		
		furnace.interact("Smelt");
		
		return random(1_000, 3_000);
	}
}
