package event.cooking;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.utility.Condition;

import event.interact.UseItemOn;
import event.misc.AnimationFilterEvent;

/**
 * Cook the given food item in the given area
 * @finish No more of the given food item in inventory
 * @fail   No cooking station in area
 * 
 * @TODO:
 * 		* Fail on insufficient level
 * 
 * @author user
 *
 */
public class CookEvent extends Event {
	private Area area;
	private String foodName;
	
	public CookEvent (Area area, String foodName) {
		this.area = area;
		this.foodName = foodName;
	}
	
	private RS2Object getCookingStation() {
		// Cooking range
		RS2Object equipment = getObjects().closest(new Filter<RS2Object>() {
			@Override public boolean match(RS2Object obj) {
				return obj.getName().contains("Cooking") ||
					   obj.getName().equals("Range") ||
					   obj.getName().equals("Fire");
			}
		});
		
		return equipment;
	}

	@Override public int execute() throws InterruptedException {
		if (!getInventory().contains(foodName)) {
			log("[CookEvent][+] No more food, done");
			setFinished();
			return 100;
		}
		
		if (!area.contains(myPlayer())) getWalking().webWalk(area);
		
		RS2Object cookingStation = getCookingStation();
		if ( cookingStation == null ) {
			log("[CookEvent][-] No cooking station found");
			setFailed();
			return 100;
		}
		UseItemOn useEvent = new UseItemOn(area, foodName, getCookingStation().getName(), new Condition() {
			@Override public boolean evaluate() {
				return getWidgets().getWidgetContainingAction("Cook") != null;
			}
		});
		execute(useEvent);
		
		RS2Widget widget = getWidgets().getWidgetContainingAction("Cook");
		if (widget != null) {
			widget.interact("Cook");
			execute(new AnimationFilterEvent(3_000));
		}

		return 100;
	}
}
