package event.interact;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.utility.Condition;

/**
 * Generic use inventory item on
 * world object event.
 * 
 * @author user
 *
 */
public class UseItemOn extends Event {
	private Area area;
	private String itemName;
	private String objectName;
	private Condition finished;
	
	public UseItemOn(Area area, String itemName, String objectName, Condition finished) {
		this.area = area;
		this.itemName = itemName;
		this.objectName = objectName;
		this.finished = finished;
	}
	
	@Override public int execute() throws InterruptedException {
		// Check finished condition
		if ( finished.evaluate() ) {
			setFinished();
			return 100;
		}
		
		// If not in right area, walk there
		if ( !area.contains(myPlayer())) { 
			getWalking().webWalk(area);
			return 100;
		}
		
		
		// Fetch object and do null sanity check
		RS2Object obj = getObjects().closest(objectName);
		if ( obj == null ) {
			log(String.format("[UseItemOn][-] Object `%s` not found!", objectName));
			setFailed();
			return 100;
		}
		
		// If object is not reachable, walk to it
		if (!getMap().canReach(obj)) {
			getWalking().webWalk(obj.getPosition());
		}
		
		// Fetch item and do null sanity check
		Item it = getInventory().getItem(itemName);
		if ( it == null ) {
			log(String.format("[UseItemOn][-] Item `%s` not found!", itemName));
			setFailed();
			return 100;
		}
		
		// Use item on object
		it.interact("Use", itemName);
		obj.interact("Use");
		
		return random(1_000, 3_000);
	}

}
