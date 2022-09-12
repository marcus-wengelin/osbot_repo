package event.combat;

import java.util.List;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.utility.ConditionalSleep;

public class LootCloseEvent extends Event {
	private boolean foundItems;
	private long timeout;
	private long startTime;
	private Filter<GroundItem> lootFilter;

	public LootCloseEvent(Position position, long timeout, Filter<GroundItem> lootFilter) {
		this.timeout    = timeout;
		this.lootFilter = lootFilter;

		this.foundItems = false;
		this.startTime = System.currentTimeMillis();
	}

	@Override
	public int execute() throws InterruptedException {
		if (System.currentTimeMillis() > startTime + timeout) {
			log("[LootEvent][-] Timeout exceeded");
			setFailed();
			return 100;
		}

		List<GroundItem> items = getGroundItems().getAll();
		
		for (GroundItem gi : items) {
			if (getMap().distance(gi.getPosition()) < 2) { 
				if (lootFilter.match(gi)) {
					log("[LootEvent][+] Looting item "+gi.getName());
					long amt = getInventory().getAmount(gi.getName());

					gi.interact("Take");

					if(new ConditionalSleep(5_000) {
						@Override public boolean condition() throws InterruptedException {
							return getInventory().getAmount(gi.getName()) > amt && !myPlayer().isAnimating();
						}
					}.sleep())
						foundItems = true;
					
				}
			}
		}

		if (foundItems) {
			log("[LootEvent][+] Done looting");
			setFinished();
		}
		return 100;
	}
}