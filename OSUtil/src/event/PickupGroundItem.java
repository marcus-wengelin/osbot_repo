package event;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.utility.ConditionalSleep;
/**
 * An event which WebWalks to a certain area and picks up
 * some amount of some given item.
 * 
 * @author Marcus
 * 
 */
public class PickupGroundItem extends Event {

	private Area area;
	private String name;
	private int amount;
	
	public PickupGroundItem(Area area, String name, int amount) {
		this.area = area;
		this.name = name;
		this.amount = amount;
	}
	
	@Override
	public int execute() throws InterruptedException {
		if (inventory.getAmount(name) >= amount) setFinished();
		
		if (area.contains(myPlayer())) {
			GroundItem item = groundItems.closest(name);
			if (item != null) {
				if (!map.canReach(item)) {
					walking.walk(item.getPosition());
				} else {
					item.interact("Take");
					new ConditionalSleep(random(1_000, 5_000)) {
						@Override public boolean condition() throws InterruptedException {
							return !item.exists();
						}
					}.sleep();
				}
			}
		} else {
			walking.webWalk(area);
		}
		
		return 100;
	}

}
