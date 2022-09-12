package events;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.utility.ConditionalSleep;

public class DepositEvent extends Event {
	
	private static final Area CHEST_AREA = new Area(
		    new int[][]{
		        { 3759, 5664 },
		        { 3761, 5666 },
		        { 3759, 5668 },
		        { 3757, 5668 },
		        { 3757, 5664 }
		    }
		);
	
	@Override public int execute() throws InterruptedException {
		if (getBank().isOpen()) {
			getBank().depositAllExcept("Hammer");
			if (new ConditionalSleep(5_000) {
				@Override public boolean condition() throws InterruptedException {
					return getInventory().onlyContains("Hammer");
				}
			}.sleep()) {
				getBank().close();
				setFinished();
			}
		} else {
			if (!CHEST_AREA.contains(myPlayer())) getWalking().webWalk(CHEST_AREA);
			RS2Object chest = getObjects().closest("Bank chest");
			if (chest != null) {
				chest.interact("Use");
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
