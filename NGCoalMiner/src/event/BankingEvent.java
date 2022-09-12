package event;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.utility.ConditionalSleep;

public class BankingEvent extends Event {

	@Override public int execute() throws InterruptedException {
		if (!getInventory().isFull()) setFinished();
		
		if (!Banks.FALADOR_EAST.contains(myPlayer())) {
			getWalking().webWalk(Banks.FALADOR_EAST);
		} else if (getBank().isOpen()) {
			getBank().depositAllExcept(new Filter<Item>() {
				@Override public boolean match(Item i) {
					return i.getName().contains("pickaxe");
				}
			});
			getBank().close();
			setFinished();
		} else {
			RS2Object booth = getObjects().closest("Bank booth");
			if (booth != null) {
				booth.interact("Bank");
				new ConditionalSleep(10_000) {
					@Override public boolean condition() throws InterruptedException {
						return getBank().isOpen();
					}
				}.sleep();
			}
		}
		return 300;
	}

}
