package events;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.event.Event;

public class DropAllEvent extends Event {
	@Override
	public int execute() throws InterruptedException {
		if (getInventory().isFull()) {
			getInventory().dropAllExcept(new Filter<Item>() {
				@Override
				public boolean match(Item item) {
					return item.getName().contains("pickaxe");
				}
			});
		} else setFinished();
		return 300;
	}

}
