package event.combat;

import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.utility.ConditionalSleep;

public class BuryBonesEvent extends Event {

	@Override public int execute() throws InterruptedException {
		if (!getInventory().contains("Bones")) {
			setFinished();
			return 100;
		}
		
		Item bones = getInventory().getItem("Bones");
		if (bones != null) {
			long amt = getInventory().getAmount("Bones");
			bones.interact("Bury");
			
			new ConditionalSleep(5_000) {
				@Override public boolean condition() throws InterruptedException {
					return getInventory().getAmount("Bones") < amt && !myPlayer().isAnimating();
				}
			}.sleep();
		}
		
		return 100;
	}
}
