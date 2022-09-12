package event.prayer;

import org.osbot.rs07.api.filter.ActionFilter;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.event.Event;

/**
 * Buries all items with the "Bury" action in inventory.
 * 
 * @finish when all items are buried
 * 
 * @author user
 *
 */
public class BuryAllEvent extends Event {

	@Override public int execute() throws InterruptedException {
		if (myPlayer().isAnimating())
			return random(800, 1_200);
		
		if (!getInventory().contains(new ActionFilter<Item>("Bury"))) {
			log("[BuryAllEvent][+] All items buried");
			setFinished();
			return 100;
		}
		
		Item bones = getInventory().getItem(new ActionFilter<Item>("Bury"));
		if (bones != null) bones.interact("Bury");
		
		return 100;
	}
}
