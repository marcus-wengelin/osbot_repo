package event.crafting.internal;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.event.Event;

public class TanHidesUIEvent extends Event {

	private String leatherName;
	public TanHidesUIEvent(String leatherName) {
		this.leatherName = leatherName;
	}
	
	@Override
	public int execute() throws InterruptedException {
		if (getInventory().contains(new Filter<Item>() {
			@Override public boolean match(Item i) {
				return i.getName().contains("hide");
			}
		})) {
			RS2Widget widget = getWidgets().getWidgetContainingText(leatherName);
			
			if (widget == null) {
				log("[TanHidesUIEvent][-] Event failed, no leather widget");
				setFailed();
				return 100;
			}
			
			log("[TanHidesUIEvent][+] Interacting with tanning UI");
			widget.interact("Tan All");
			return random(1_000, 2_000);
		} else {
			setFinished();
		}
		
		
		return 100;
	}

}
