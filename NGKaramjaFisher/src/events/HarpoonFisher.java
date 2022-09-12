package events;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.utility.ConditionalSleep;

public class HarpoonFisher extends Event {
	private NPC fishingSpot;
	
	public void findSpot() {
		NPC npc = getNpcs().closest(new Filter<NPC>() {
			@Override 
			public boolean match(NPC npc) {
				return (npc.getName().equals("Fishing spot") && npc.hasAction("Harpoon"));
			}
		});
		
		if (npc == null) setFailed();
		else			 fishingSpot = npc;
	}
	
	@Override
	public int execute() throws InterruptedException {
		if (!getInventory().contains("Harpoon")) {
			log("[ERROR] No harpoon found");
			setFailed();
		} 
		if (getSkills().getStatic(Skill.FISHING) < 35) {
			log("[ERROR] Too low fishing level");
			setFailed();
		}
		
		if (getInventory().isFull() && getInventory().contains("Raw tuna"))
			getInventory().dropAll("Raw tuna");
		
		if (!getInventory().isFull()) {
			if (!myPlayer().isAnimating() && !myPlayer().isMoving()) {
				findSpot();
				fishingSpot.interact("Harpoon");
				getMouse().moveOutsideScreen();
				new ConditionalSleep(random(5_000, 10_000)) {
					@Override public boolean condition() throws InterruptedException {
						return !myPlayer().isAnimating();
					}
				}.sleep();
			}
		} else setFinished();
		
		return random(1_000, 5_000);
	}
}
