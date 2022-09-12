package events;

import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.input.mouse.InventorySlotDestination;

public class DropEvent extends Event {

	private String[] oreNames;
	public DropEvent(String... oreNames) {
		this.oreNames = oreNames;
		setBlocking();
	}
	
	@Override
	public int execute() throws InterruptedException {
		log("Executing drop event");
		if (getInventory().contains(oreNames)) {
			getKeyboard().pressKey(16);
			for (int col = 0; col < 4; col++) {
				for (int row = 0; row < 7; row++) {
					Item item = getInventory().getItemInSlot(col + row*4);
					if (item != null && item.nameContains(oreNames)) {
						getMouse().move(new InventorySlotDestination(getBot(), col + row*4));
						getMouse().click(false);
					}
				}
			}
			getKeyboard().releaseKey(16);
		} else {
			setFinished();
		}

		return 300;
	}

}
